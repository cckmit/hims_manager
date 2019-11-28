package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import com.cmpay.lemon.monitor.entity.ErcdmgUpdmgnDO;
import com.cmpay.lemon.monitor.entity.ErrorRecordBeanDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: ty
 */
@Mapper
public interface IErcdmgUpdmgnDao extends BaseDao<ErcdmgUpdmgnDO, String> {

    public ErcdmgUpdmgnDO searchSingleUpdmgn(String updateNo);


    public void delUpdmgn(String updateNo);

    public void delErrorLg(String updateNo);
    /**
     * 新增更新管理信息
     * @param ercdmgUpdmgn
     */
    void insertUpdmgn(ErcdmgUpdmgnDO ercdmgUpdmgn);
    /**
     *
     * @param updateNos
     * @return
     */
    List<ErcdmgUpdmgnDO> searchUpdmgnList(String[]updateNos);
    /**
     * 根据批次号更新更新管理表状态
     * @param updateNos
     */
    public void updateUpdmgnState(String[]updateNos);

    public void updateErrorState(String[]updateNos);

    void updateUpdmgnCount(Map<String,Object> map);

    public String selectUpdateCount(String updateNo);

}
