package com.cmpay.lemon.monitor.search.page;

import com.cmpay.lemon.monitor.bo.TxnPlteLogBO;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.search.AbstractEsSearcher;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.TYPE;
import static com.cmpay.lemon.monitor.utils.SearchUtils.removeHighlightTagsIfNecessary;

/**
 * @author: zhou_xiong
 */
@Component
public class TxnPlteLogSearch extends AbstractEsSearcher {

    public TxnPlteLogSearch(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        TxnPlteLogBO txnPlteLogBO = Optional.ofNullable(t).filter(a -> a instanceof TxnPlteLogBO)
                .map(b -> (TxnPlteLogBO) b).orElse(new TxnPlteLogBO());
        return getSearchRequest(txnPlteLogBO);
    }

    private SearchRequest getSearchRequest(TxnPlteLogBO txnPlteLogBO) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.matchQuery("log_requestId", removeHighlightTagsIfNecessary(txnPlteLogBO.getLogRequestId()))).minimumShouldMatch(1);
        // .sort(TIME_FILED, SortOrder.ASC)
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder);
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }

    @Override
    public SearchResult responseProcess(SearchResponse response) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        return new SearchResult(response.getHits().totalHits, list);
    }
}
