package com.cmpay.lemon.monitor.search;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.query.SearchResult;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author: zhou_xiong
 */
public abstract class AbstractEsSearcher implements EsSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEsSearcher.class);
    public static final String PRE_TAGS = "<mark>";
    public static final String POST_TAGS = "</mark>";
    protected static final String APP_FIELD = "fields.log_app";
    protected static final String TXCODE_FIELD = "log_txCode";
    protected static final String[] TXNPLTE_INDICE = new String[]{"log-*-txnplte-*"};
    protected static final String SUCCESS_CRITERIA = "*00000";
    protected static final String TX_RETRUN = "log_txReturn";
    protected static final String TX_DURATION = "log_txDuration";
    protected static final String ICS_SYSERROR = "SCM*";
    protected static final String ICS_SYSERROR_BUTNOT = "SCM00000";
    protected static final String LEMON_SYSERROR = "SYS*";


    protected RestHighLevelClient client;

    public AbstractEsSearcher(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public SearchResponse query(SearchRequest request) throws IOException {
        return client.search(request, RequestOptions.DEFAULT);
    }

    @Override
    public void exception(Exception e) {
        LOGGER.error("search failed", e);
        BusinessException.throwBusinessException(MsgEnum.SEARCH_FAILED);
    }

    @Override
    public SearchResult responseProcess(SearchResponse response) {
        return null;
    }
}
