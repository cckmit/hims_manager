package com.cmpay.lemon.monitor.dto;

import java.util.List;

public class GetQuestionRsqDTO {

    List<ProblemDTO> problemDTO;

    public List<ProblemDTO> getProblemDTO() {
        return problemDTO;
    }

    public void setProblemDTO(List<ProblemDTO> problemDTO) {
        this.problemDTO = problemDTO;
    }
}
