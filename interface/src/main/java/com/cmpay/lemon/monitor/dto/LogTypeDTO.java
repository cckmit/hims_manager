/*
 * @ClassName LogType
 * @Description
 * @version 1.0
 * @Date 2019-01-16 16:31:48
 */
package com.cmpay.lemon.monitor.dto;


public class LogTypeDTO {
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

    @Override
    public String toString() {
        return "LogTypeDTO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
