package com.cmpay.lemon.monitor.service.impl.automaticCommissioningInterface;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.monitor.bo.AutoCancellationProductionBO;
import com.cmpay.lemon.monitor.bo.AutomatedProductionBO;
import com.cmpay.lemon.monitor.dao.IAutomatedProductionRegistrationDao;
import com.cmpay.lemon.monitor.dao.IPreproductionExtDao;
import com.cmpay.lemon.monitor.entity.AutomatedProductionRegistrationDO;
import com.cmpay.lemon.monitor.entity.PreproductionDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.automaticCommissioningInterface.AutomaticCommissioningInterfaceService;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
@Service
public class AutomaticCommissioningInterfaceServiceImpl implements AutomaticCommissioningInterfaceService {
    @Autowired
    IAutomatedProductionRegistrationDao automatedProductionRegistrationDao;
    @Autowired
    IPreproductionExtDao iPreproductionExtDao;
    /**
     * 异步调用预投产接口 - 增加自动化投产包
     * @param automatedProductionBO
     */
    // @Async
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void automatedProduction(AutomatedProductionBO automatedProductionBO) {
        System.err.println("预投产调用");
        System.err.println(Thread.currentThread().getName());
        System.err.println(automatedProductionBO.getProNumber());
        System.err.println(automatedProductionBO.getJson());
        int code =0;
        int i=0;
        //todo 调用预投产
        while(true) {
            try {
                Response response = given()
                        .header("Content-Type", "application/json")
                        .header("charset", "utf-8")
                        .body(automatedProductionBO.getJson())
                        .post("http://127.0.0.1:6005/v1/monitoringui/preproduction/test");
                response.prettyPrint();
                System.err.println("返回码:"+code);
                code = response.getStatusCode();

                break;
            } catch (Throwable e) {
                i++;
                System.err.println("catch:"+code);
                if(i>2) {
                    AutomatedProductionRegistrationDO automatedProductionRegistrationDO = new AutomatedProductionRegistrationDO();
                    automatedProductionRegistrationDO.setCreatTime(LocalDateTime.now());
                    automatedProductionRegistrationDO.setEnv(automatedProductionBO.getEnv());
                    automatedProductionRegistrationDO.setPronumber(automatedProductionBO.getProNumber());
                    automatedProductionRegistrationDO.setRemark("接口调用失败");
                    automatedProductionRegistrationDO.setStatus("失败");
                    automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);
                    //消息回退
                    // 同步操作报错
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("调用自动化投产增加自动化投产包接口失败！");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                    PreproductionDO preproductionDO = iPreproductionExtDao.get(automatedProductionRegistrationDO.getPronumber());
//                    if(preproductionDO.getPreStatus().equals("预投产待部署")){
//                        preproductionDO.setPreStatus("预投产提出");
//                        iPreproductionExtDao.updatePreSts(preproductionDO);
//                    }
                    return;
                }
            }
        }
        System.err.println("返回码1:"+code);
        //登记自动化
        AutomatedProductionRegistrationDO automatedProductionRegistrationDO = new AutomatedProductionRegistrationDO();
        automatedProductionRegistrationDO.setCreatTime(LocalDateTime.now());
        automatedProductionRegistrationDO.setEnv(automatedProductionBO.getEnv());
        automatedProductionRegistrationDO.setPronumber(automatedProductionBO.getProNumber());
        if( code ==200){
            automatedProductionRegistrationDO.setStatus("成功");
            automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);
        } else if(code==450){
            //450则包格式错误
            automatedProductionRegistrationDO.setRemark("投产包异常");
            automatedProductionRegistrationDO.setStatus("失败");
            automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);

            //消息回退
//            PreproductionDO preproductionDO = iPreproductionExtDao.get(automatedProductionRegistrationDO.getPronumber());
//
//            if(preproductionDO.getPreStatus().equals("预投产待部署")){
//                preproductionDO.setPreStatus("预投产提出");
//                iPreproductionExtDao.updatePreSts(preproductionDO);
//            }
            // 同步操作报错
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("调用自动化投产增加自动化投产包接口失败：投产包异常！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }else{
            //其他错误
            automatedProductionRegistrationDO.setRemark("其他错误");
            automatedProductionRegistrationDO.setStatus("失败");
            automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);

            //消息回退
