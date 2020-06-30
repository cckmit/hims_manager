/*
 * @ClassName DemandResourceInvestedDO
 * @Description
 * @version 1.0
 * @Date 2020-06-29 16:18:05
 */
package com.cmpay.lemon.monitor.dto;

public class DemandResourceInvestedReqDTO {
    /**
     * @Fields id
     */
    private Integer id;
    /**
     * @Fields epicKey epickey
     */
    private String epicKey;
    /**
     * @Fields reqNo 需求编号
     */
    private String reqNo;
    /**
     * @Fields value 数值
     */
    private String value;
    /**
     * @Fields valueType 数值类型
     */
    private String valueType;
    /**
     * @Fields department 部门
     */
    private String department;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEpicKey() {
        return epicKey;
    }

    public void setEpicKey(String epicKey) {
        this.epicKey = epicKey;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "DemandResourceInvestedDO{" +
                "id=" + id +
                ", epicKey='" + epicKey + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", value='" + value + '\'' +
                ", valueType='" + valueType + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
