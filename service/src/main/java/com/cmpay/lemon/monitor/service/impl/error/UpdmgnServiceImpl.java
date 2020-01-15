package com.cmpay.lemon.monitor.service.impl.error;

import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgUpdmgnBO;
import com.cmpay.lemon.monitor.dao.IErcdmgErorDao;
import com.cmpay.lemon.monitor.dao.IErcdmgUpdmgnDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import com.cmpay.lemon.monitor.entity.ErcdmgUpdmgnDO;
import com.cmpay.lemon.monitor.entity.TPermiUser;
import com.cmpay.lemon.monitor.entity.sendemail.MultiMailSenderInfo;
import com.cmpay.lemon.monitor.entity.sendemail.MultiMailsender;
import com.cmpay.lemon.monitor.entity.sendemail.SendEmailConfig;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.errror.UpdmgnService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 需求计划
 * @author: ty
 */
@Service
public class UpdmgnServiceImpl implements UpdmgnService {
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
        PageInfo<ErcdmgUpdmgnBO> pageInfo = getPageInfo(ercdmgErrorComditionBO);
        List<ErcdmgUpdmgnBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), ErcdmgUpdmgnBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgUpdmgnBOList(productionBOList);
        productionRspBO.setUpdmgnBOPageInfo(pageInfo);
        return  productionRspBO;
    }
    private PageInfo<ErcdmgUpdmgnBO> getPageInfo(ErcdmgErrorComditionBO demandBO) {
        ErcdmgErrorComditionDO demandDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        System.err.println(demandDO);
        System.err.println(demandBO);
        PageInfo<ErcdmgUpdmgnBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iErcdmgUpdmgnDao.findErcdmgUpdmgnList(demandDO), ErcdmgUpdmgnBO.class));
        return pageInfo;
    }
    //查看
    public ErcdmgErrorComditionRspBO details (String ids){
        ErcdmgUpdmgnDO ercdmgUpdmgnBean = iErcdmgUpdmgnDao.searchSingleUpdmgn(ids);
        ercdmgUpdmgnBean.setProdUserName(userService.getFullname(ercdmgUpdmgnBean.getProdUserId()));
        List<ErcdmgErrorComditionDO> errorList=iErcdmgErorDao.searchErcdmgErrorList(ids);
        List<ErcdmgErrorComditionBO> ercdmgErrorComditionBOS =  BeanConvertUtils.convertList(errorList, ErcdmgErrorComditionBO.class);
        List<ErcdmgUpdmgnDO> ercdmgPordUserBOList = new ArrayList<>();
        ercdmgPordUserBOList.add(ercdmgUpdmgnBean);
        List<ErcdmgUpdmgnBO> ercdmgUpdmgnBOS = BeanConvertUtils.convertList(ercdmgPordUserBOList, ErcdmgUpdmgnBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgUpdmgnBOList(ercdmgUpdmgnBOS);
        productionRspBO.setErcdmgErrorComditionBOList(ercdmgErrorComditionBOS);
        return  productionRspBO;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public ErcdmgErrorComditionRspBO updmgnProduction (String ids){
        String[] updateNosArr = ids.split("~");
        List<ErcdmgUpdmgnDO>  updmmgnList = iErcdmgUpdmgnDao.searchUpdmgnList(updateNosArr);
        for(ErcdmgUpdmgnDO ercdmgUpdmgn:updmmgnList){
            if(ercdmgUpdmgn.getCurtState().equals("02")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择状态为：待提交的更新清单！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        List<ErcdmgUpdmgnBO> ercdmgUpdmgnBOS = BeanConvertUtils.convertList(updmmgnList, ErcdmgUpdmgnBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgUpdmgnBOList(ercdmgUpdmgnBOS);
        return  productionRspBO;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public  void submitPorduct(String updateNos,  String emailCont){
        String[] updateNosArr = updateNos.split("~");
        //根据批次号查询出CR
        List<String> crList =iErcdmgErorDao.selectCRByUpdateNo(updateNosArr);
        List<String> emails =iErcdmgErorDao.selectUserForEmail(updateNosArr);
        boolean flag=false;
        StringBuffer cont = new StringBuffer();
        cont.append(""+emailCont+";<br/>");
        cont.append("<br/>");
        for(String cr :crList){
            //根据cr和批次号查询错误码
            List<ErcdmgErrorComditionDO> errorList =iErcdmgErorDao.selectErrorByUpdateNoAndCR(updateNosArr,cr);

            if(errorList!=null){
                flag=true;   //有错误码，需要发送邮件
                if(!("null".equals(cr)||"".equals(cr)||cr==null)){
                    cont.append("--CR或Plog编号:"+cr+"<br/>");
                }
                cont.append("--在核心库1下PAYADM用户下执行--<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                cont.append("select * from PUBTMSG <br/> where msg_cd in ( ");
                String pubtmsgStr="";
                for(ErcdmgErrorComditionDO ercdmgError:errorList){
                    pubtmsgStr+="'"+ercdmgError.getErrorCd()+"',";
                }
                String pubtmsgStrSub=pubtmsgStr.substring(0,pubtmsgStr.length()-1);
                cont.append(pubtmsgStrSub);
                cont.append("); <br/>");
                cont.append("<br/>");

                StringBuffer redisStr = new StringBuffer();
                redisStr.append("<br/>");
                redisStr.append("--技术消息码缓存redis，执行脚本将错误码存入 redis集群（172.29.46.24-26:6379/6380 ）<br/>");

                for (ErcdmgErrorComditionDO ercdmgError:errorList) {
                    cont.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    cont.append("insert into PUBTMSG (MSG_CD, MSG_TYP, ATH_LVL1, ATH_LVL2, MSG_INF, MON_FLG, MSG_DESC, WARN_LVL, LAST_UPD_OPR, UPD_DT, TM_SMP, BMSG_INF) <br/> values ");
                    cont.append("('"+ercdmgError.getErrorCd()+"',");
                    cont.append("'E',");
                    cont.append("'0',");
                    cont.append("'0',");
                    cont.append("'"+ercdmgError.getTechTip()+"',");
                    cont.append("'0',");
                    cont.append("'"+ercdmgError.getAppScen()+"',");
                    cont.append("' ',");
                    cont.append("' ',");
                    cont.append("to_char(sysdate,'yyyymmdd'),");
                    cont.append("to_char(sysdate,'yyyymmddhh24miss'),");
                    cont.append("' '); <br/>");
                    cont.append("<br/>");
                    redisStr.append("&nbsp;&nbsp;&nbsp;&nbsp;sh ./alert-redis.sh  'insert' 'pubtmsg' '"+ercdmgError.getErrorCd()+"' 'busCnl' 'busType' 'E' '"+ercdmgError.getTechTip()+"' '0' ' '<br/>");

                }
                cont.append("--在核心库1下PAYADM用户下执行<br/>");

                cont.append("select * from PUBTTMS <br/> where msg_cd in ( ");

                String PUBTTMSStr="";

                for(ErcdmgErrorComditionDO ercdmgError:errorList){
                    PUBTTMSStr+="'"+ercdmgError.getErrorCd()+"',";
                }
                String PUBTTMSStrSub=PUBTTMSStr.substring(0,PUBTTMSStr.length()-1);
                cont.append(PUBTTMSStrSub);

                cont.append("); <br/>");
                cont.append("<br/>");

                redisStr.append("--业务消息码缓存redis，执行脚本将错误码存入 redis集群（172.29.46.24-26:6379/6380 ）<br/>");
                for (ErcdmgErrorComditionDO ercdmgError:errorList) {
                    cont.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    cont.append("insert into PUBTTMS (MSG_CD, BUS_CNL, MSG_INF, MSG_DESC, LAST_UPD_OPR, UPD_DT, TM_SMP, BUS_TYP) <br/> values ");
                    cont.append("('"+ercdmgError.getErrorCd()+"',");
                    cont.append("'"+ercdmgError.getBuscnl()+"',");
                    cont.append("'"+ercdmgError.getBusnTip()+"',");
                    cont.append("'"+ercdmgError.getAppScen()+"',");
                    cont.append("' ',");
                    cont.append("to_char(sysdate,'yyyymmdd'),");
                    cont.append("to_char(sysdate,'yyyymmddhh24miss'),");
                    cont.append("'*'); <br/>");
                    cont.append("<br/>");
                    redisStr.append("&nbsp;&nbsp;&nbsp;&nbsp;sh ./alert-redis.sh  'insert' 'pubttms' '"+ercdmgError.getErrorCd()+"' '"+ercdmgError.getBuscnl()+"' '*' 'msgType' '"+ercdmgError.getBusnTip()+"' 'monFlg' 'bmsgInf'<br/>");
                }
                cont.append(redisStr.toString());
                cont.append("<br/>");
                cont.append("Redis操作注意：<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;1、需在终端测试部署redis机器，是否能运行redis-cli命令行；如果不能，则需检查redis环境变量是否配置正确；\n" +
                        "如果能则进行下一步；<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;2、alert-redis.sh 部署redis机器的终端上；<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;3、alert-redis.sh 进行编辑，由于 redis-cli -c -h 172.16.49.135 -p 6679 -a \"password\" --raw 需要替换IP地址、端口号及其登录redis缓存密码；<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;4、执行 redis缓存语句 ，对其做存储与更新。<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执行过程中报错：需检查redis配置文件与password redis登录密码是否正确；<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执行结果：当其存储成功终端界面会返回0与1；<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(integer) 0：更新成功，Redis缓存中有数据；<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(integer) 1 ：新增成功，Redis缓存中没有数据。<br/>");
                cont.append("&nbsp;&nbsp;&nbsp;&nbsp;5、必须utf-8编码；<br/>");
            }
        }

        //发送邮件
        if(flag){
            try {
                String[] receivers=new String[]{};
                if(emails!=null){
                    receivers = emails.toArray(new String[]{});

                }

                SendEmailConfig config = new SendEmailConfig();
                String receivers2[] = config.getErroCodeMailTo();

                String[] bothReceivers=null;

                if(receivers!=null&&receivers.length!=0){
                    bothReceivers = concat(receivers2, receivers);
                }else{
                    bothReceivers =receivers2;
                }

                String email=new String(cont);

                //保存至本地
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String dateStr = df.format(calendar.getTime());
                PrintWriter pw =null;
                try {
                    if(LemonUtils.getEnv().equals(Env.SIT)) {
                        pw= new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/devms/temp/upload/errcd/" + File.separator+ dateStr+"_mail.txt"),"gbk"));
                    }
                    else if(LemonUtils.getEnv().equals(Env.DEV)) {
                        pw= new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/devadm/temp/upload/errcd/" + File.separator+ dateStr+"_mail.txt"),"gbk"));
                    }else {
                        MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                        MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                        BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                    }
                    pw.print(email);
                    pw.flush();
                    if (pw != null) {
                        pw.close();
                    }
                } catch (Exception e) {
                    if (pw != null) {
                        pw.close();
                    }
                    e.printStackTrace();
                }

                // 创建邮件信息
                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
                mailInfo.setMailServerHost("smtp.qiye.163.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUsername(Constant.EMAIL_NAME);
                mailInfo.setPassword(Constant.EMAIL_PSWD);
                mailInfo.setFromAddress(Constant.EMAIL_NAME);
                mailInfo.setToAddress(Constant.VERSION);
                mailInfo.setSubject("和包错误码在线更新通知(新增)");
                mailInfo.setContent(email);
                mailInfo.setCcs(bothReceivers);



                MultiMailsender.sendMailtoMultiCC(mailInfo);
                //1更新管理表当前状态值为：已生效
                iErcdmgUpdmgnDao.updateUpdmgnState(updateNosArr);
                //2更新错误码管理表当前状态值为：已生效
                iErcdmgUpdmgnDao.updateErrorState(updateNosArr);

            } catch (Exception e) {
                e.printStackTrace();
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("邮件发送失败");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }

    }
    public  String[] concat(String[] a, String[] b) {
        String[] c= new String[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updatePro() {
        //获取登录用户名
        String userName = userService.getFullname(SecurityUtils.getLoginName());
        TPermiUser currentUser =iErcdmgErorDao.findByUsername(userName);
        // 收件人信息
        SendEmailConfig config = new SendEmailConfig();
        String[] bothReceivers = config.getErroCodeMailTo();
        if (currentUser.getEmail() != null && currentUser.getEmail().trim().equals("")) {
            bothReceivers = concat(config.getErroCodeMailTo(), new String[]{currentUser.getEmail().trim()});
        }
      //  bothReceivers = concat(config.getErroCodeMailTo(), new String[]{"tu_yi@hisuntech.com","liujia3@hisuntech.com"});

        // 获取投产更新表信息
        List<ErcdmgErrorComditionDO> errorList = iErcdmgErorDao.queryForUpload();
        if (errorList == null || errorList.size() == 0){
            // 返回错误
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("没有数据更新");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 可优化
        List<String> errorIds = new ArrayList<>();

        // 创建邮件内容
        StringBuffer sqlStr = new StringBuffer();
        StringBuffer redisStr = new StringBuffer();
        sqlStr.append("版本更新组好，需求错误码已审批完毕，请及时更新至生产环境;<br/>");
        sqlStr.append("<br/>");
        sqlStr.append("--在核心库1下PAYADM用户下执行--<br/>");
        redisStr.append("--在部署redis终端上，执行脚本将错误码存入 redis集群（172.29.46.24-26:6379/6380 ）-<br/>");
        for (ErcdmgErrorComditionDO error : errorList) {
            errorIds.add(error.getId());
            sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            sqlStr.append("UPDATE PUBTTMS SET MSG_INF='" + error.getBusnTip() + "' WHERE MSG_CD= '" + error.getErrorCd() + "' AND BUS_CNL='" + error.getBuscnl() + "' ; <br/>");
            sqlStr.append("<br/>");
            sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;UPDATE PUBTMSG SET MSG_INF='" + error.getTechTip() + "' WHERE MSG_CD= '" + error.getErrorCd() + "' ; <br/>");
            sqlStr.append("<br/>");
            redisStr.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            redisStr.append("sh ./alert-redis.sh 'update' 'pubttms' '"+error.getErrorCd()+"' '"+error.getBuscnl()+"' '*' 'msgType' '"+error.getBusnTip()+"' 'monFlg' 'bmsgInf' <br/>");
            redisStr.append("&nbsp;&nbsp;&nbsp;&nbsp;sh ./alert-redis.sh 'update' 'pubtmsg' '"+error.getErrorCd()+"' 'busCnl' 'busType' 'E' '"+error.getTechTip()+"' '0' ''  <br/>");
            redisStr.append("<br/>");
        }
        sqlStr.append(redisStr.toString());

        sqlStr.append("<br/>");
        sqlStr.append("Redis操作注意：<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;1、需在终端测试部署redis机器，是否能运行redis-cli命令行；如果不能，则需检查redis环境变量是否配置正确；\n" +
                "如果能则进行下一步；<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;2、alert-redis.sh 部署redis机器的终端上；<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;3、alert-redis.sh 进行编辑，由于 redis-cli -c -h 172.16.49.135 -p 6679 -a \"password\" --raw 需要替换IP地址、端口号及其登录redis缓存密码；<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;4、执行 redis缓存语句 ，对其做存储与更新。<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执行过程中报错：需检查redis配置文件与password redis登录密码是否正确；<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执行结果：当其存储成功终端界面会返回0与1；<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(integer) 0：更新成功，Redis缓存中有数据；<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(integer) 1 ：新增成功，Redis缓存中没有数据。<br/>");
        sqlStr.append("&nbsp;&nbsp;&nbsp;&nbsp;5、必须utf-8编码；<br/>");

        // 创建邮件信息
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);
        mailInfo.setToAddress(Constant.VERSION);
        mailInfo.setSubject("和包错误码在线更新通知(修改)");
        mailInfo.setContent(sqlStr.toString());
        mailInfo.setCcs(bothReceivers);
        try{
            MultiMailsender.sendMailtoMultiCC(mailInfo);
        } catch (Exception e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("邮件发送失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 通过错误码标示更新代表成功
        iErcdmgErorDao.updateForUpload(errorIds);
    }
}
