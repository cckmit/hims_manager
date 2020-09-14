/*
 * @ClassName ProUnhandledIssuesDO
 * @Description
 * @version 1.0
 * @Date 2020-07-03 17:03:49
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.List;

public class ProUnhandledIssuesRspDTO extends GenericRspDTO {
   List<ProUnhandledIssuesDTO> proUnhandledIssuesDTOList ;

    public List<ProUnhandledIssuesDTO> getProUnhandledIssuesDTOList() {
        return proUnhandledIssuesDTOList;
    }

    public void setProUnhandledIssuesDTOList(List<ProUnhandledIssuesDTO> proUnhandledIssuesDTOList) {
        this.proUnhandledIssuesDTOList = proUnhandledIssuesDTOList;
    }

    @Override
    public String toString() {
        return "ProUnhandledIssuesRspDTO{" +
                "proUnhandledIssuesDTOList=" + proUnhandledIssuesDTOList +
                '}';
    }
}
