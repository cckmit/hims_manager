package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class RoleQueryDTO extends GenericRspDTO {
    private List<RoleDTO> list;

    public List<RoleDTO> getList() {
        return list;
    }

    public void setList(List<RoleDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RoleQueryDTO{" +
                "list=" + list +
                '}';
    }
}
