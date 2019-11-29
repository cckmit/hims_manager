package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.annotation.QueryBody;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.monitor.bo.UserInfoBO;
import com.cmpay.lemon.monitor.bo.UserInfoQueryBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.entity.UserDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemRoleService;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 *
 * @author : 曾益
 * @date : 2018/11/1
 */
@RestController
@RequestMapping(value = MonitorConstants.SYSTEM_USER_PATH)
public class SystemUserController {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemRoleService systemRoleService;

    /**
     * 查询用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public GenericRspDTO<UserInfoDTO> getUserInfo(GenericDTO<NoBody> req) {
        Long userNo = Long.valueOf(SecurityUtils.getLoginUserId());
        return getUserInfo(userNo);
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @GetMapping("/info/{id}")
    public GenericRspDTO<UserInfoDTO> getUserInfoById(@PathVariable("id") String id, GenericDTO<NoBody> req) {
        Long userNo = Long.valueOf(id);
        return getUserInfo(userNo);
    }

    /**
     * 分页查询用户列表
     *
     * @param userInfoQueryReqDTO
     * @return
     */
    @GetMapping("/list")
    public GenericRspDTO<UserInfoQueryRspDTO> getUserInfoPage(@QueryBody UserInfoQueryReqDTO userInfoQueryReqDTO) {
        UserInfoQueryBO userInfoQueryBO = new UserInfoQueryBO();
        BeanUtils.copyProperties(userInfoQueryBO, userInfoQueryReqDTO);
        PageInfo<UserDO> page = systemUserService.findUsers(userInfoQueryBO);
        List<UserInfoDTO> userInfos = BeanConvertUtils.convertList(page.getList(), UserInfoDTO.class);
        UserInfoQueryRspDTO userInfoQueryRspDTO = new UserInfoQueryRspDTO();
        userInfoQueryRspDTO.setList(userInfos);
        userInfoQueryRspDTO.setPageNum(page.getPageNum());
        userInfoQueryRspDTO.setPageSize(page.getPageSize());
        userInfoQueryRspDTO.setPages(page.getPages());
        userInfoQueryRspDTO.setTotal(page.getTotal());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, userInfoQueryRspDTO);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/listAll")
    public GenericRspDTO<UserInfoQueryRspDTO> getUserList(GenericDTO<NoBody> req) {
        UserInfoQueryRspDTO userInfoQueryRspDTO = new UserInfoQueryRspDTO();
        List<UserInfoBO> users = systemUserService.getUserList();
        List<UserInfoDTO> userInfos = BeanConvertUtils.convertList(users, UserInfoDTO.class);
        userInfoQueryRspDTO.setList(userInfos);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, userInfoQueryRspDTO);
    }

    /**
     * 添加用户
     *
     * @param addReqDTO
     * @return
     */
    @PostMapping("/save")
    public GenericRspDTO add(@RequestBody UserAddReqDTO addReqDTO) {
        UserInfoBO userInfoBO = BeanUtils.copyPropertiesReturnDest(new UserInfoBO(), addReqDTO);
        Long userNo = systemUserService.add(userInfoBO);
        systemUserService.addUserRole(userNo, addReqDTO.getRoleIds());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除用户
     *
     * @param deleteReqDTO
     * @return
     */
    @DeleteMapping("/delete")
    public GenericRspDTO delete(@RequestBody UserDeleteReqDTO deleteReqDTO) {
        systemUserService.deleteBatch(deleteReqDTO.getUserNos());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 用户更新
     *
     * @param updateReqDTO
     * @return
     */
    @PostMapping("/update")
    public GenericRspDTO update(@RequestBody UserUpdateReqDTO updateReqDTO) {
        UserInfoBO userInfoBO = BeanUtils.copyPropertiesReturnDest(new UserInfoBO(), updateReqDTO);
        userInfoBO.setDepartment(updateReqDTO.getDepartment());
        userInfoBO.setFullname(updateReqDTO.getFullname());
        systemUserService.update(userInfoBO);
        systemUserService.updateUserRole(userInfoBO.getUserNo(), updateReqDTO.getRoleIds());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 密码更新
     *
     * @param passwordReqDTO
     * @return
     */
    @PostMapping("/password")
    public GenericRspDTO updatePassword(@RequestBody UserPasswordReqDTO passwordReqDTO) {
        systemUserService.updatePassword(passwordReqDTO.getPassword(), passwordReqDTO.getNewPassword());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }


    /**
     * 查询用户信息
     *
     * @param userNo
     * @return
     */
    private GenericRspDTO<UserInfoDTO> getUserInfo(Long userNo) {
        UserInfoBO userInfoBO = new UserInfoBO();
        userInfoBO.setUserNo(userNo);
        userInfoBO = systemUserService.getUserInfo(userInfoBO);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfoDTO, userInfoBO);
        //查询用户归属角色
        List<Long> roleIds = systemRoleService.getRolesByUserNo(userNo);
        userInfoDTO.setRoleIds(roleIds);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, userInfoDTO);
    }

    /**
     * 获得操作者fullname（姓名全称）
     *
     * @param name
     * @return
     */
    @PostMapping("/getFullName")
    public GenericRspDTO<String> getFullName(String name) {
        if(StringUtils.isEmpty(name)){
            name = SecurityUtils.getLoginName();
        }
        String fullName = systemUserService.getFullname(name);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, fullName);
    }
}
