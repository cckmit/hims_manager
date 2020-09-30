/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;


public class ProblemRspDTO  extends PageableRspDTO {
    private List<ProblemDTO> problemDTOList ;

    public List<ProblemDTO> getProblemDTOList() {
        return problemDTOList;
    }

    public void setProblemDTOList(List<ProblemDTO> problemDTOList) {
        this.problemDTOList = problemDTOList;
    }

    @Override
    public String toString() {
        return "ProblemRspDTO{" +
                "problemDTOList=" + problemDTOList +
                '}';
    }
}
