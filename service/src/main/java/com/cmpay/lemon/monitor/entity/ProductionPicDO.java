/*
 * @ClassName productionPicDO
 * @Description 
 * @version 1.0
 * @Date 2019-10-20 10:04:37
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProductionPicDO extends BaseDO {




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

    public ProductionPicDO() {
    }

    public ProductionPicDO(String picId, String picName, String picLocal) {
        this.picId = picId;
        this.picName = picName;
        this.picLocal = picLocal;
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