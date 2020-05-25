/*
 * @ClassName DemandPictureDO
 * @Description
 * @version 1.0
 * @Date 2020-05-25 17:03:08
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;


public class DemandPictureBO {
    /**
     * @Fields picId 图片标示与关联表唯一标示关联
     */
    private String picId;
    /**
     * @Fields picName
     */
    private String picName;
    /**
     * @Fields picLocal 文件存储路径
     */
    private String picLocal;

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

    public String getPicLocal() {
        return picLocal;
    }

    public void setPicLocal(String picLocal) {
        this.picLocal = picLocal;
    }
}
