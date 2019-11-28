package com.cmpay.lemon.monitor.service.impl.error;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.datasource.TargetDataSource;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.dao.IErcdmgErorDao;
import com.cmpay.lemon.monitor.dao.IErcdmgUpdmgnDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.SendErrorEmail;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.CreateSequence;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 需求计划
 * @author: ty
 */
@Service
public class ErrorServiceImpl implements ErrorService {
    //超级管理员
    private static final Long SUPERADMINISTRATOR =(long)10506;
    //团队主管
    private static final Long SUPERADMINISTRATOR1 =(long)5004;
    //运维部署组
    private static final Long SUPERADMINISTRATOR2 =(long)5005;

    @Autowired
    private IErcdmgErorDao iErcdmgErorDao;
    @Autowired
    private IUserRoleExtDao userRoleExtDao;
    @Autowired
    private IErcdmgUpdmgnDao iErcdmgUpdmgnDao;
    @Autowired
    SystemUserService userService;

    @Override
    public ErcdmgErrorComditionRspBO searchErroeList (ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        PageInfo<ErcdmgErrorComditionBO> pageInfo = getPageInfo(ercdmgErrorComditionBO);
        List<ErcdmgErrorComditionBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), ErcdmgErrorComditionBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgErrorComditionBOList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return  productionRspBO;
    }
    private PageInfo<ErcdmgErrorComditionBO> getPageInfo(ErcdmgErrorComditionBO demandBO) {
        ErcdmgErrorComditionDO demandDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        System.err.println(demandDO);
        System.err.println(demandBO);
        PageInfo<ErcdmgErrorComditionBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iErcdmgErorDao.findErcdmgErrorList(demandDO), ErcdmgErrorComditionBO.class));
        return pageInfo;
    }
    @Override
    public ErcdmgErrorComditionBO checkErrorCodeExist(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        ErcdmgErrorComditionDO ercdmgErrorbean = iErcdmgErorDao.checkErrorCodeExist(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        if (ercdmgErrorbean != null) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"已存在，请重新输入后再试");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 获取当前操作人信息
        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
        // 生成id
        ercdmgErrorComditionBO.setId(CreateSequence.getSequence());
        ercdmgErrorComditionBO.setCurtState("1");
        ercdmgErrorComditionBO.setCreateUserId(SecurityUtils.getLoginName());//技术负责人id
        if(ercdmgErrorComditionBO.getTechUserName()!=null){
            ercdmgErrorComditionBO.setTechUserId(SecurityUtils.getLoginName());//技术负责人id
            ercdmgErrorComditionBO.setTechUserName(ercdmgErrorComditionBO.getTechUserName());//技术负责人名称
        }else{
            ercdmgErrorComditionBO.setTechUserId(SecurityUtils.getLoginName());//技术负责人id
            ercdmgErrorComditionBO.setTechUserName(currentUser);//技术负责人名称
        }
        return ercdmgErrorComditionBO;
    }
    /**
     * 新增错误码
     * @param ercdmgErrorComditionBO 错误码对象
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void addError(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
//        ErcdmgErrorComditionDO ercdmgErrorbean = iErcdmgErorDao.checkErrorCodeExist(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
//        if (ercdmgErrorbean != null) {
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"已存在，请重新输入后再试");
//            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//        }
//        // 获取当前操作人信息
//        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
//        // 生成id
//        ercdmgErrorComditionBO.setId(CreateSequence.getSequence());
//        ercdmgErrorComditionBO.setCurtState("1");
//        ercdmgErrorComditionBO.setCreateUserId(SecurityUtils.getLoginName());//技术负责人id
//        if(ercdmgErrorComditionBO.getTechUserName()!=null){
//            ercdmgErrorComditionBO.setTechUserId(SecurityUtils.getLoginName());//技术负责人id
//            ercdmgErrorComditionBO.setTechUserName(ercdmgErrorComditionBO.getTechUserName());//技术负责人名称
//        }else{
//            ercdmgErrorComditionBO.setTechUserId(SecurityUtils.getLoginName());//技术负责人id
//            ercdmgErrorComditionBO.setTechUserName(currentUser);//技术负责人名称
//        }
//        // 查询sit错误码数据库并新增
//         String sit = selectSitMsg(ercdmgErrorComditionBO);
//         System.err.println(sit);
////        // 查询uat错误码并新增
////        String uat = selectUatMsg(ercdmgErrorComditionBO);
//        // 新增考核错误码
//        addErcdmgError(ercdmgErrorComditionBO);
//        // 记录错误码导入记录
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
//        ErrorRecordBeanDO errorRecordBean = new ErrorRecordBeanDO();
//        errorRecordBean.setErrorCode(ercdmgErrorComditionBO.getErrorCd());
//        if("SIT" == sit){
//            errorRecordBean.setTimeStmp(df.format(new Date()));
//            errorRecordBean.setEnvirCode("SIT");
//            iErcdmgErorDao.insertErrorRecordBean(errorRecordBean);
//        }
////        if("UAT" == uat){
////            errorRecordBean.setTimeStmp(df.format(new Date()));
////            errorRecordBean.setEnvirCode("UAT");
////            iErcdmgErorDao.insertErrorRecordBean(errorRecordBean);
////        }
    }
    @Override
    public void insertErrorRecordBean(ErrorRecordBeanDO errorRecordBean){
        iErcdmgErorDao.insertErrorRecordBean(errorRecordBean);
    }
    // 判断是否为角色权限
    public boolean isDepartmentManager(Long juese ){
        //查询该操作员角色
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(juese);
        userRoleDO.setUserNo(Long.parseLong(SecurityUtils.getLoginUserId()));
        List<UserRoleDO> userRoleDOS =new LinkedList<>();
        userRoleDOS  = userRoleExtDao.find(userRoleDO);
        if (!userRoleDOS.isEmpty()){
            return true ;
        }
        return false ;
    }
    @Override
    public void addErcdmgError(ErcdmgErrorComditionBO ercdmgError) {
        if(ercdmgError.getErrorCd()==null){
            //throw new ServiceException("你输入的错误码为空");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(ercdmgError.getErrorCd().length()>10){
            //throw new ServiceException("错误码长度不超过10位");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("错误码长度不超过10位");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

        ercdmgError.setEntryDate(LocalDateTime.now());
        ercdmgError.setCreateDatetime(LocalDateTime.now());
        ercdmgError.setAudiRoleName("审核人员");//默认审核人员角色
        ercdmgError.setErrorState("正常");
//        ercdmgError.setProdUserId(tPermiUser.getUserId());
        ercdmgError.setProdUserId( SecurityUtils.getLoginName());

        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgError);
        iErcdmgErorDao.insert(errorComditionDO);

    }
    /**
     * 查询sit错误码
     */
    @Override
    @TargetDataSource("dsSit")
    public String selectSitMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        Date ss = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        errorComditionDO.setUpdDt(df1.format(ss));
        errorComditionDO.setTmSmp(df2.format(ss));
        System.err.println(errorComditionDO.getUpdDt());
        System.err.println(errorComditionDO.getTmSmp());
        String sitmsg_cd= iErcdmgErorDao.selectPubtmsg(ercdmgErrorComditionBO.getErrorCd());
        String sittsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        System.err.println("查询sit"+sitmsg_cd);
        System.err.println("查询sit"+sittsg_cd);
        if(sittsg_cd!=null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在SIT数控库pubttms表中已存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(sitmsg_cd==null){
            iErcdmgErorDao.insertPubtmsg(errorComditionDO);
        }
        iErcdmgErorDao.insertPubttms(errorComditionDO);
        try {
            String sit_url_str = "http://172.16.49.25:8002/v1/user/message/redis?errorCode=" + ercdmgErrorComditionBO.getErrorCd();
            URL sit_url = new URL(sit_url_str);
            //得到connection对象。
            HttpURLConnection sit_connection = (HttpURLConnection) sit_url.openConnection();
            //设置请求方式
            sit_connection.setRequestMethod("GET");
            //连接
            sit_connection.connect();
            int sit_responseCode = sit_connection.getResponseCode();
            System.err.println(sit_responseCode);
            if(sit_responseCode != HttpURLConnection.HTTP_OK){
                System.err.println("SITTTTTTTTTTTTTTTTTT");
                return "SIT";
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("操作SIT数控库失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        return "";
    }
    /**
     * 查询Uat错误码
     */
    @Override
    @TargetDataSource("dsUat")
    public String selectUatMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        Date ss = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        errorComditionDO.setUpdDt(df1.format(ss));
        errorComditionDO.setTmSmp(df2.format(ss));
        String uatmsg_cd= iErcdmgErorDao.selectPubtmsg(ercdmgErrorComditionBO.getErrorCd());
        String uattsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        System.err.println("uat:"+uatmsg_cd+""+uattsg_cd);
        if(uattsg_cd!=null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在UAT数控库pubttms表中已存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(uatmsg_cd==null){
            iErcdmgErorDao.insertPubtmsg(errorComditionDO);
        }
        iErcdmgErorDao.insertPubttms(errorComditionDO);
        try {
            String uat_url_str = "http://172.16.48.170:8002/v1/user/message/redis?errorCode=" + ercdmgErrorComditionBO.getErrorCd();

            URL uat_url = new URL(uat_url_str);
            //得到connection对象。
            HttpURLConnection uat_connection = (HttpURLConnection) uat_url.openConnection();
            //设置请求方式
            uat_connection.setRequestMethod("GET");
            //连接
            uat_connection.connect();
            int uat_responseCode = uat_connection.getResponseCode();
            if(uat_responseCode != HttpURLConnection.HTTP_OK){
                return "UAT";
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("操作UAT数控库失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        return "";
    }
    @Override
    public void checkErrorUP(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        ErcdmgErrorComditionDO ercdmgError = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(ercdmgError, ercdmgErrorComditionBO);
        ErcdmgErrorComditionDO errorSingle=iErcdmgErorDao.selectErrorSingle(ercdmgErrorComditionBO.getId());
        if (errorSingle==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("输入错误码"+ercdmgError.getErrorCd()+"不存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }
    /**
     * 修改错误码
     * @param ercdmgErrorComditionBO 错误码对象
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateError(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
//        ErcdmgErrorComditionDO ercdmgError = new ErcdmgErrorComditionDO();
//        BeanConvertUtils.convert(ercdmgError, ercdmgErrorComditionBO);
//        ErcdmgErrorComditionDO errorSingle=iErcdmgErorDao.selectErrorSingle(ercdmgErrorComditionBO.getId());
//        if (errorSingle==null){
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("输入错误码"+ercdmgError.getErrorCd()+"不存在");
//            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//        }
//        updateSitMsg(ercdmgErrorComditionBO);
//        updateUatMsg(ercdmgErrorComditionBO);
//        updateErcdmgError(ercdmgErrorComditionBO);
    }
    /**
     * 修改sit数据库
     * @param ercdmgErrorComditionBO
     * @return
     */
    @Override
    @TargetDataSource("dsSit")
    public void updateSitMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        Date ss = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        errorComditionDO.setUpdDt(df1.format(ss));
        errorComditionDO.setTmSmp(df2.format(ss));
        System.err.println(errorComditionDO.getUpdDt());
        System.err.println(errorComditionDO.getTmSmp());
        String sitmsg_cd= iErcdmgErorDao.selectPubtmsg(ercdmgErrorComditionBO.getErrorCd());
        String sittsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        System.err.println("查询sit"+sitmsg_cd);
        System.err.println("查询sit"+sittsg_cd);
        if(sittsg_cd==null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在SIT数控库pubttms表中不存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(sitmsg_cd==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在SIT数控库pubttms表中不存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        try {
            String sit_url_str = "http://172.16.49.25:8002/v1/user/message/redis?errorCode=" + ercdmgErrorComditionBO.getErrorCd();
            URL sit_url = new URL(sit_url_str);
            HttpURLConnection sit_connection = (HttpURLConnection)sit_url.openConnection();
            sit_connection.setRequestMethod("GET");
            sit_connection.connect();
            int sit_responseCode = sit_connection.getResponseCode();
            if (sit_responseCode != 200) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"SIT更新在SIT数控库pubttms表中不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("操作SIT数控库失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        try {
            iErcdmgErorDao.updatePubtmsg(errorComditionDO);
            iErcdmgErorDao.updatePubttms(errorComditionDO);
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("错误码修改SIT数控库失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }
    /**
     * 修改uat数据库
     * @param ercdmgErrorComditionBO
     * @return
     */
    @Override
    @TargetDataSource("dsUat")
    public void updateUatMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        Date ss = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        errorComditionDO.setUpdDt(df1.format(ss));
        errorComditionDO.setTmSmp(df2.format(ss));
        String uatmsg_cd= iErcdmgErorDao.selectPubtmsg(ercdmgErrorComditionBO.getErrorCd());
        String uattsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        if(uattsg_cd==null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在UAT数控库pubttms表中已存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(uatmsg_cd==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在UAT数控库pubttms表中不存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        try {
            String uat_url_str = "http://172.16.48.170:8002/v1/user/message/redis?errorCode=" + ercdmgErrorComditionBO.getErrorCd();
            URL uat_url = new URL(uat_url_str);
            HttpURLConnection uat_connection = (HttpURLConnection)uat_url.openConnection();
            uat_connection.setRequestMethod("GET");
            uat_connection.connect();
            int uat_responseCode = uat_connection.getResponseCode();
            if (uat_responseCode != 200) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"UAT更新在UAT数控库pubttms表中不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("操作UAT数控库失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        try {
            iErcdmgErorDao.updatePubtmsg(errorComditionDO);
            iErcdmgErorDao.updatePubttms(errorComditionDO);
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("错误码修改UAT数控库失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }
    @Override
    public void updateErcdmgError(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        ErcdmgErrorComditionDO ercdmgError = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(ercdmgError, ercdmgErrorComditionBO);
        if(ercdmgError.getErrorCd()==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("错误码编号为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(ercdmgError.getProdUserName()==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请输入产品经理名称");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(ercdmgError.getProdMod().length()>3){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("产品模块长度不超过3位");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        //检查输入产品经理是否存在
//        TPermiUser tPermiUser=ercdmgErrorMapper.findByUsername(ercdmgError.getProdUserName());
//        if(tPermiUser==null){
//            throw new ServiceException("你输入的产品经理名称不存在，请重新输入后再试");
//        }
//        //检查用户角色
//        String roleName=ercdmgErrorMapper.searchUserRoleProd(tPermiUser.getUserId());
//
//        if (!ErrerConstant.PORD.equals(roleName)||roleName==null){
//            throw new ServiceException("你输入的用户"+tPermiUser.getUserName()+"非产品经理角色，请重新输入后再试");
//        }
        ercdmgError.setProdUserId( SecurityUtils.getLoginName());
        ercdmgError.setLastUpdateDate(LocalDateTime.now());
        iErcdmgErorDao.update(ercdmgError);
    }
    @Override
    public ErcdmgErrorComditionBO checkErrorDelete(String id){
        ErcdmgErrorComditionDO errorSingle=iErcdmgErorDao.selectErrorSingle(id);
        if(errorSingle!=null){
            if(!SecurityUtils.getLoginName().equals(errorSingle.getCreateUserId())){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("只能删除自己新增的数据");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if(errorSingle.getCurtState().equals("5")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("不能删除已生效数据");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        ErcdmgErrorComditionBO errorComditionBO = new ErcdmgErrorComditionBO();
        BeanConvertUtils.convert(errorComditionBO, errorSingle);
        return errorComditionBO;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void deleteError(String id){
//        ErcdmgErrorComditionDO errorSingle=iErcdmgErorDao.selectErrorSingle(id);
//        if(errorSingle!=null){
//            if(!SecurityUtils.getLoginName().equals(errorSingle.getCreateUserId())){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("只能删除自己新增的数据");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            if(errorSingle.getCurtState().equals("5")){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("不能删除已生效数据");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//        }
//        //删除sit
//        deleteSMsg(errorSingle);
//        //删除uat
//        deleteUatMsg(errorSingle);
//        // 删除考核数据库
//        delErcdmgError(id,errorSingle);
    }
    /**
     * 删除sit数据库
     * @param errorSingle
     * @return
     */
    @Override
    @TargetDataSource("dsSit")
    public void deleteSMsg(ErcdmgErrorComditionBO errorSingle) {
        try {
            iErcdmgErorDao.deletePubtmsg(errorSingle.getErrorCd());
            iErcdmgErorDao.deletePubttms(errorSingle.getErrorCd(),errorSingle.getBuscnl());
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("删除SIT数控库错误码失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

    }
    /**
     * 删除uat数据库
     * @param errorSingle
     * @return
     */
    @Override
    @TargetDataSource("dsUat")
    public void deleteUatMsg(ErcdmgErrorComditionBO errorSingle) {
        try {
            iErcdmgErorDao.deletePubtmsg(errorSingle.getErrorCd());
            iErcdmgErorDao.deletePubttms(errorSingle.getErrorCd(),errorSingle.getBuscnl());
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("删除UAT数控库错误码失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }

    /**
     * 单个删除错误码
     */
    @Override
    public void delErcdmgError(String id,ErcdmgErrorComditionBO e)  {
        // 备份至错误码历史表
        searchBackErrorCd(id);
        //查询是否有更新批次信息
        String updateNo=iErcdmgErorDao.selectUpdateErrorCd(id);
        System.out.println(updateNo);
        //有则更新管理表数量减1
        if(updateNo !=null&& !"".equals(updateNo)){
            String count= iErcdmgUpdmgnDao.selectUpdateCount(updateNo);
            int sun=Integer.parseInt(count);
            if(sun>0){
                String countStr=Integer.toString(sun-1);
                Map<String, Object> map = new HashMap<String,Object>();
                map.put("countStr", countStr);
                map.put("updateNo",updateNo);
                iErcdmgUpdmgnDao.updateUpdmgnCount(map);
            }
        }
        iErcdmgErorDao.deleteError(id);
    }

    public void searchBackErrorCd(String errorCd) {
        ErcdmgErrorComditionDO ercdmgError=iErcdmgErorDao.selectErrorSingle(errorCd);
        if(ercdmgError.getCurtState()=="5"){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("错误码状态为：已生效的不能删除");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        ercdmgError.setErrorState("被删除");
        iErcdmgErorDao.insertErrorlg(ercdmgError);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchImport(MultipartFile file) {
        File f = null;
        List<ErcdmgErrorComditionBO> demandDOS=new ArrayList<>();
        try {
            //MultipartFile转file
            String originalFilename = file.getOriginalFilename();
            //获取后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if(suffix.equals("xls")){
                suffix=".xls";
            }else if(suffix.equals("xlsm")||suffix.equals("xlsx")){
                suffix=".xlsx";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件类型错误");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            f=File.createTempFile("tmp", suffix);
            file.transferTo(f);
            String filepath = f.getPath();
            //excel转java类
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
                ErcdmgErrorComditionBO demandDO = new ErcdmgErrorComditionBO();
                demandDO.setCr(map.get(i).get(0).toString());
                demandDO.setErrorCd(map.get(i).get(1).toString());
                demandDO.setProdMod(map.get(i).get(2).toString());
                demandDO.setBuscnl(map.get(i).get(3).toString());
                demandDO.setBusnTip(map.get(i).get(4).toString());
                demandDO.setTechTip(map.get(i).get(5).toString());
                demandDO.setAppScen(map.get(i).get(6).toString());
                demandDO.setProdUserName(map.get(i).get(7).toString());
                demandDOS.add(demandDO);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }finally {
            f.delete();
        }

        List<ErcdmgErrorComditionBO> updateList = new ArrayList<>();
        demandDOS.forEach(m -> {
            int i = demandDOS.indexOf(m)+2;
            updateList.add(m);
        });
        try {
             //更新数据库
            updateList.forEach(m -> {
                addError(m);
            });
        } catch (Exception e) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("批量新增错误码失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }

    // 退回
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void goback(String ids,String status)  {
        String[] strArray=ids.split("~");;
        List<ErcdmgErrorComditionDO> elist = iErcdmgErorDao.selectErcdmgByErrorList(strArray);
        for(ErcdmgErrorComditionDO ercdmgError :elist){
            if(!ercdmgError.getCurtState().equals(status)){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("该操作员对此状态的错误码没有操作权限!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        String errcds="";
        for(ErcdmgErrorComditionDO e:elist){
            errcds+=e.getErrorCd()+",";
        }
        String str=null;
        List<ErcdmgPordUserDO> userList=null;
        if("2".equals(status)){
            //产品经理退回
            //查询错误码所属技术人员经理邮箱
            userList=iErcdmgErorDao.selectProdUserbyErrorCds(strArray);
            str="错误码："+errcds+"<br/>"+"已被产品验证人员退回";
        }else if ("3".equals(status)){
            //审核人员退回
            //查询错误码所属产品经理邮箱
            userList=iErcdmgErorDao.selectTechUserbyErrorCds(strArray);
            str="错误码："+errcds+"<br/>"+"已被审核验证人员退回";
        }else{
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("退回操作异常，退回失败!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }


        for(ErcdmgPordUserDO user:userList){
            if(user.getEmail()==null||"".equals(user.getEmail())){
                continue;
            }
            SendErrorEmail.sendEmail("错误码退回提醒", Constant.EMAIL_NAME,  Constant.EMAIL_PSWD,user.getEmail(), str);
        }
        //更新错误码状态
        String[] id = ids.split("~");
        //回退到上一阶段
        int backStatus=Integer.parseInt(status)-1;
        //批量更新
        iErcdmgErorDao.updateErrorCurtState(""+backStatus,id);
    }
}
