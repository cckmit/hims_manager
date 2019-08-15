package com.cmpay.lemon.monitor.service.impl.dic;

import com.cmpay.lemon.monitor.bo.DictionaryBO;
import com.cmpay.lemon.monitor.dao.IDictionaryExtDao;
import com.cmpay.lemon.monitor.entity.DictionaryDO;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhou_xiong
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS,rollbackFor = RuntimeException.class)
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private IDictionaryExtDao dictionaryDao;

    @Override
    public DictionaryBO getDicByDicId(DictionaryDO dictionaryDO) {
        List<DictionaryDO> dictionaryDOList = dictionaryDao.getDicByDicId(dictionaryDO);
        List<DictionaryBO> dictionaryBOList = BeanConvertUtils.convertList(dictionaryDOList, DictionaryBO.class);
        DictionaryBO dictionaryBO = new DictionaryBO();
        dictionaryBO.setDictionaryBOList(dictionaryBOList);
        return dictionaryBO;
    }

    @Override
    public DictionaryBO getJdInfo() {
        List<DictionaryDO> dictionaryDOList = dictionaryDao.getJdInfo();
        List<DictionaryBO> dictionaryBOList = BeanConvertUtils.convertList(dictionaryDOList, DictionaryBO.class);
        DictionaryBO dictionaryBO = new DictionaryBO();
        dictionaryBO.setDictionaryBOList(dictionaryBOList);
        return dictionaryBO;
    }
}