//            PreproductionDO preproductionDO = iPreproductionExtDao.get(automatedProductionRegistrationDO.getPronumber());
//            System.err.println(automatedProductionRegistrationDO.getPronumber());
//            System.err.println(preproductionDO.getPreStatus());
//
//            if(preproductionDO.getPreStatus().equals("预投产待部署")){
//                preproductionDO.setPreStatus("预投产提出");
//                iPreproductionExtDao.updatePreSts(preproductionDO);
//            }
            // 同步操作报错
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("调用自动化投产增加自动化投产包接口失败：其他错误！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }

    /**
     * 异步调用预投产接口 - 2．	取消自动化投产包
     * @param autoCancellationProductionBO
     */
    // @Async
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void autoCancellationProduction(AutoCancellationProductionBO autoCancellationProductionBO) {
        System.err.println("取消自动化投产包");
        System.err.println(Thread.currentThread().getName());
        System.err.println(autoCancellationProductionBO.getProNumber());
        System.err.println(autoCancellationProductionBO.getJson());
        int code =0;
        int i=0;
        while(true) {
            try {
                Response response = given()
                        .header("Content-Type", "application/json")
                        .header("charset", "utf-8")
                        .body(autoCancellationProductionBO.getJson())
                        .post("http://127.0.0.1:6005/v1/monitoringui/preproduction/test1");
                response.prettyPrint();
                System.err.println("返回码:"+code);
                code = response.getStatusCode();
                break;
            } catch (Throwable e) {
                i++;
                System.err.println("catch:"+code);
                if(i>2) {
                    AutomatedProductionRegistrationDO automatedProductionRegistrationDO = new AutomatedProductionRegistrationDO();
                    automatedProductionRegistrationDO.setCreatTime(LocalDateTime.now());
                    automatedProductionRegistrationDO.setPronumber(autoCancellationProductionBO.getProNumber());
                    automatedProductionRegistrationDO.setRemark("接口调用失败");
                    automatedProductionRegistrationDO.setStatus("失败");
                    automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);
                    // 同步操作报错
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("调用自动化投产取消接口失败！");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                    //消息回退
//                    PreproductionDO preproductionDO = iPreproductionExtDao.get(automatedProductionRegistrationDO.getPronumber());
//                    if(preproductionDO.getPreStatus().equals("预投产待部署")){
//                        preproductionDO.setPreStatus("预投产提出");
//                        iPreproductionExtDao.updatePreSts(preproductionDO);
//                    }
                    return;
                }
            }
        }
        System.err.println("返回码1:"+code);
        //登记自动化
        AutomatedProductionRegistrationDO automatedProductionRegistrationDO = new AutomatedProductionRegistrationDO();
        automatedProductionRegistrationDO.setCreatTime(LocalDateTime.now());
        automatedProductionRegistrationDO.setEnv("0");
        automatedProductionRegistrationDO.setPronumber(autoCancellationProductionBO.getProNumber());
        if( code ==200){
            automatedProductionRegistrationDO.setStatus("成功");
            automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);
        }else{
            //其他错误
            automatedProductionRegistrationDO.setRemark("接口调用失败");
            automatedProductionRegistrationDO.setStatus("失败");
            automatedProductionRegistrationDao.insert(automatedProductionRegistrationDO);
            // 同步操作报错
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("调用自动化投产取消接口失败！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);

//            //消息回退
//            PreproductionDO preproductionDO = iPreproductionExtDao.get(automatedProductionRegistrationDO.getPronumber());
//            System.err.println(automatedProductionRegistrationDO.getPronumber());
//            System.err.println(preproductionDO.getPreStatus());

//            if(preproductionDO.getPreStatus().equals("预投产待部署")){
//                preproductionDO.setPreStatus("预投产提出");
//                iPreproductionExtDao.updatePreSts(preproductionDO);
//            }
        }
    }
}
