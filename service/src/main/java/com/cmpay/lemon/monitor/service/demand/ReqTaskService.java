package com.cmpay.lemon.monitor.service.demand;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface ReqTaskService {
    /**
     * 通过id查找记录
     *
     * @param req_inner_seq
     * @return
     */
    DemandBO findById(String req_inner_seq);

    /**
     * 分页查询
     *
     * @param demandBO
     * @return
     */
    PageInfo<DemandBO> find(DemandBO demandBO);

    /**
     * 新增
     *
     * @param demandBO
     */
    void add(DemandBO demandBO);

    /**
     * 删除
     *
     * @param req_inner_seq
     */
    void delete(String req_inner_seq);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteBatch(List<String> ids);

    /**
     * 修改
     *
     * @param demandBO
     */
    void update(DemandBO demandBO);

    /**
     * 查找所有
     *
     * @return
     */
    List<DemandBO> findAll();

    /**
     * 校验需求编号
     *
     * @return
     */
    Boolean checkNumber(String req_no) ;

    /**
     * 按需求编号需求名称查找
     * @param
     */
    List<DemandBO> getReqTaskByUK(DemandBO demandBO);

    /**
     * 获取下一个内部用户号
     * @param
     */
    String getNextInnerSeq();

    /**
     * 查找最大内部用户号
     * @param
     */
    DemandBO getMaxInnerSeq();

    /**
     * 批量导入
     *
     * @param file
     */
    void doBatchImport(MultipartFile file);
}
