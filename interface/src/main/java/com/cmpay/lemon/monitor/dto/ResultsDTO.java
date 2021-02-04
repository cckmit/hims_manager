package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.util.Date;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ResultsDTO extends GenericDTO {
    private VerificationResultsFeedbackDTO verificationResultsFeedbackBO ;
    private ProblemDTO problemBO;
    private List<ProductionFollowDTO> followBOList ;

    public VerificationResultsFeedbackDTO getVerificationResultsFeedbackBO() {
        return verificationResultsFeedbackBO;
    }

    public void setVerificationResultsFeedbackBO(VerificationResultsFeedbackDTO verificationResultsFeedbackBO) {
        this.verificationResultsFeedbackBO = verificationResultsFeedbackBO;
    }

    public ProblemDTO getProblemBO() {
        return problemBO;
    }

    public void setProblemBO(ProblemDTO problemBO) {
        this.problemBO = problemBO;
    }

    public List<ProductionFollowDTO> getFollowBOList() {
        return followBOList;
    }

    public void setFollowBOList(List<ProductionFollowDTO> followBOList) {
        this.followBOList = followBOList;
    }
}
