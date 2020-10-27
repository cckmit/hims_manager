/*
 * @ClassName OnlineDefectDO
 * @Description
 * @version 1.0
 * @Date 2020-10-19 14:11:34
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class OnlineDefectRspBO {
    List<OnlineDefectBO> onlineDefectBOList;
    PageInfo<OnlineDefectBO> pageInfo;

    public List<OnlineDefectBO> getOnlineDefectBOList() {
        return onlineDefectBOList;
    }

    public void setOnlineDefectBOList(List<OnlineDefectBO> onlineDefectBOList) {
        this.onlineDefectBOList = onlineDefectBOList;
    }

    public PageInfo<OnlineDefectBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<OnlineDefectBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "OnlineDefectRspBO{" +
                "onlineDefectBOList=" + onlineDefectBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
