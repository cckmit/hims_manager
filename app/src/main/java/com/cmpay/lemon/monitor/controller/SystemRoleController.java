package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.annotation.QueryBody;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.PageQueryBO;
import com.cmpay.lemon.monitor.bo.RoleBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemRoleService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色服务
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
@RestController
@RequestMapping(MonitorConstants.SYSTEM_ROLE_PATH)
public class SystemRoleController {
    @Autowired
    private SystemRoleService systemRoleService;


    /**
     * 分页查询
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/list")
    public GenericRspDTO<RolePageQueryRspDTO> list(@QueryBody RolePageQueryReqDTO queryDTO) {
        RoleBO roleBO = new RoleBO();
        BeanUtils.copyProperties(roleBO, queryDTO);
        PageQueryBO pageQueryBO = new PageQueryBO();
        BeanUtils.copyProperties(pageQueryBO, queryDTO);
        PageInfo<RoleBO> page = systemRoleService.getRolesPage(roleBO, pageQueryBO);

        RolePageQueryRspDTO rolePageQueryRspDTO = new RolePageQueryRspDTO();
        rolePageQueryRspDTO.setPageNum(page.getPageNum());
        rolePageQueryRspDTO.setPageSize(page.getPageSize());
        rolePageQueryRspDTO.setPages(page.getPages());
        rolePageQueryRspDTO.setTotal(page.getTotal());
        List<RoleDTO> roles = BeanConvertUtils.convertList(page.getList(), RoleDTO.class);
        rolePageQueryRspDTO.setRoles(roles);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rolePageQueryRspDTO);
    }

    /**
     * 获取用户角色
     *
     * @return
     */
    @GetMapping("/select")
    public GenericRspDTO<RoleQueryDTO> getAllRoles(GenericDTO<NoBody> req) {
        List<RoleBO> roleBOList = systemRoleService.getAllRoles();
        List<RoleDTO> roleDTOList = BeanConvertUtils.convertList(roleBOList, RoleDTO.class);
        RoleQueryDTO roleQueryDTO = new RoleQueryDTO();
        roleQueryDTO.setList(roleDTOList);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, roleQueryDTO);
    }

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    @GetMapping("/info/{roleId}")
    public GenericRspDTO<RoleInfoRspDTO> getRoleInfo(@PathVariable("roleId") Long roleId, GenericDTO<NoBody> req) {
        RoleBO roleBO = systemRoleService.getRole(roleId);
        List<Long> menuIds = systemRoleService.getMenuIdsByRoleId(roleId);
        RoleInfoRspDTO roleInfoRspDTO = BeanUtils.copyPropertiesReturnDest(new RoleInfoRspDTO(), roleBO);
        roleInfoRspDTO.setMenuIds(menuIds);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, roleInfoRspDTO);
    }

    /**
     * 新增角色
     *
     * @param addReqDTO
     * @return
     */
    @PostMapping("/save")
    public GenericRspDTO add(@RequestBody RoleAddReqDTO addReqDTO) {
        RoleBO roleBO = new RoleBO();
        BeanUtils.copyProperties(roleBO, addReqDTO);
        Long roleId = systemRoleService.add(roleBO);
        systemRoleService.addRoleMenu(roleId, addReqDTO.getMenuIdList());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 角色修改
     *
     * @param updateReqDTO
     * @return
     */
    @PostMapping("/update")
    public GenericRspDTO update(@RequestBody RoleUpdateReqDTO updateReqDTO) {
        RoleBO roleBO = new RoleBO();
        BeanUtils.copyProperties(roleBO, updateReqDTO);
        systemRoleService.update(roleBO);
        systemRoleService.updateRoleMenu(roleBO.getRoleId(), updateReqDTO.getMenuIdList());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 角色删除
     *
     * @param deleteReqDTO
     * @return
     */
    @DeleteMapping("/delete")
    public GenericRspDTO deleteBatch(@RequestBody RoleDeleteReqDTO deleteReqDTO) {
        systemRoleService.deleteBatch(deleteReqDTO.getRoleIds());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
}
