package com.cmpay.lemon.monitor.http;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author: zhou_xiong
 */
public class SendProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendProcessor.class);
    private static final String CONTENT_TYEP = "Content-type";
    private static final String OK = "0";
    private final RestTemplate loadBalancedRestTemplate;
    private final RestTemplate httpRestTemplate;

    public SendProcessor(RestTemplate loadBalancedRestTemplate, RestTemplate httpRestTemplate) {
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.httpRestTemplate = httpRestTemplate;
    }

    public <T extends SearchReqBody> SearchResponse exchange(String url, HttpMethod method, T body, Boolean useRibbon) {
        RestTemplate restTemplate = loadBalancedRestTemplate;
        if (useRibbon == null || !useRibbon) {
            restTemplate = httpRestTemplate;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYEP, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        long start = System.currentTimeMillis();
        SearchResponse rsp = restTemplate.exchange(url, method, requestEntity, SearchResponse.class).getBody();
        LOGGER.debug("search elapsed:{}ms", System.currentTimeMillis() - start);
        if (JudgeUtils.notEquals(OK, rsp.getCode())) {
            BusinessException.throwBusinessException(MsgEnum.SEARCH_FAILED);
        }
        return rsp;
    }

    public static class SearchResponse {
        private String code;
        private String msg;
        private String recordNum;
        private String datas;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getRecordNum() {
            return recordNum;
        }

        public void setRecordNum(String recordNum) {
            this.recordNum = recordNum;
        }

        public String getDatas() {
            return datas;
        }

        public void setDatas(String datas) {
            this.datas = datas;
        }

        @Override
        public String toString() {
            return "SearchResponse{" +
                    "code='" + code + '\'' +
                    ", msg='" + msg + '\'' +
                    ", recordNum='" + recordNum + '\'' +
                    ", datas='" + datas + '\'' +
                    '}';
        }
    }
}
