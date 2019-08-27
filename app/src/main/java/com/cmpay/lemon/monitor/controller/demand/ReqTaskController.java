package com.cmpay.lemon.monitor.controller.demand;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.DemandDTO;
import com.cmpay.lemon.monitor.dto.DemandReqDTO;
import com.cmpay.lemon.monitor.dto.DemandRspDTO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * @author: zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.REQTASK_PATH)
public class ReqTaskController {

    @Autowired
    private ReqTaskService reqTaskService;

    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<DemandRspDTO> findAll(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        String time = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        PageInfo<DemandBO> pageInfo = reqTaskService.find(demandBO);
        List<DemandBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandBO.class);

        for (int i = 0; i < demandBOList.size(); i++) {
            String reqAbnorType = demandBOList.get(i).getReq_abnor_type();
            String reqAbnorTypeAll = "";
            DemandBO demand = reqTaskService.findById(demandBOList.get(i).getReq_inner_seq());

            //当需求定稿时间、uat更新时间、测试完成时间、需求当前阶段、需求状态都不为空的时候，执行进度实时显示逻辑。
            if (StringUtils.isNotBlank(demand.getPrd_finsh_tm()) && StringUtils.isNotBlank(demand.getUat_update_tm())
                    && StringUtils.isNotBlank(demand.getTest_finsh_tm()) && StringUtils.isNotBlank(demand.getPre_cur_period())
                    && StringUtils.isNotBlank(demand.getReq_sts())) {
                //当前时间大于预计时间，并且所处阶段小于30,并且需求状态不为暂停或取消（30，40）,则该需求进度异常
                if (time.compareTo(demand.getPrd_finsh_tm()) > 0 && Integer.parseInt(demand.getPre_cur_period()) < 30
                        && "30".compareTo(demand.getReq_sts()) != 0 && "40".compareTo(demand.getReq_sts()) != 0) {
                    reqAbnorTypeAll += "需求进度滞后,";
                }
                if (time.compareTo(demand.getUat_update_tm()) > 0 && Integer.parseInt(demand.getPre_cur_period()) >= 30
                        && Integer.parseInt(demand.getPre_cur_period()) < 120 && "30".compareTo(demand.getReq_sts()) != 0
                        && "40".compareTo(demand.getReq_sts()) != 0) {
                    reqAbnorTypeAll += "开发进度滞后,";
                }
                if (time.compareTo(demand.getTest_finsh_tm()) > 0 && Integer.parseInt(demand.getPre_cur_period()) >= 120
                        && Integer.parseInt(demand.getPre_cur_period()) < 140 && "30".compareTo(demand.getReq_sts()) != 0
                        && "40".compareTo(demand.getReq_sts()) != 0) {
                    reqAbnorTypeAll += "测试进度滞后";
                }
                if (StringUtils.isBlank(reqAbnorTypeAll)) {
                    reqAbnorTypeAll += "正常";
                }
            } else if (reqAbnorType.indexOf("01") != -1) {
                demandBOList.get(i).setReq_abnor_type("正常");
                continue;
            } else {
                if (reqAbnorType.indexOf("03") != -1) {
                    reqAbnorTypeAll += "需求进度滞后,";
                }
                if (reqAbnorType.indexOf("04") != -1) {
                    reqAbnorTypeAll += "开发进度滞后,";
                }
                if (reqAbnorType.indexOf("05") != -1) {
                    reqAbnorTypeAll += "测试进度滞后";
                }
            }

            if (reqAbnorTypeAll.length() >= 1 && ',' == reqAbnorTypeAll.charAt(reqAbnorTypeAll.length() - 1)) {
                reqAbnorTypeAll = reqAbnorTypeAll.substring(0, reqAbnorTypeAll.length() - 1);
                demandBOList.get(i).setReq_abnor_type(reqAbnorTypeAll);
            } else {
                demandBOList.get(i).setReq_abnor_type(reqAbnorTypeAll);
            }
        }

        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandBOList, DemandDTO.class));
        rspDTO.setPageNum(pageInfo.getPageNum());
        rspDTO.setPages(pageInfo.getPages());
        rspDTO.setTotal(pageInfo.getTotal());
        rspDTO.setPageSize(pageInfo.getPageSize());

        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 查询需求信息
     *
     * @return
     */
    @RequestMapping("/info/{id}")
    public GenericRspDTO<DemandDTO> getInfoById(@PathVariable("req_inner_seq") String req_inner_seq, GenericDTO<NoBody> req) {
        DemandBO demandBO = reqTaskService.findById(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
    }

    /**
     * 添加需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/add")
    public GenericRspDTO add(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqTaskService.add(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 更新需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/update")
    public GenericRspDTO update(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqTaskService.update(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除/批量删除需求信息
     *
     * @param reqDTO
     * @return
     */
    @DeleteMapping("/delete")
    public GenericRspDTO delete(@RequestBody DemandReqDTO reqDTO) {
        reqTaskService.deleteBatch(reqDTO.getIds());
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
        doWrite("static/reqTask.xlsm", response);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public GenericRspDTO<NoBody> download(@RequestBody DemandReqDTO reqDTO, HttpServletResponse response) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        List<DemandDO> demandDOList = reqTaskService.getReqTask(demandBO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandDO.class, demandDOList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "reqTask_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 项目文档下载
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/downloadProFile")
    public GenericRspDTO<NoBody> downloadProFile(GenericDTO<NoBody> req, HttpServletRequest request, HttpServletResponse response) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);

        response.reset();
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)){
            Map<String, Object> resMap = reqTaskService.doBatchDown(file);

            File srcfile[] = (File[]) resMap.get("srcfile");

            //压缩包名称
            String zipPath = "/home/hims/temp/propkg/";
            String zipName = "项目文档_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".zip";

            //压缩文件
            File zip = new File(zipPath + zipName);
            reqTaskService.ZipFiles(srcfile, zip, true);

            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + zipName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            response.setContentType("application/octet-stream; charset=utf-8");

            output.write(org.apache.commons.io.FileUtils.readFileToByteArray(zip));
            bufferedOutPut.flush();

            // 删除文件
//            zip.delete();
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 导入
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        reqTaskService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }
}
