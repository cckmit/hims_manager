/*
 * @ClassName LogType
 * @Description
 * @version 1.0
 * @Date 2019-01-16 16:31:48
 */
package com.cmpay.lemon.monitor.dto;


public class DictionaryDTO {
    private String seq_id;
    private String name;
    private String value;
    private String jp;
    private String qp;
    private String dic_id;
    private String remark;
    private String sort;

    public DictionaryDTO() {
    }

    public String getSeq_id() {
        return seq_id;
    }

    public void setSeq_id(String seq_id) {
        this.seq_id = seq_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public String getQp() {
        return qp;
    }

    public void setQp(String qp) {
        this.qp = qp;
    }

    public String getDic_id() {
        return dic_id;
    }

    public void setDic_id(String dic_id) {
        this.dic_id = dic_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "DictionaryDO{" +
                "seq_id='" + seq_id + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", jp='" + jp + '\'' +
                ", qp='" + qp + '\'' +
                ", dic_id='" + dic_id + '\'' +
                ", remark='" + remark + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
