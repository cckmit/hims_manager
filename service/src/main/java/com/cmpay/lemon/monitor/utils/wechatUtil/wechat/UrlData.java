package com.cmpay.lemon.monitor.utils.wechatUtil.wechat;

/**
 * @author: zhou_xiong
 */
public class UrlData {
    private String corpId;
    private String corpSecret;
    private String tokenUrl;
    private String sendMessageUrl;
    private String uploadImageUrl;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public void setTokenUrl(String corpid, String corpsecret) {
        this.tokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setSendMessageUrl(String accessToken) {
        this.sendMessageUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
    }

    public String getSendMessageUrl() {
        return sendMessageUrl;
    }

    public String getUploadImageUrl() {
        return uploadImageUrl;
    }

    public void setUploadImageUrl(String accessToken) {
        this.uploadImageUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=image";
    }
}
