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


        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }

}
