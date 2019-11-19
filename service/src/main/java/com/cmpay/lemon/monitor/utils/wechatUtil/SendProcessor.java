package com.cmpay.lemon.monitor.utils.wechatUtil;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhou_xiong
 */
@Component
public class SendProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendProcessor.class);
    private static final String CONTENT_TYEP = "Content-type";
    private static final String ERRCODE = "errcode";
    private static final int OK = 0;
    private RestTemplate httpRestTemplate;
    private ObjectMapper objectMapper;

    public SendProcessor(RestTemplate httpRestTemplate, ObjectMapper objectMapper) {
        this.httpRestTemplate = httpRestTemplate;
        this.objectMapper = objectMapper;
    }
    public SendProcessor() {
    }

    public <T> Map<String, Object> exchange(String url, HttpMethod method, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYEP, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        Map<String, Object> resultMap = new HashMap<>(16);
        long start = System.currentTimeMillis();
        try {
            ResponseEntity<String> responseEntity = httpRestTemplate.exchange(url, method, requestEntity, String.class);
            LOGGER.debug("wechatUtil query:[{}] elapsed:{}ms", url, System.currentTimeMillis() - start);
            resultMap = objectMapper.readValue(responseEntity.getBody(), Map.class);
        } catch (Exception ignored) {
            LOGGER.error("wechatUtil query:[{}] I/O exception", url, ignored);
        }
        return resultMap;
    }

    public String uploadTempImage(File file, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", resource);
        parts.add("name", "media");
        parts.add("filename", file.getName());
        parts.add("filelength", file.length());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, headers);
        long start = System.currentTimeMillis();
        ResponseEntity<Map> responseEntity = httpRestTemplate.postForEntity(url, httpEntity, Map.class);
        LOGGER.debug("wechatUtil query:[{}] elapsed:{}ms", url, System.currentTimeMillis() - start);
        Map body = responseEntity.getBody();
        if ((int) body.get(ERRCODE) != OK) {
            LOGGER.error("wechatUtil query:[{}] failed", url);
            BusinessException.throwBusinessException(MsgEnum.WECHAT_QUERY_FAILED);
        }
        return (String) body.get("media_id");
    }
}
