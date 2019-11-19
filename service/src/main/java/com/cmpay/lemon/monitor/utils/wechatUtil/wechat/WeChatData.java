package com.cmpay.lemon.monitor.utils.wechatUtil.wechat;

/**
 * @author: zhou_xiong
 */
public class WeChatData {
    private String touser;
    private String msgtype;
    private int agentid;
    private Object text;
    private Object markdown;
    private Object image;
    /**
     * 表示是否是保密消息，0表示否，1表示是，默认0
     */
    private int safe;

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public Object getMarkdown() {
        return markdown;
    }

    public void setMarkdown(Object markdown) {
        this.markdown = markdown;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }
}
