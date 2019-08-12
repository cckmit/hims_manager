/*
 * @ClassName LogTypeDO
 * @Description
 * @version 1.0
 * @Date 2019-07-03 11:31:38
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class LogTypeDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Long id;
    /**
     * @Fields type 日志类型
     */
    private String type;

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
}
