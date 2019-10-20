package com.cmpay.lemon.monitor.bo;

/**
 * Created by zouxin on 2018/8/15.
 */
public class ProductionTimeBO {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String desc;
    private String time;

    public ProductionTimeBO(){}

    public ProductionTimeBO(Integer id, String desc, String time) {
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

    @Override
    public String toString() {
        return "ProductionTimeBO{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
