package com.cmpay.lemon.monitor.entity;

/**
 * Created by zouxin on 2018/8/15.
 */
public class ProductionTimeD0 extends AbstractDO {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String desc;
    private String time;

    public ProductionTimeD0(){}

    public ProductionTimeD0(Integer id, String desc, String time) {
        this.id = id;
        this.desc = desc;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
