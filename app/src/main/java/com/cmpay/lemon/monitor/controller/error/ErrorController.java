package com.cmpay.lemon.monitor.controller.error;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.bo.ErcdmgPordUserBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.entity.ErrorRecordBeanDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.CreateSequence;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;

/**
 * 需求月反馈
 */
@RestController
@RequestMapping(value = MonitorConstants.ERROR_PATH)
public class ErrorController {

    @Autowired
    private ErrorService errorService;
    @Autowired
    private ReqPlanService reqPlanService;
    @Autowired
    SystemUserService userService;

    /**
     * 分页需求列表
     *
     * @param reqDTO 查询条件
     * @return 错误码列表
     */
    @RequestMapping("/list")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO> getUserInfoPage(@RequestBody ErcdmgErrorComditionReqDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO demandBO = BeanUtils.copyPropertiesReturnDest(new ErcdmgErrorComditionBO(), reqDTO);
        ErcdmgErrorComditionRspBO demandRspBO = errorService.searchErroeList(demandBO);
        System.err.println(demandRspBO);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErrorComditionDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgErrorComditionBOList(), ErcdmgErrorComditionDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    //多数据源添加错误码
    public void addError1(ErcdmgErrorComditionBO errorComditionBO){
        errorComditionBO = errorService.checkErrorCodeExist(errorComditionBO);
        // 本地库 新增考核错误码
        errorService.addErcdmgError(errorComditionBO);
        // 查询sit错误码数据库并新增
        String sit = errorService.selectSitMsg(errorComditionBO);
        // 查询uat错误码数据库并新增
        String uat = errorService.selectUatMsg(errorComditionBO);
        // 记录错误码导入记录
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        ErrorRecordBeanDO errorRecordBean = new ErrorRecordBeanDO();
        errorRecordBean.setErrorCode(errorComditionBO.getErrorCd());
        if("SIT" == sit){
            errorRecordBean.setTimeStmp(df.format(new Date()));
            errorRecordBean.setEnvirCode("SIT");
            errorService.insertErrorRecordBean(errorRecordBean);
        }
        if("UAT" == uat){
            errorRecordBean.setTimeStmp(df.format(new Date()));
            errorRecordBean.setEnvirCode("UAT");
            errorService.insertErrorRecordBean(errorRecordBean);
        }
    }
    /**
     * 新增
     * @param
     * @return
     */
    @RequestMapping("/add")
    public GenericRspDTO addError(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO errorComditionBO = new ErcdmgErrorComditionBO();
        BeanConvertUtils.convert(errorComditionBO, reqDTO);
        addError1(errorComditionBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 修改
     * @param
     * @return
     */
    @RequestMapping("/update")
    public GenericRspDTO updateError(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO errorComditionBO = new ErcdmgErrorComditionBO();
        BeanConvertUtils.convert(errorComditionBO, reqDTO);
        errorService.checkErrorUP(errorComditionBO);
        errorService.updateSitMsg(errorComditionBO);
        errorService.updateUatMsg(errorComditionBO);
        errorService.checkErrorUP(errorComditionBO);
        errorService.updateErcdmgError(errorComditionBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除
     * @param id 错误码id
     * @return
     */
    @RequestMapping("/delete")
    public GenericRspDTO deleteError(@RequestParam("ids") String id){
        System.err.println(id);
        ErcdmgErrorComditionBO  errorSingle = errorService.checkErrorDelete(id);
        errorService.deleteSMsg(errorSingle);
        errorService.deleteUatMsg(errorSingle);
        errorService.delErcdmgError(id,errorSingle);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 错误码导入
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        System.err.println(file.getOriginalFilename());
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
                demandDO.setCr(map.get(i).get(0).toString().trim());
                demandDO.setErrorCd(map.get(i).get(1).toString().trim());
                demandDO.setProdMod(map.get(i).get(2).toString().trim());
                demandDO.setBuscnl(map.get(i).get(3).toString().trim());
                demandDO.setBusnTip(map.get(i).get(4).toString().trim());
                demandDO.setTechTip(map.get(i).get(5).toString().trim());
                demandDO.setAppScen(map.get(i).get(6).toString().trim());
                demandDO.setProdUserName(map.get(i).get(7).toString().trim());
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
//        try {
            //更新数据库
            updateList.forEach(m -> {
                addError1(m);
            });
//        } catch (Exception e) {
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("批量新增错误码失败");
//            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//        }
        //errorService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }
    @RequestMapping("/goback")
    public GenericRspDTO gobackError(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO.getTaskIdStr()+"==="+reqDTO.getStatus());
        errorService.goback(reqDTO.getTaskIdStr(),reqDTO.getStatus());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 模板下载
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/template/download")
    public GenericRspDTO<NoBody> downloadTmp(GenericDTO<NoBody> req, HttpServletResponse response) {
        doWrite("static/errorcode.xlsx", response);
        return GenericRspDTO.newSuccessInstance();
    }
    @RequestMapping("/forwardpord")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO>  forwardpord(@RequestParam("ids") String ids){
        ErcdmgErrorComditionRspBO demandRspBO = errorService.forwardpord(ids);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErrorComditionDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgErrorComditionBOList(), ErcdmgErrorComditionDTO.class));
        rspDTO.setErcdmgPordUserDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgPordUserDTOList(), ErcdmgPordUserDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 下一步转产品
     * @param reqDTO
     * @return
     */
    @RequestMapping("/pordsubmit")
    public GenericRspDTO pordsubmit(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO.getTaskIdStr()+"==="+reqDTO.getEmails()+"=="+reqDTO.getEmailContent());
        errorService.pordsubmit(reqDTO.getTaskIdStr(),reqDTO.getEmails(),reqDTO.getEmailContent());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    @RequestMapping("/forwardaudi")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO>  forwardaudi(@RequestParam("ids") String ids){
        ErcdmgErrorComditionRspBO demandRspBO = errorService.forwardaudi(ids);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErrorComditionDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgErrorComditionBOList(), ErcdmgErrorComditionDTO.class));
        rspDTO.setErcdmgPordUserDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgPordUserDTOList(), ErcdmgPordUserDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 下一步转产品
     * @param reqDTO
     * @return
     */
    @RequestMapping("/audisubmit")
    public GenericRspDTO audisubmit(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO.getTaskIdStr()+"==="+reqDTO.getEmails()+"=="+reqDTO.getEmailContent());
        errorService.audisubmit(reqDTO.getTaskIdStr(),reqDTO.getEmails(),reqDTO.getEmailContent(),reqDTO.getUpdateDateSH());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 保存至更新管理
     * @param ids
     * @return
     */
    @RequestMapping("/audiSubmitUpdmgn")
    public GenericRspDTO audiSubmitUpdmgn(@RequestParam("ids") String ids){
        errorService.audiSubmitUpdmgn(ids);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    @RequestMapping("/addCnl")
    public GenericRspDTO addCnl(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO errorComditionBO = new ErcdmgErrorComditionBO();
        BeanConvertUtils.convert(errorComditionBO, reqDTO);
        addCnl(errorComditionBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    //多数据源添加错误码
    public void addCnl(ErcdmgErrorComditionBO errorComditionBO){
        errorComditionBO = errorService.addCnlCheckErrorCode(errorComditionBO);
        // 新增渠道sit
       errorService.addCnlSitMsg(errorComditionBO);
        // 新增渠道uat
        errorService.addCnlUatMsg(errorComditionBO);
        // 本地库 新增考核错误码
        errorService.addErcdmgError(errorComditionBO);
    }
    /*
    * 角色权限控制
    * 技术负责人，产品经理，错误码审核人access
    * */
    @RequestMapping("/access")
    public GenericRspDTO<ErcdmgPordUserDTO> access(){
        ErcdmgPordUserBO ercdmgPordUserBO = errorService.access();
        ErcdmgPordUserDTO rspDTO =  new ErcdmgPordUserDTO();
        BeanConvertUtils.convert(rspDTO, ercdmgPordUserBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
}
