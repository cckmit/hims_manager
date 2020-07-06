package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

/**
 * @author TY
 */
public class DefectDetailsRspBO {
    List<DefectDetailsBO> defectDetailsBos;
    PageInfo<DefectDetailsBO> pageInfo;

    public List<DefectDetailsBO> getDefectDetailsBos() {
        return defectDetailsBos;
    }

    public void setDefectDetailsBos(List<DefectDetailsBO> defectDetailsBos) {
        this.defectDetailsBos = defectDetailsBos;
    }

    public PageInfo<DefectDetailsBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DefectDetailsBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "DefectDetailsRspBO{" +
                "defectDetailsBos=" + defectDetailsBos +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
