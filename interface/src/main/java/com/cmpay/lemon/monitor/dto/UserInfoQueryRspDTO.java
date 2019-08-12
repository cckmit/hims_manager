package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/8
 */
public class UserInfoQueryRspDTO extends PageableRspDTO {
    private List<UserInfoDTO> list;

    public List<UserInfoDTO> getList() {
        return list;
    }

    public void setList(List<UserInfoDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "UserInfoQueryRspDTO{" +
                "list=" + list +
                '}';
    }
}
