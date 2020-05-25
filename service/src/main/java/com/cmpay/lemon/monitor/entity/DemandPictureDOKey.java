/*
 * @ClassName DemandPictureDOKey
 * @Description 
 * @version 1.0
 * @Date 2020-05-25 17:03:08
 */
package com.cmpay.lemon.monitor.entity;

public class DemandPictureDOKey {
    /**
     * @Fields picId 图片标示与关联表唯一标示关联
     */
    private String picId;
    /**
     * @Fields picName 
     */
    private String picName;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }
}