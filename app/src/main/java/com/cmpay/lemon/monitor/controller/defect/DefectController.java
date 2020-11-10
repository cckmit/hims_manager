package com.cmpay.lemon.monitor.controller.defect;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.defects.DefectsService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;

@RestController
@RequestMapping(value = MonitorConstants.DEFECT_PATH)
public class DefectController {
    @Autowired
    DefectsService defectsService;


    @RequestMapping(value = "/findDefectList", method = RequestMethod.POST)
    public GenericRspDTO<ProductionDefectsRspDTO> findDefectAll(@RequestBody ProductionDefectsReqDTO productionDefectsReqDTO) {
        ProductionDefectsBO productionDefectsBO = BeanUtils.copyPropertiesReturnDest(new ProductionDefectsBO(), productionDefectsReqDTO);

        ProductionDefectsRspBO productionDefectsRspBO = defectsService.findDefectAll(productionDefectsBO);

        ProductionDefectsRspDTO rspDTO = new ProductionDefectsRspDTO();
        rspDTO.setProductionDefectsDTOList(BeanConvertUtils.convertList(productionDefectsRspBO.getProductionDefectsBOList(), ProductionDefectsDTO.class));
        rspDTO.setPageNum(productionDefectsRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionDefectsRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionDefectsRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionDefectsRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    @RequestMapping(value = "/findTestFailedList", method = RequestMethod.POST)
    public GenericRspDTO<SmokeTestRegistrationRsqDTO> smokeTestFailedQuery(@RequestBody SmokeTestRegistrationReqDTO smokeTestRegistrationReqDTO) {
        SmokeTestRegistrationBO smokeTestRegistrationBO = BeanUtils.copyPropertiesReturnDest(new SmokeTestRegistrationBO(), smokeTestRegistrationReqDTO);

        SmokeTestRegistrationRspBO smokeTestRegistrationRspBO = defectsService.smokeTestFailedQuery(smokeTestRegistrationBO);

        SmokeTestRegistrationRsqDTO rspDTO = new SmokeTestRegistrationRsqDTO();
        rspDTO.setSmokeTestRegistrationDTOList(BeanConvertUtils.convertList(smokeTestRegistrationRspBO.getSmokeTestRegistrationBOList(), SmokeTestRegistrationDTO.class));
        rspDTO.setPageNum(smokeTestRegistrationRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(smokeTestRegistrationRspBO.getPageInfo().getPages());
        rspDTO.setTotal(smokeTestRegistrationRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(smokeTestRegistrationRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     * 导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public GenericRspDTO<NoBody> download(@RequestBody ProductionDefectsReqDTO productionDefectsReqDTO, HttpServletResponse response) {
        ProductionDefectsBO productionDefectsBO = BeanUtils.copyPropertiesReturnDest(new ProductionDefectsBO(), productionDefectsReqDTO);
        defectsService.getDownload(response, productionDefectsBO);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/downloadTest")
    public GenericRspDTO<NoBody> downloadTest(@RequestBody SmokeTestRegistrationReqDTO smokeTestRegistrationReqDTO, HttpServletResponse response) {
        SmokeTestRegistrationBO smokeTestRegistrationBO = BeanUtils.copyPropertiesReturnDest(new SmokeTestRegistrationBO(), smokeTestRegistrationReqDTO);
        defectsService.getDownloadTest(response, smokeTestRegistrationBO);
        return GenericRspDTO.newSuccessInstance();
    }


    @RequestMapping(value = "/zenQuestiontFindList", method = RequestMethod.POST)
    public GenericRspDTO<ZenQuestiontRspDTO> zenQuestiontFindList(@RequestBody ZenQuestiontReqDTO zenQuestiontReqDTO) {
        if((zenQuestiontReqDTO.getStartTime() != null && !zenQuestiontReqDTO.getStartTime().equals(""))&&(zenQuestiontReqDTO.getEndTime()==null || zenQuestiontReqDTO.getEndTime().equals(""))){
            zenQuestiontReqDTO.setCreateddate(zenQuestiontReqDTO.getStartTime());
        }
        if((zenQuestiontReqDTO.getStartTime() == null|| zenQuestiontReqDTO.getStartTime().equals(""))&&(zenQuestiontReqDTO.getEndTime()!=null && !zenQuestiontReqDTO.getEndTime().equals(""))){
            zenQuestiontReqDTO.setCreateddate(zenQuestiontReqDTO.getEndTime());
        }
        ZenQuestiontBO zenQuestiontBO = BeanUtils.copyPropertiesReturnDest(new ZenQuestiontBO(), zenQuestiontReqDTO);

        ZenQuestiontRspBO zenQuestiontRspBO = defectsService.zenQuestiontFindList(zenQuestiontBO);

        ZenQuestiontRspDTO rspDTO = new ZenQuestiontRspDTO();
        rspDTO.setZenQuestiontDTOList(BeanConvertUtils.convertList(zenQuestiontRspBO.getZenQuestiontBOList(), ZenQuestiontDTO.class));
        rspDTO.setPageNum(zenQuestiontRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(zenQuestiontRspBO.getPageInfo().getPages());
        rspDTO.setTotal(zenQuestiontRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(zenQuestiontRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     * 禅道数据导入
     *
     * @return
     */
    @PostMapping("/zennDataImport")
    public GenericRspDTO<NoBody> zennDataImport (HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        defectsService.zennDataImport(file);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/downloadZenQuestiont")
    public GenericRspDTO<NoBody> downloadZenQuestiont(@RequestBody ZenQuestiontReqDTO zenQuestiontReqDTO, HttpServletResponse response) {
        ZenQuestiontBO zenQuestiontBO = BeanUtils.copyPropertiesReturnDest(new ZenQuestiontBO(), zenQuestiontReqDTO);
        defectsService.downloadZenQuestiont(response, zenQuestiontBO);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping(value = "/onlineDefectFindList", method = RequestMethod.POST)
    public GenericRspDTO<OnlineDefectRspDTO> onlineDefectFindList(@RequestBody OnlineDefectReqDTO onlineDefectReqDTO) {

        OnlineDefectBO onlineDefectBO = BeanUtils.copyPropertiesReturnDest(new OnlineDefectBO(), onlineDefectReqDTO);

        OnlineDefectRspBO onlineDefectRspBO = defectsService.onlineDefectFindList(onlineDefectBO);

        OnlineDefectRspDTO rspDTO = new OnlineDefectRspDTO();
        rspDTO.setOnlineDefectDTOList(BeanConvertUtils.convertList(onlineDefectRspBO.getOnlineDefectBOList(), OnlineDefectDTO.class));
        rspDTO.setPageNum(onlineDefectRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(onlineDefectRspBO.getPageInfo().getPages());
        rspDTO.setTotal(onlineDefectRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(onlineDefectRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     * 线上数据导入
     *
     * @return
     */
    @PostMapping("/onlineDefectImport")
    public GenericRspDTO<NoBody> onlineDefectImport (HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        defectsService.onlineDefectImport(file);
        return GenericRspDTO.newSuccessInstance();
    }


    /**
     * 导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/onlineDefectDownloadt")
    public GenericRspDTO<NoBody> onlineDefectDownloadt(@RequestBody OnlineDefectReqDTO zenQuestiontReqDTO, HttpServletResponse response) {
        OnlineDefectBO zenQuestiontBO = BeanUtils.copyPropertiesReturnDest(new OnlineDefectBO(), zenQuestiontReqDTO);
        defectsService.onlineDefectDownloadt(response, zenQuestiontBO);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping(value = "/internalDefectInquiry", method = RequestMethod.POST)
    public GenericRspDTO<DefectDetailsRspDTO> internalDefectInquiry(@RequestBody DefectDetailsReqDTO defectDetailsReqDTO) {

        DefectDetailsBO defectDetailsBO = BeanUtils.copyPropertiesReturnDest(new DefectDetailsBO(), defectDetailsReqDTO);

        DefectDetailsRspBO defectDetailsRspBO = defectsService.internalDefectInquiry(defectDetailsBO);

        DefectDetailsRspDTO rspDTO = new DefectDetailsRspDTO();
        rspDTO.setDefectDetailsDTOArrayList(BeanConvertUtils.convertList(defectDetailsRspBO.getDefectDetailsBos(), DefectDetailsDTO.class));
        rspDTO.setPageNum(defectDetailsRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(defectDetailsRspBO.getPageInfo().getPages());
        rspDTO.setTotal(defectDetailsRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(defectDetailsRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }


    @RequestMapping("/internalDefectDownload")
    public GenericRspDTO<NoBody> internalDefectDownload(@RequestBody DefectDetailsReqDTO defectDetailsReqDTO, HttpServletResponse response) {
        DefectDetailsBO defectDetailsBO = BeanUtils.copyPropertiesReturnDest(new DefectDetailsBO(), defectDetailsReqDTO);
        defectsService.internalDefectDownload(response, defectDetailsBO);
        return GenericRspDTO.newSuccessInstance();
    }
}
