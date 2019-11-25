package com.cmpay.lemon.monitor.service.impl.error;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.datasource.TargetDataSource;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.dao.IErcdmgErorDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import com.cmpay.lemon.monitor.entity.ErrorRecordBeanDO;
import com.cmpay.lemon.monitor.entity.UserRoleDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.CreateSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    /**
     * 新增错误码
     * @param ercdmgErrorComditionBO 错误码对象
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void addError(ErcdmgErrorComditionBO ercdmgErrorComditionBO){
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
        // 查询sit错误码数据库并新增
        // String sit = selectSitMsg(ercdmgErrorComditionBO);
//        // 查询uat错误码并新增
//        selectUatMsg(ercdmgErrorComditionBO);
        // 新增考核错误码
        addErcdmgError(ercdmgErrorComditionBO);
        // 记录错误码导入记录
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        ErrorRecordBeanDO errorRecordBean = new ErrorRecordBeanDO();
        errorRecordBean.setErrorCode(ercdmgErrorComditionBO.getErrorCd());
//        if(sit != null){
//            errorRecordBean.setTimeStmp(df.format(new Date()));
//            errorRecordBean.setEnvirCode("SIT");
//            iErcdmgErorDao.insertErrorRecordBean(errorRecordBean);
//        }
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

    public void addErcdmgError(ErcdmgErrorComditionBO ercdmgError) {
        if(ercdmgError.getErrorCd()==null){
            //throw new ServiceException("你输入的错误码为空");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(ercdmgError.getErrorCd().length()>10){
            //throw new ServiceException("错误码长度不超过10位");
        }
//        //检查输入产品经理是否存在
//        TPermiUser tPermiUser=ercdmgErrorMapper.findByUsername(ercdmgError.getProdUserName());
//        if(tPermiUser==null){
//            //throw new ServiceException("你输入的产品经理名称:"+ercdmgError.getProdUserName()+"不存在，请重新输入后再试");
//        }
//        //检查用户角色
//        String roleName=ercdmgErrorMapper.searchUserRoleProd(tPermiUser.getUserId());
//
//        if (!ErrerConstant.PORD.equals(roleName)||roleName==null){
//            throw new ServiceException("你输入的用户"+tPermiUser.getUserName()+"非产品经理角色，请重新输入后再试");
//        }

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
    @TargetDataSource("dsSit")
    public String selectSitMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        try {
            ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
            BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
            Date ss = new Date();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyymmdd");//设置日期格式
            SimpleDateFormat df2 = new SimpleDateFormat("yyyymmddhh24miss");//设置日期格式
            errorComditionDO.setUpdDt(df1.format(ss));
            errorComditionDO.setTmSmp(df2.format(ss));
            String sitmsg_cd= iErcdmgErorDao.selectPubtmsg(ercdmgErrorComditionBO.getErrorCd());
            String sittsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
            System.err.println(sitmsg_cd);
            System.err.println(sittsg_cd);
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
        }
        return "";
    }
    /**
     * 查询Uat错误码
     */
    @TargetDataSource("dsUat")
    public void selectUatMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO) {
        ErcdmgErrorComditionDO errorComditionDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(errorComditionDO, ercdmgErrorComditionBO);
        String uatmsg_cd= iErcdmgErorDao.selectPubtmsg(ercdmgErrorComditionBO.getErrorCd());
        String uattsg_cd= iErcdmgErorDao.selectPubttms(ercdmgErrorComditionBO.getErrorCd(),ercdmgErrorComditionBO.getBuscnl());
        if(uattsg_cd!=null){
            //throw new ServiceException("你输入的错误码"+ercdmgError.getErrorCd()+"|"+ercdmgError.getBuscnl()+"在SIT数控库pubttms表中已存在");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的错误码"+ercdmgErrorComditionBO.getErrorCd()+"|"+ercdmgErrorComditionBO.getBuscnl()+"在SIT数控库pubttms表中已存在");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(uatmsg_cd==null){
            iErcdmgErorDao.insertPubtmsg(errorComditionDO);
        }
        iErcdmgErorDao.insertPubttms(errorComditionDO);
    }

    /**
     * 修改错误码
     * @param ercdmgErrorComditionBO 错误码对象
     */
    @Override
    public void updateError(ErcdmgErrorComditionBO ercdmgErrorComditionBO){

    }
}
