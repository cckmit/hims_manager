package com.cmpay.lemon.monitor.controller.systemOperation;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.OperationApplicationDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = MonitorConstants.SYSTEMOPERATION_PATH)
public class systemOperationController {
    @Autowired
    private OperationApplicationService operationApplicationService;

    @RequestMapping("/entry")
    public GenericRspDTO<NoBody> systemOperationEntry(OperationApplicationDTO operationApplicationDTO, HttpServletRequest request) {
        List<MultipartFile> files=null;
        //判断是否带附件
        if(operationApplicationDTO.getAttachment()!=null&&operationApplicationDTO.getAttachment().equals("true")) {
            files = ((MultipartHttpServletRequest) request).getFiles("file");
            files.forEach(m->{
                System.err.println(m.getOriginalFilename());
            });
        }
        OperationApplicationBO operationApplicationBO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationBO(), operationApplicationDTO);

        operationApplicationService.systemOperationEntry(files,operationApplicationBO,request);

        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<OperationApplicationRspDTO> getUserInfoPage(@RequestBody OperationApplicationReqDTO reqDTO) {
        if(reqDTO.getPoDateStart()!=null && !reqDTO.getPoDateStart().equals("") && (reqDTO.getPoDateEnd()==null || reqDTO.getPoDateEnd().equals(""))){
            reqDTO.setProposeDate(reqDTO.getPoDateStart());
        }
        if(reqDTO.getPoDateEnd()!=null && !reqDTO.getPoDateEnd().equals("") && (reqDTO.getPoDateStart()==null || reqDTO.getPoDateStart().equals(""))){
            reqDTO.setProposeDate(reqDTO.getPoDateEnd());
        }
        OperationApplicationBO operationApplicationBO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationBO(), reqDTO);
        OperationApplicationRspBO productionRspBO = operationApplicationService.find(operationApplicationBO);
        OperationApplicationRspDTO rspDTO = new OperationApplicationRspDTO();
        rspDTO.setOperationApplicationList(BeanConvertUtils.convertList(productionRspBO.getProductionList(), OperationApplicationDTO.class));
        rspDTO.setPageNum(productionRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

}
