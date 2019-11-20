package com.cmpay.lemon.monitor.utils.wechatUtil;

import com.cmpay.lemon.framework.cache.jcache.JCacheCacheable;
import com.cmpay.lemon.monitor.utils.wechatUtil.wechat.UrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author: zhou_xiong
 */
@Component
public class BoardcastExecutor {
    private static final String ACCESS_TOKEN = "access_token";
    private UrlData urlData;
    @Value("${monitor.boardcast.agentid}")
    private String agentid;
    @Autowired
    private SendProcessor sendProcessor;
    @Autowired
    private BoardcastDataGenerator generator;
    @JCacheCacheable("wechatUtil")
    public String getAccessToken(String corpid, String corpsecret) {
        urlData = new UrlData();
        urlData.setTokenUrl(corpid, corpsecret);
        Map tokenResult = sendProcessor.exchange(urlData.getTokenUrl(), HttpMethod.GET, null);
        String token = tokenResult.get(ACCESS_TOKEN).toString();
        urlData.setSendMessageUrl(token);
        urlData.setUploadImageUrl(token);
        urlData.setUploadFileUrl(token);
        return token;
    }

    public Map<String, Object> sendMarkdownMessage(String toUser, String contentValue) {
        String body = generator.create(toUser, "markdown", Integer.valueOf(agentid), "content", contentValue);
        return sendProcessor.exchange(urlData.getSendMessageUrl(), HttpMethod.POST, body);
    }


    public String sendUploadMessage(File file) {
        return sendProcessor.uploadTempImage(file, urlData.getUploadImageUrl());
    }
    public String sendUploadFile(File file) {
        return sendProcessor.uploadTempFile(file, urlData.getUploadImageUrl());
    }


    public Map<String, Object> sendImageMessage(String toUser, String contentValue) {
        String body = generator.create(toUser, "image", Integer.valueOf(agentid), "media_id", contentValue);
        return sendProcessor.exchange(urlData.getSendMessageUrl(), HttpMethod.POST, body);
    }
    public Map<String, Object> sendTextMessage(String toUser, String contentValue ) {
        String body = generator.create(toUser, "text", Integer.valueOf(agentid), "content", contentValue);
        return sendProcessor.exchange(urlData.getSendMessageUrl(), HttpMethod.POST, body);
    }

    public Map<String, Object> sendFileMessage(String toUser, String contentValue ) {
        String body = generator.create(toUser, "file", Integer.valueOf(agentid), "media_id", contentValue);
        return sendProcessor.exchange(urlData.getSendMessageUrl(), HttpMethod.POST, body);
    }

}
