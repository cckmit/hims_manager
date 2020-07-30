package com.cmpay.lemon.monitor.utils.wechatUtil.schedule;

import com.cmpay.lemon.monitor.utils.wechatUtil.BoardcastExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @author: zhou_xiong
 */
@Component
public class BoardcastScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardcastScheduler.class);
    private static final Byte ENABLE_BOARDCAST = 1;
    @Value("${monitor.boardcast.corpid}")
    private String corpid;
    @Value("${monitor.boardcast.corpsecret}")
    private String corpsecret;
    @Autowired
    private BoardcastExecutor boardcastExecutor;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void pushValidationNotTimelyChecklist(String body , File file){
       boardcastExecutor.getAccessToken(corpid, corpsecret);
        boardcastExecutor.sendTextMessage("@all", body);
        String fileID = boardcastExecutor.sendUploadFile(file);
        boardcastExecutor.sendFileMessage("@all", fileID);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void pushTimeOutWarning(String body){
        boardcastExecutor.getAccessToken(corpid, corpsecret);
        // 投产日期定时变动提醒 未收到则需要注意本周投产时是否转入
        boardcastExecutor.sendTextMessage("@all", body);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void verificationCompleteReminder(String username ,String body){
        boardcastExecutor.getAccessToken(corpid, corpsecret);
        //投产验证变更提醒 发送给对应部门经理
        boardcastExecutor.sendTextMessage(username, body);
    }
}
