package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.framework.data.NoBody;
import io.swagger.annotations.ApiModel;

/**
 * 用户登录返回数据
 * @author : 曾益
 * @date : 2018/10/31
 */
@ApiModel(value = "UserLoginRspDTO", description = "用户登录返回数据")
public class UserLoginRspDTO extends GenericRspDTO<NoBody> {
}
