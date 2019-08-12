/*
 * @ClassName LogType
 * @Description
 * @version 1.0
 * @Date 2019-01-16 16:31:48
 */
package com.cmpay.lemon.monitor.bo;


import java.util.List;

public class LogTypeBO {
    /**
     * @Fields id 主键
     */
    private Long id;
    /**
     * @Fields type 日志类型
     */
    private String type;

    private List<LogTypeBO> logTypeBOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LogTypeBO> getLogTypeBOList() {
        return logTypeBOList;
    }

    public void setLogTypeBOList(List<LogTypeBO> logTypeBOList) {
        this.logTypeBOList = logTypeBOList;
    }
}
