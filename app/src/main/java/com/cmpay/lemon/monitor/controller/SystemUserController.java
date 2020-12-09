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
        systemUserService.addUserRole(userNo, addReqDTO.getRoleIds(),userInfoBO.getUsername());
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
        systemUserService.updateUserRole(userInfoBO.getUserNo(), updateReqDTO.getRoleIds(),userInfoBO.getUsername());
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

    /**
     * 根据中文名获取手机号
     * @param name
     * @return
     */
    @PostMapping("/getTelbyName")
    public GenericRspDTO<String> getTelbyName(@RequestParam("name")String name) {
        String tel=systemUserService.getTelbyName(name);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, tel);
    }

    /**
     * 根据中文名获取邮箱
     * @param name
     * @return
     */
    @PostMapping("/getEmailbyName")
    public GenericRspDTO<String> getEmailbyName(@RequestParam("name")String name) {
        String tel=systemUserService.getEmailbyName(name);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, tel);
    }

    /**
     * 人员批量导入
     *
     * @return
     */
//    @PostMapping("/batch/import")
//    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
//        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
//        System.err.println(file.getOriginalFilename());
//        File f = null;
//        List<UserInfoBO> demandDOS=new ArrayList<>();
//        try {
//            //MultipartFile转file
//            String originalFilename = file.getOriginalFilename();
//            //获取后缀名
//            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//            if(suffix.equals("xls")){
//                suffix=".xls";
//            }else if(suffix.equals("xlsm")||suffix.equals("xlsx")){
//                suffix=".xlsx";
//            }else {
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件类型错误");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            f=File.createTempFile("tmp", suffix);
//            file.transferTo(f);
//            String filepath = f.getPath();
//            //excel转java类
//            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
//            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
//            for (int i = 1; i <= map.size(); i++) {
//                UserInfoBO demandDO = new UserInfoBO();
//                System.err.println(map.get(i).get(0).toString());
//                //部门
//                demandDO.setDepartment(map.get(i).get(1).toString());
//                System.err.println(map.get(i).get(1).toString());
//                demandDO.setUsername(map.get(i).get(2).toString());
//                System.err.println(map.get(i).get(2).toString());
//                demandDO.setFullname(map.get(i).get(3).toString());
//                System.err.println(map.get(i).get(3).toString());
//                demandDO.setEmail(map.get(i).get(4).toString());
//                System.err.println(map.get(i).get(4).toString());
//                System.err.println(map.get(i).get(5).toString());
//                List<Long> list = new ArrayList<Long>();
//                list.add((long)4001);
//                if("运维部署组".equals(map.get(i).get(5).toString())){
//                    list.add((long)5005);
//                }
//                demandDO.setRoleIds(list);
//                demandDO.setPassword("12345678");
//                demandDO.setStatus((byte)1);
//                System.err.println(map.get(i).get(6).toString());
//                demandDOS.add(demandDO);
//            }
//        } catch (BusinessException e) {
//            e.printStackTrace();
//            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
//        }catch (Exception e) {
//            e.printStackTrace();
//            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
//        }finally {
//            f.delete();
//        }
//
//        List<UserInfoBO> updateList = new ArrayList<>();
//        demandDOS.forEach(m -> {
//            int i = demandDOS.indexOf(m)+2;
//            updateList.add(m);
//        });
////        try {
//        //更新数据库
//        updateList.forEach(m -> {
//            Long userNo = systemUserService.addP(m);
//            systemUserService.addUserRole(userNo, m.getRoleIds());
//        });
////        } catch (Exception e) {
////            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
////            MsgEnum.ERROR_CUSTOM.setMsgInfo("批量新增错误码失败");
////            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
////        }
//        //errorService.doBatchImport(file);
//        return GenericRspDTO.newSuccessInstance();
//    }
}
