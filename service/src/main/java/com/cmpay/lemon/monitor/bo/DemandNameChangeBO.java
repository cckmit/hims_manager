/*
 * @ClassName DemandNameChangeDO
 * @Description
 * @version 1.0
 * @Date 2020-06-03 10:21:06
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

public class DemandNameChangeBO   {
    /**
     * @Fields id id自增
     */
    private Integer id;
    /**
     * @Fields newReqInnerSeq 现内部需求编号
     */
    private String newReqInnerSeq;
    /**
     * @Fields newReqNo 现需求编号
     */
    private String newReqNo;
    /**
     * @Fields newReqNm 现需求名称
     */
    private String newReqNm;
    /**
     * @Fields oldReqInnerSeq 原内部需求编号
     */
    private String oldReqInnerSeq;
    /**
     * @Fields oldReqNo 原需求名称
     */
    private String oldReqNo;
    /**
     * @Fields oldReqNm 原需求名称
     */
    private String oldReqNm;
    /**
     * @Fields operator 操作人
     */
    private String operator;
    /**
     * @Fields operationTime 操作时间
     */
    private String operationTime;
    /**
     * @Fields uuid 同一需求唯一标识
     */
    private String uuid;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewReqInnerSeq() {
        return newReqInnerSeq;
    }

    public void setNewReqInnerSeq(String newReqInnerSeq) {
        this.newReqInnerSeq = newReqInnerSeq;
    }

    public String getNewReqNo() {
        return newReqNo;
    }

    public void setNewReqNo(String newReqNo) {
        this.newReqNo = newReqNo;
    }

    public String getNewReqNm() {
        return newReqNm;
    }

    public void setNewReqNm(String newReqNm) {
        this.newReqNm = newReqNm;
    }

    public String getOldReqInnerSeq() {
        return oldReqInnerSeq;
    }

    public void setOldReqInnerSeq(String oldReqInnerSeq) {
        this.oldReqInnerSeq = oldReqInnerSeq;
    }

    public String getOldReqNo() {
        return oldReqNo;
    }

    public void setOldReqNo(String oldReqNo) {
        this.oldReqNo = oldReqNo;
    }

    public String getOldReqNm() {
        return oldReqNm;
    }

    public void setOldReqNm(String oldReqNm) {
        this.oldReqNm = oldReqNm;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "DemandNameChangeBO{" +
                "id=" + id +
                ", newReqInnerSeq='" + newReqInnerSeq + '\'' +
                ", newReqNo='" + newReqNo + '\'' +
                ", newReqNm='" + newReqNm + '\'' +
                ", oldReqInnerSeq='" + oldReqInnerSeq + '\'' +
                ", oldReqNo='" + oldReqNo + '\'' +
                ", oldReqNm='" + oldReqNm + '\'' +
                ", operator='" + operator + '\'' +
                ", operationTime='" + operationTime + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
