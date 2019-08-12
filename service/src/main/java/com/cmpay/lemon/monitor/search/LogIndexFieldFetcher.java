package com.cmpay.lemon.monitor.search;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.exception.ErrorMsgCode;
import com.cmpay.lemon.common.utils.StringUtils;
import org.elasticsearch.action.fieldcaps.FieldCapabilities;
import org.elasticsearch.action.fieldcaps.FieldCapabilitiesRequest;
import org.elasticsearch.action.fieldcaps.FieldCapabilitiesResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhou_xiong
 */
@Component
public class LogIndexFieldFetcher {
    @Autowired
    private RestHighLevelClient esClient;

    public Map<String, String> fetchIndexFields(String name) {
        return filterUserFields(name).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));
    }

    private Map<String, List<String>> filterUserFields(String name) {
        try {
            return fetchFieldCapabilities(name).entrySet().stream()
                    .filter(entry -> !StringUtils.startsWith(entry.getKey(), "_"))
                    .filter(entry -> !StringUtils.startsWith(entry.getKey(), "@")
                            || StringUtils.equals(entry.getKey(), "@timestamp"))
                    .filter(entry -> !entry.getValue().keySet().contains("object"))
                    .filter(entry -> !StringUtils.endsWith(entry.getKey(), ".keyword"))
                    .filter(entry -> !StringUtils.startsWith(entry.getKey(), "log."))
                    .filter(entry -> !StringUtils.equals(entry.getKey(), "tags"))
                    .filter(entry -> entry.getValue().keySet().size() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue().keySet())));
        } catch (IOException e) {
            BusinessException.throwBusinessException(ErrorMsgCode.CLIENT_TIMEOUT);
        }
        return null;
    }

    private Map<String, Map<String, FieldCapabilities>> fetchFieldCapabilities(String name) throws IOException {
        FieldCapabilitiesRequest request = new FieldCapabilitiesRequest()
                .indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN)
                .indices(name).fields("*");
        FieldCapabilitiesResponse response = esClient.fieldCaps(request, RequestOptions.DEFAULT);
        return response.get();
    }
}
