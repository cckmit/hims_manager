package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.monitor.bo.MenuBO;
import com.cmpay.lemon.monitor.bo.UserMenuBO;
import com.cmpay.lemon.monitor.bo.UserPermissionBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemMenuService;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/5
 */
@RestController
@RequestMapping(value = MonitorConstants.SYSTEM_MENU_PATH)
public class SystemMenuController {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemMenuService systemMenuService;
    private static final String DEFAULT_USER_ID = "-1";

    /**
     * 导航菜单
     *
     * @return
     */
    @GetMapping("/nav")
    public GenericRspDTO<UserNavDTO> nav(GenericDTO<NoBody> req) {
        Long userNo = Long.valueOf(Optional.ofNullable(SecurityUtils.getLoginUserId()).orElse(DEFAULT_USER_ID));
        UserMenuBO menuBO = new UserMenuBO();
        menuBO.setUserNo(userNo);
        menuBO = systemMenuService.getUserMenus(menuBO);
        List<MenuDTO> menuDTOList = new ArrayList<>();
        menuBO.getMenus().stream().forEach(menu -> {
            MenuDTO menuDTO = new MenuDTO();
            BeanUtils.copyProperties(menuDTO, menu);
            menuDTOList.add(menuDTO);
        });

        UserPermissionBO permissionBO = new UserPermissionBO();
        permissionBO.setUserNo(userNo);
        permissionBO = systemUserService.getUserPermissions(permissionBO);

        UserNavDTO userNavDTO = new UserNavDTO();
        userNavDTO.setMenusList(menuDTOList);
        userNavDTO.setPermissions(permissionBO.getPermissions());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, userNavDTO);
    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    @GetMapping("/list")
    public GenericRspDTO<MenuPageRspDTO> list(GenericDTO<NoBody> req) {
        UserMenuBO menuBO = systemMenuService.getAllMenus();
        List<MenuDTO> menuDTOS = BeanConvertUtils.convertList(menuBO.getMenus(), MenuDTO.class);
        MenuPageRspDTO menuPageRspDTO = new MenuPageRspDTO();
        menuPageRspDTO.setMenuList(menuDTOS);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, menuPageRspDTO);
    }

    /**
     * 菜单下拉框
     *
     * @return
     */
    @GetMapping("/select")
    public GenericRspDTO<MenuSelectRspDTO> select(GenericDTO<NoBody> req) {
        List<MenuBO> menuBOList = systemMenuService.getNotButtonList();
        List<MenuDTO> menuDTOList = BeanConvertUtils.convertList(menuBOList, MenuDTO.class);
        MenuSelectRspDTO menuSelectRspDTO = new MenuSelectRspDTO();
        menuSelectRspDTO.setMenuList(menuDTOList);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, menuSelectRspDTO);
    }

    /**
     * 查询菜单信息
     *
     * @return
     */
    @GetMapping("/info/{menuId}")
    public GenericRspDTO<MenuQueryRspDTO> info(@PathVariable("menuId") Long menuId) {
        MenuBO menuBO = systemMenuService.getMenuById(menuId);
        MenuQueryRspDTO menuQueryRspDTO = new MenuQueryRspDTO();
        BeanUtils.copyProperties(menuQueryRspDTO, menuBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, menuQueryRspDTO);
    }

    /**
     * 新增菜单
     *
     * @param addReqDTO
     * @return
     */
    @PostMapping("/save")
    public GenericRspDTO add(@RequestBody MenuAddReqDTO addReqDTO) {
        MenuBO menuBO = new MenuBO();
        BeanUtils.copyProperties(menuBO, addReqDTO);
        systemMenuService.add(menuBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 更新菜单信息
     *
     * @param menuUpdateReqDTO
     * @return
     */
    @PostMapping("/update")
    public GenericRspDTO update(@RequestBody MenuUpdateReqDTO menuUpdateReqDTO) {
        MenuBO menuBO = new MenuBO();
        BeanUtils.copyProperties(menuBO, menuUpdateReqDTO);
        systemMenuService.update(menuBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除菜单信息
     *
     * @param menuId
     * @return
     */
    @DeleteMapping("/delete/{menuId}")
    public GenericRspDTO delete(@PathVariable("menuId") Long menuId, GenericDTO<NoBody> req) {
        systemMenuService.delete(menuId);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
}
