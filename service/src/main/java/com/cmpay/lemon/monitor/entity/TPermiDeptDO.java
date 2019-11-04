/*
 * @ClassName TPermiDeptDO
 * @Description 
 * @version 1.0
 * @Date 2019-11-01 16:28:33
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.time.LocalDateTime;

@DataObject
public class TPermiDeptDO extends BaseDO {
    /**
     * @Fields seqId 
     */
    private Long seqId;
    /**
     * @Fields deptId 
     */
    private String deptId;
    /**
     * @Fields deptName 
     */
    private String deptName;
    /**
     * @Fields parentDeptId 
     */
    private String parentDeptId;
    /**
     * @Fields createTime 
     */
    private LocalDateTime createTime;
    /**
     * @Fields updateTime 
     */
    private LocalDateTime updateTime;
    /**
     * @Fields createUserId 
     */
    private String createUserId;
    /**
     * @Fields updateUserId 
     */
    private String updateUserId;
    /**
     * @Fields sort 
     */
    private Long sort;
    /**
     * @Fields flag 01：生效
            02：未生效
     */
    private String flag;
    /**
     * @Fields thirdDeptId 
     */
    private String thirdDeptId;
    /**
     * @Fields deptManagerId 
     */
    private String deptManagerId;
    /**
     * @Fields deptManagerName 
     */
    private String deptManagerName;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getParentDeptId() {
        return parentDeptId;
    }

    public void setParentDeptId(String parentDeptId) {
        this.parentDeptId = parentDeptId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getThirdDeptId() {
        return thirdDeptId;
    }

    public void setThirdDeptId(String thirdDeptId) {
        this.thirdDeptId = thirdDeptId;
    }

    public String getDeptManagerId() {
        return deptManagerId;
    }

    public void setDeptManagerId(String deptManagerId) {
        this.deptManagerId = deptManagerId;
    }

    public String getDeptManagerName() {
        return deptManagerName;
    }

    public void setDeptManagerName(String deptManagerName) {
        this.deptManagerName = deptManagerName;
    }
}