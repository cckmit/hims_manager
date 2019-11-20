package com.cmpay.lemon.monitor.utils.wechatUtil;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.utils.wechatUtil.wechat.WeChatData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhou_xiong
 */
@Component
public class BoardcastDataGenerator {
    private static final String TEXT_TYPE = "text";
    private static final String MARKDOWN_TYPE = "markdown";
    private static final String FILE_TYPE = "file";
    @Autowired
    private ObjectMapper objectMapper;

    public String create(String touser, String msgtype, int agentid, String contentKey, String contentValue) {
        WeChatData weChatData = new WeChatData();
        weChatData.setTouser(touser);
        weChatData.setAgentid(agentid);
        weChatData.setMsgtype(msgtype);
        Map<Object, Object> content = new HashMap<>(10);
        content.put(contentKey, contentValue);
        if (JudgeUtils.equals(msgtype, TEXT_TYPE)) {
            weChatData.setText(content);
            weChatData.setSafe(0);
        } else if (JudgeUtils.equals(msgtype, MARKDOWN_TYPE)) {
            weChatData.setMarkdown(content);
        } else if (JudgeUtils.equals(msgtype, FILE_TYPE)) {
            weChatData.setFile(content);
        }else {
            weChatData.setImage(content);
            weChatData.setSafe(0);
        }
        String data = "";
        try {
            data = objectMapper.writeValueAsString(weChatData);
        } catch (JsonProcessingException ignored) {
        }
        return data;
    }
}
