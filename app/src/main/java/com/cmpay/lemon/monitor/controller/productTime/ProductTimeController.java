package com.cmpay.lemon.monitor.controller.productTime;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.ProductionTimeBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.ProductionTimeDTO;
import com.cmpay.lemon.monitor.dto.ProductionTimeReqDTO;
import com.cmpay.lemon.monitor.dto.ProductionTimeRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.impl.timer.ReqMonitorTimer;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zouxin on 2018/9/10.
 */
@RestController
@RequestMapping(value = MonitorConstants.PRODUCTTIME)
public class ProductTimeController {

    @Autowired
    private ProductTimeService productTimeService;
    @Autowired
    private ReqMonitorTimer reqMonitorTimer;
    @RequestMapping("/list")
    public GenericRspDTO<ProductionTimeRspDTO> list(GenericDTO<NoBody> req) {
        ProductionTimeRspDTO productionTimeRspDTO = new ProductionTimeRspDTO();
        // --------- 查询
        List<ProductionTimeBO> productTimeBOList = productTimeService.productTimeList();
        LinkedList<ProductionTimeDTO> productTimeDTOList = new LinkedList<>();
        productTimeBOList.forEach(m -> {
            productTimeDTOList.add(BeanUtils.copyPropertiesReturnDest(new ProductionTimeDTO(), m));
        });
        productionTimeRspDTO.setProductTimes(productTimeDTOList);
        // 设置投产日时间范围
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 设置投产日最小值
        productionTimeRspDTO.setMinProDate(sdf.format(cal.getTime()));
        cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
        // 设置投产日最大值
        productionTimeRspDTO.setMaxProDate(sdf.format(cal.getTime()));
        reqMonitorTimer.getIssueModifiedWithinOneDay();

        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, productionTimeRspDTO);
    }

    @RequestMapping("/updateProductTime")
    public GenericRspDTO update(@RequestBody ProductionTimeReqDTO productionTimeReqDTO) {
        List<ProductionTimeDTO> productTime = productionTimeReqDTO.getProductTime();
        if (!productTime.isEmpty()) {
            productTime.forEach(m -> {
                productTimeService.updateProductTime(BeanUtils.copyPropertiesReturnDest(new ProductionTimeBO(), m));
            });
        }
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
}
