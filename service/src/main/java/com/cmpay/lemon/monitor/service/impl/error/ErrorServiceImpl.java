package com.cmpay.lemon.monitor.service.impl.error;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.framework.datasource.TargetDataSource;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgPordUserBO;
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
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
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
    //错误码审核人
    private static final Long SUPERADMINISTRATOR1 =(long)16001;
    //产品经理
    private static final Long SUPERADMINISTRATOR2 =(long)5002;
    //技术负责人
    private static final Long SUPERADMINISTRATOR3 =(long)5006;
    //普通员工
    private static final Long SUPERADMINISTRATOR4 =(long)4001;

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
        TPermiUser tPermiUser=iErcdmgErorDao.findByUsername(ercdmgError.getProdUserName());
        if(tPermiUser==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的产品经理名称:"+ercdmgError.getProdUserName()+"不存在，请重新输入后再试");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        ercdmgError.setProdUserId( tPermiUser.getUserId());

        //判断cr的长度 ,如果长度超过30，则截取前三十位
        if(ercdmgError.getCr().length()>30){
            ercdmgError.setCr(ercdmgError.getCr().substring(0,30));
        }
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
            if(sit_responseCode != HttpURLConnection.HTTP_OK){
                return "SIT";
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("操作SIT数控库失败,请删除错误码后重新录入");
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
            MsgEnum.ERROR_CUSTOM.setMsgInfo("操作UAT数控库失败,请删除错误码后重新录入");
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
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在UAT数控库pubttms表中不存在");
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
        TPermiUser tPermiUser=iErcdmgErorDao.findByUsername(ercdmgError.getProdUserName());
        if(tPermiUser==null){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的产品经理名称:"+ercdmgError.getProdUserName()+"不存在，请重新输入后再试");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        ercdmgError.setProdUserId( tPermiUser.getUserId());
        ercdmgError.setLastUpdateDate(LocalDateTime.now());
        iErcdmgErorDao.update(ercdmgError);
        //已生效同步生产修改
        if(ercdmgError.getCurtState().equals("5")){
            ercdmgError.setFtpUploadStatus("0");
            iErcdmgErorDao.insertForUpload(ercdmgError);

        }
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
        String[] strArray=ids.split("~");
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
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public ErcdmgErrorComditionRspBO forwardpord (String ids){
        String[] strArray=ids.split("~");
        List<ErcdmgErrorComditionBO> elist = BeanConvertUtils.convertList(iErcdmgErorDao.selectErcdmgByErrorList(strArray), ErcdmgErrorComditionBO.class);
        for(ErcdmgErrorComditionBO ercdmgError :elist){
            if(ercdmgError.getCurtState()!="1"&&!ercdmgError.getCurtState().equals("1")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择状态为：已录入的错误码！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        List<ErcdmgPordUserDO> pordUserList = iErcdmgErorDao.selectPordUser(iErcdmgErorDao.selectErcdmgByErrorList(strArray));
        List<ErcdmgPordUserBO> ercdmgPordUserBOList = BeanConvertUtils.convertList(pordUserList, ErcdmgPordUserBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgErrorComditionBOList(elist);
        productionRspBO.setErcdmgPordUserDTOList(ercdmgPordUserBOList);
        return  productionRspBO;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void pordsubmit(String ids,String emails,String emailContent) {
        String[] errorArr = ids.split("~");
        iErcdmgErorDao.updateErrorCurtState("2",errorArr);
        List<ErcdmgErrorComditionDO> elist = iErcdmgErorDao.selectErcdmgByErrorList(errorArr);
        String errcds="";
        for(ErcdmgErrorComditionDO e:elist){
            errcds+=e.getErrorCd()+",";
        }

        String emailCont="错误码："+errcds+"<br/>"+emailContent;
        // 发邮件至对应产品经理
        String[] emailsArr = emails.split("~");
        //String[] emailsArr = {"tu_yi@hisuntech.com","wu_lr@hisuntech.com","liujia3@hisuntech.com"};
        for (int i = 0; i < emailsArr.length; i++) {
            if (null == emailsArr[i] || "".equals(emailsArr[i])){
                continue;
            }
            SendErrorEmail.sendEmail("错误码提交给产品经理复核提醒", Constant.EMAIL_NAME,  Constant.EMAIL_PSWD,emailsArr[i], emailCont);
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public ErcdmgErrorComditionRspBO forwardaudi (String ids){
        String[] strArray=ids.split("~");
        List<ErcdmgErrorComditionBO> elist = BeanConvertUtils.convertList(iErcdmgErorDao.selectErcdmgByErrorList(strArray), ErcdmgErrorComditionBO.class);
        for(ErcdmgErrorComditionBO ercdmgError :elist){
            if(ercdmgError.getCurtState()!="2"&&!ercdmgError.getCurtState().equals("2")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择状态为：待产品复核的错误码！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        List<ErcdmgPordUserDO> pordUserList = iErcdmgErorDao.selectUserByRole();
        List<ErcdmgPordUserBO> ercdmgPordUserBOList = BeanConvertUtils.convertList(pordUserList, ErcdmgPordUserBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgErrorComditionBOList(elist);
        productionRspBO.setErcdmgPordUserDTOList(ercdmgPordUserBOList);
        return  productionRspBO;
    }

    //下一步，审核人员
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void audisubmit(String ids, String emails, String emailContent, Date updateDate){
        String dateStr= DateUtil.date2String(updateDate,DateUtil.PATTERN_DATE);
        String[] strArray = ids.split("~");
        List<ErcdmgErrorComditionDO> elist = iErcdmgErorDao.selectErcdmgByErrorList(strArray);
        String errcds = "";
        for(ErcdmgErrorComditionDO e:elist){
            errcds+=e.getErrorCd()+",";
        }
        //发送邮件
        String emailCont="错误码："+errcds+"<br/>"+emailContent+dateStr;
        iErcdmgErorDao.updateErrorCurtState("3", strArray);
        // 更新期望更新时间
        iErcdmgErorDao.updateErrorUpdDate(updateDate,strArray);
        // 发邮件至对应产品经理
        String[] emailsArr = emails.split("~");
        //String[] emailsArr = {"tu_yi@hisuntech.com","wu_lr@hisuntech.com","liujia3@hisuntech.com"};
        for (int i = 0; i < emailsArr.length; i++) {
            if (null == emailsArr[i] || "".equals(emailsArr[i])){
                continue;
            }
            SendErrorEmail.sendEmail("错误码提交给待审核人验证提醒", Constant.EMAIL_NAME,  Constant.EMAIL_PSWD,emailsArr[i], emailCont);
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public  void audiSubmitUpdmgn(String ids){
        String[] errorArr = ids.split("~");
        List<ErcdmgErrorComditionDO> errorList = iErcdmgErorDao.selectErcdmgByErrorList(errorArr);
        //检验错误码状态为待审核人验证
        for(ErcdmgErrorComditionDO ercdmgError :errorList){
            if(ercdmgError.getCurtState()!="3" &&!ercdmgError.getCurtState().equals("3")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择状态为：待审核人验证的错误码！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        //2、根据错误码更新错误码管理表（t_ercdmg_error）当前状态字段为：待提交
        iErcdmgErorDao.updateErrorCurtState("4", errorArr);
        //4、根据错误码查询错误码管理表（t_ercdmg_error）输入产品经理列表
        List<ErcdmgPordUserDO> prodUserIdList = iErcdmgErorDao.selectPordUser(iErcdmgErorDao.selectErcdmgByErrorList(errorArr));
        //5、根据产品经理列表循环新增更新管理表（t_ercdmg_updmgn）信息
        for(ErcdmgPordUserDO prodUser :prodUserIdList){
            //3、创建更新批次号
            String updateNo=CreateSequence.getSequence();//创建批次号
            //查找期望更新时间
            Date updateDate = iErcdmgErorDao.selectErrorUpdDate(prodUser.getProdUserId(),errorArr);
            //查询数量
            String count=iErcdmgErorDao.selectErrorCount(prodUser.getProdUserId(),errorArr);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt=new Date();
            String dateStr=sdf.format(dt);
            String content=dateStr+"( "+prodUser.getProdUserName()+" )提交待更新的错误提示";
            ErcdmgUpdmgnDO ercdmgUpdmgn =new ErcdmgUpdmgnDO();
            ercdmgUpdmgn.setUpdateNo(updateNo);
            ercdmgUpdmgn.setProdUserId(prodUser.getProdUserId());
            ercdmgUpdmgn.setUpdateDate(updateDate);
            ercdmgUpdmgn.setContent(content);
            ercdmgUpdmgn.setCount(count);
            ercdmgUpdmgn.setCurtState("01");
            ercdmgUpdmgn.setCreateTime(new Date());
            iErcdmgUpdmgnDao.insertUpdmgn(ercdmgUpdmgn);
            //6、根据错误码编号更新错误码管理表（t_ercdmg_error）更新批次号字段
            updateErrorUpdateNo(updateNo,prodUser.getProdUserId(),errorArr);
        }
    }
    public void updateErrorUpdateNo(String updateNo, String prodUserId,String[] errorCds) {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("updateNo", updateNo);
        map.put("prodUserId", prodUserId);
        map.put("errorCds",errorCds);
        iErcdmgErorDao.updateErrorUpdateNo(map);
    }
    //新增渠道
    @Override
    public ErcdmgErrorComditionBO addCnlCheckErrorCode(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        ErcdmgErrorComditionDO errorSingle=iErcdmgErorDao.selectErrorSingle(ercdmgErrorComditionBO.getId());
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
        ercdmgErrorComditionBO.setCreateUserId(SecurityUtils.getLoginName());
        ercdmgErrorComditionBO.setTechUserId(errorSingle.getTechUserId());//技术负责人id
        ercdmgErrorComditionBO.setTechUserName(errorSingle.getTechUserName());//技术负责人名称
        ercdmgErrorComditionBO.setCurtState("2");
        return ercdmgErrorComditionBO;
    }
    //新增渠道新增sit数据库
    @Override
    @TargetDataSource("dsSit")
    public void addCnlSitMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        Date ss = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        errorComditionDO.setUpdDt(df1.format(ss));
        errorComditionDO.setTmSmp(df2.format(ss));
        String sittsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        System.err.println("查询sit"+sittsg_cd);
        if(sittsg_cd!=null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在SIT数控库pubttms表中已存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        iErcdmgErorDao.insertPubttms(errorComditionDO);
    }
    //新增渠道新增uat数据库
    @Override
    @TargetDataSource("dsUat")
    public void addCnlUatMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        Date ss = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        errorComditionDO.setUpdDt(df1.format(ss));
        errorComditionDO.setTmSmp(df2.format(ss));
        String uattsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        System.err.println("uat:"+uattsg_cd);
        if(uattsg_cd!=null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在UAT数控库pubttms表中已存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        iErcdmgErorDao.insertPubttms(errorComditionDO);
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

    /**
     * 角色控制
     * @return
     */
    @Override
    public ErcdmgPordUserBO access(){
        // 获取当前操作人信息
        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
        String access ="";
        //判断是否是普通员工
        if(isDepartmentManager(SUPERADMINISTRATOR4)){
            access ="3";
        }
        //判断是否是产品经理
        if(isDepartmentManager(SUPERADMINISTRATOR2)){
            access ="2";
        }
        //判断是否是错误码审核人
        if(isDepartmentManager(SUPERADMINISTRATOR1)){
            access ="1";
        }
        //判断是否是超级管理员
        if(isDepartmentManager(SUPERADMINISTRATOR)){
            access ="0";
        }
        System.err.println(access);
        ErcdmgPordUserBO ercdmgPordUserBO = new ErcdmgPordUserBO();
        ercdmgPordUserBO.setUserAcesss(access);
        ercdmgPordUserBO.setProdUserName(currentUser);
        return ercdmgPordUserBO;
    }

    @Override
    public Boolean errorCdCheck(String errorStart){
        String errorCode= iErcdmgErorDao.checkErrorCode(errorStart);
        if(errorCode==null){
            return true;
        }
        return false;
    }
    //获取错误码，sit
    @Override
    @TargetDataSource("dsSit")
    public Boolean errorCdSit(String errorStart){
        String sitmsg_cd= iErcdmgErorDao.selectPubtmsg(errorStart);
       // String sittsg_cd= iErcdmgErorDao.CheckPubttms(errorStart);
//        if(sitmsg_cd==null&&sittsg_cd==null){
//            return true;
//        }
        if(sitmsg_cd==null){
            return true;
        }
        return false;
    }
    //获取错误码，uat
    @Override
    @TargetDataSource("dsUat")
    public Boolean errorCdUat(String errorStart){
        String sitmsg_cd= iErcdmgErorDao.selectPubtmsg(errorStart);
//        String sittsg_cd= iErcdmgErorDao.CheckPubttms(errorStart);
//        System.err.println("查询Uit"+sitmsg_cd);
//        System.err.println("查询Uit"+sittsg_cd);
//        if(sitmsg_cd==null&&sittsg_cd==null){
//            return true;
//        }
        if(sitmsg_cd==null){
            return true;
        }
        return false;
    }
    //查询错误码后缀
    @Override
    public int selectIndex(String errorCdStart){
        String errorCdEnd = iErcdmgErorDao.selectIndex(errorCdStart);
        int index ;
        if(errorCdEnd==null){
            index = 10000;
            iErcdmgErorDao.insertIndex( errorCdStart , errorCdEnd+"");
        }else {
            index = Integer.parseInt(errorCdEnd);
        }
        return index;
    }
    //新增后缀查询数
    @Override
    public void updateIndex(String errorCdStart ,int errorCdEnd){
        iErcdmgErorDao.updateIndex(errorCdEnd+"",errorCdStart );
    }
}
