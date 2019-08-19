package com.cmpay.lemon.monitor.bo;

import java.util.Date;

/**
 * Created on 2018/12/17
 *
 * @author: ou_yn
 */
public class SnapshotLogBO {

    /**
     * @Fields id
     */
    private String id;
    /**
     * @Fields title
     */
    private String title;
    /**
     * @Fields createBy
     */
    private String createBy;
    /**
     * @Fields createDate
     */
    private Date createDate;
    /**
     * @Fields remoteAddr
     */
    private String remoteAddr;
    /**
     * @Fields requestUri
     */
    private String requestUri;
    /**
     * @Fields params
     */
    private String params;
    /**
     * @Fields html
     */
    private String html;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }


    @Override
    public String toString() {
        return "SnapshotLogBO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate='" + createDate + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", params='" + params + '\'' +
                ", html='" + html + '\'' +
                '}';
    }
}
