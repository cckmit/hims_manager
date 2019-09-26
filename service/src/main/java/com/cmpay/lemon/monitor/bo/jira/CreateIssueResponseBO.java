package com.cmpay.lemon.monitor.bo.jira;

/**
 * Copyright 2019 bejson.com
 */


/**
 * Auto-generated: 2019-09-02 11:37:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CreateIssueResponseBO {

    private String id;
    private String key;
    private String self;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }

    public void setSelf(String self) {
        this.self = self;
    }
    public String getSelf() {
        return self;
    }

    @Override
    public String toString() {
        return "CreateIssueResponse{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", self='" + self + '\'' +
                '}';
    }
}