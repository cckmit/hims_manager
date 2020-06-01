/*
 * @ClassName DemandPictureDO
 * @Description
 * @version 1.0
 * @Date 2020-05-27 15:28:00
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class DemandPictureDO extends BaseDO {
    /**
     * @Fields picId 图片标示与关联表唯一标示关联
     */
    private Integer picId;
    /**
     * @Fields picName
     */
    private String picName;
    /**
     * @Fields picLocal 文件存储路径
     */
    private String picLocal;
    /**
     * @Fields picUser 创建者
     */
    private String picUser;
    /**
     * @Fields picTime 创建时间
     */
    private String picTime;
    /**
     * @Fields picMoth 需求实施月份
     */
    private String picMoth;
    /**
     * @Fields picReqinnerseq 内部需求编号
     */
    private String picReqinnerseq;
    /**
     * @Fields picReqno 需求编号
     */
    private String picReqno;
    /**
     * @Fields picReqnm 需求名称
     */
    private String picReqnm;

    public Integer getPicId() {
        return picId;
    }

    public void setPicId(Integer picId) {
        this.picId = picId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicLocal() {
        return picLocal;
    }

    public void setPicLocal(String picLocal) {
        this.picLocal = picLocal;
    }

    public String getPicUser() {
        return picUser;
    }

    public void setPicUser(String picUser) {
        this.picUser = picUser;
    }

    public String getPicTime() {
        return picTime;
    }

    public void setPicTime(String picTime) {
        this.picTime = picTime;
    }

    public String getPicMoth() {
        return picMoth;
    }

    public void setPicMoth(String picMoth) {
        this.picMoth = picMoth;
    }

    public String getPicReqinnerseq() {
        return picReqinnerseq;
    }

    public void setPicReqinnerseq(String picReqinnerseq) {
        this.picReqinnerseq = picReqinnerseq;
    }

    public String getPicReqno() {
        return picReqno;
    }

    public void setPicReqno(String picReqno) {
        this.picReqno = picReqno;
    }

    public String getPicReqnm() {
        return picReqnm;
    }

    public void setPicReqnm(String picReqnm) {
        this.picReqnm = picReqnm;
    }

    @Override
    public String toString() {
        return "DemandPictureDO{" +
                "picId=" + picId +
                ", picName='" + picName + '\'' +
                ", picLocal='" + picLocal + '\'' +
                ", picUser='" + picUser + '\'' +
                ", picTime='" + picTime + '\'' +
                ", picMoth='" + picMoth + '\'' +
                ", picReqinnerseq='" + picReqinnerseq + '\'' +
                ", picReqno='" + picReqno + '\'' +
                ", picReqnm='" + picReqnm + '\'' +
                '}';
    }
}
