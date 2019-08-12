package com.cmpay.lemon.monitor.utils;


import com.cmpay.lemon.common.utils.DateTimeUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.bo.CenterBO;
import com.cmpay.lemon.monitor.bo.PageQueryBO;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.time.LocalDateTime;
import java.util.*;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.*;
import static com.cmpay.lemon.monitor.search.AbstractEsSearcher.POST_TAGS;
import static com.cmpay.lemon.monitor.search.AbstractEsSearcher.PRE_TAGS;

/**
 * @author: zhou_xiong
 */
public class SearchUtils {
    public static final String PREFIX = "log-";
    public static final HighlightBuilder HIGHLIGHT_BUILDER = new HighlightBuilder().field("*").preTags(PRE_TAGS).postTags(POST_TAGS);

    private SearchUtils() {
    }

    public static String[] getIndicesByDateStr(String startTime, String endTime) {
        String start = StringUtils.substring(startTime, 0, 8);
        String end = StringUtils.substring(endTime, 0, 8);
        Long startLong = Long.valueOf(start);
        Long endLong = Long.valueOf(end);
        int size = (int) (endLong - startLong);
        List<String> indices = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PREFIX).append("*-").append(startLong + i);
            indices.add(stringBuilder.toString());
        }
        return indices.toArray(new String[size]);
    }

    public static String[] getIndicesByLogTypeAndDataStr(String start, String end, String logType) {
        String[] indices = getIndicesByDateStr(start, end);
        for (int i = 0; i < indices.length; i++) {
            String str = StringUtils.replace(indices[i], "*", logType);
            indices[i] = str;
        }
        return indices;
    }

    public static List<QueryBuilder> addTimeAndKeywordCriteria(LocalDateTime startTime, LocalDateTime endTime, String keyword, BoolQueryBuilder builder) {
        builder.must(QueryBuilders.rangeQuery(TIME_FILED).gte(startTime).lte(endTime));
        List<QueryBuilder> filters = builder.filter();
        if (JudgeUtils.isNotBlank(keyword)) {
            String[] keywords = StringUtils.split(keyword, SEPARATOR);
            for (String kw : keywords) {
                filters.add(QueryBuilders.multiMatchQuery(kw.trim()).lenient(true));
            }
        }
        return filters;
    }

    public static <T extends PageQueryBO> SearchSourceBuilder buildSearchSource(T t, BoolQueryBuilder builder) {
        int from = (t.getPageNum() - 1) * t.getPageSize();
        return new SearchSourceBuilder().query(builder)
                .from(from).size(t.getPageSize())
                .highlighter(HIGHLIGHT_BUILDER).sort(TIME_FILED, SortOrder.DESC);
    }

    public static Map<String, Object> resultReplaceHighlightField(SearchHit hit) {
        Map<String, Object> row = hit.getSourceAsMap();
        Map<String, HighlightField> highlights = hit.getHighlightFields();
        List<String> valueFields = new ArrayList<>();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (JudgeUtils.isNull(entry.getValue())) {
                continue;
            }
            if (entry.getValue() instanceof Map) {
                ((Map) entry.getValue()).forEach((k, v) -> valueFields.add(StringUtils.join(entry.getKey(), ".", k)));
            }
            if (highlights.containsKey(entry.getKey())) {
                row.put(entry.getKey(), concatText(highlights.get(entry.getKey()).fragments()));
            }
        }
        for (String valueField : valueFields) {
            if (highlights.containsKey(valueField)) {
                String[] parts = StringUtils.split(valueField, ".");
                String firstKey = parts[0];
                String secondKey = parts[1];
                ((Map) row.get(firstKey)).put(secondKey, concatText(highlights.get(valueField).fragments()));
            }
        }
        return row;
    }

    public static String concatText(Text[] texts) {
        StringBuilder sb = new StringBuilder();
        for (Text text : texts) {
            sb.append(text.string()).append("...");
        }
        return sb.delete(sb.length() - 3, sb.length()).toString();
    }

    public static String removeHighlightTagsIfNecessary(String requestId) {
        String removedPreTag = StringUtils.remove(requestId, PRE_TAGS);
        return StringUtils.remove(removedPreTag, POST_TAGS);
    }

    public static LocalDateTime[] getUTC(String[] logPeriods) {
        String start = logPeriods[0];
        String end = logPeriods[1];
        LocalDateTime startTime = DateTimeUtils.parseLocalDateTime(start).minusHours(8);
        LocalDateTime endTime = DateTimeUtils.parseLocalDateTime(end).minusHours(8);
        return new LocalDateTime[]{startTime, endTime};
    }

    public static void countByApp(Map<String, Long> countMap, List<? extends Terms.Bucket> buckets, List<CenterBO> centerBOS) {
        long all = 0;
        for (CenterBO centerBO : centerBOS) {
            long count = 0;
            if (StringUtils.equals(centerBO.getCenterName(), ALL)) {
                continue;
            }
            if (StringUtils.isBlank(centerBO.getApp())) {
                countMap.put(centerBO.getCenterName(), 0L);
            } else {
                List<String> apps = Arrays.asList(StringUtils.split(centerBO.getApp(), ";"));
                for (Terms.Bucket bucket : buckets) {
                    if (apps.contains(bucket.getKey())) {
                        count += bucket.getDocCount();
                    }
                }
                countMap.put(centerBO.getCenterName(), count);
                all += count;
            }
        }
        countMap.put(ALL, all);
    }

    public static Map<String, Long> bucketAsMap(Terms aggregation) {
        Map<String, Long> result = new HashMap<>(10);
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        buckets.forEach(b -> result.put((String) b.getKey(), b.getDocCount()));
        return result;
    }
}
