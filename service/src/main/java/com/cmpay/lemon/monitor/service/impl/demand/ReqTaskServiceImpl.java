package com.cmpay.lemon.monitor.service.impl.demand;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.dao.IDemandExtDao;
import com.cmpay.lemon.monitor.dao.IDictionaryExtDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DictionaryDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cmpay.lemon.monitor.utils.FileUtils.importExcel;

/**
 * @author: zhou_xiong
 */
@Service
public class ReqTaskServiceImpl implements ReqTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqTaskServiceImpl.class);
    @Autowired
    private IDemandExtDao demandDao;
    @Autowired
    private IDictionaryExtDao dictionaryDao;
    /**
     * 自注入,解决getAppsByName中调用findAll的缓存不生效问题
     */
    @Autowired
    private ReqTaskService reqTaskService;

    @Override
    public DemandBO findById(String req_inner_seq) {
        DemandDO demandDO = demandDao.get(req_inner_seq);
        if (JudgeUtils.isNull(demandDO)) {
            LOGGER.warn("id为[{}]的记录不存在", req_inner_seq);
            BusinessException.throwBusinessException(MsgEnum.DB_FIND_FAILED);
        }
        return BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDO);
    }

    @Override
    public PageInfo<DemandBO> find(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        PageInfo<DemandBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(demandDao.find(demandDO), DemandBO.class));
        return pageInfo;
    }
    @Override
    public List<DemandDO> getReqTask(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        return  demandDao.getReqTask(demandDO);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void add(DemandBO demandBO) {
        //校验数据
        checkReqTask(demandBO);

        // 判断需求编号或需求名称是否重复
        List<DemandBO> list = reqTaskService.getReqTaskByUK(demandBO);
        if (list.size() > 0) {
            BusinessException.throwBusinessException(MsgEnum.NON_UNIQUE);
        }

        //设置默认值
        demandBO.getReq_type();
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        demandBO.setReq_impl_mon(month);
        demandBO.setReq_start_mon(month);
        demandBO.setReq_inner_seq(reqTaskService.getNextInnerSeq());
        demandBO.setQa_mng("刘桂娟");
        demandBO.setConfig_mng("黄佳海");
        demandBO.setReq_abnor_type("01");

        setDefaultValue(demandBO);
        setDefaultUser(demandBO);
        setReqSts(demandBO);
        try {
            demandDao.insert(BeanUtils.copyPropertiesReturnDest(new DemandDO(), demandBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void delete(String req_inner_seq) {
        try {
            demandDao.delete(req_inner_seq);
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void deleteBatch(List<String> ids) {
        try {
            ids.forEach(demandDao::delete);
        } catch (Exception e) {
            LOGGER.error("delete error:", e);
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(DemandBO demandBO) {
        //校验数据
        checkReqTask(demandBO);

        //设置默认值
        setDefaultValue(demandBO);
        setDefaultUser(demandBO);
        setReqSts(demandBO);

        try {
            demandDao.update(BeanUtils.copyPropertiesReturnDest(new DemandDO(), demandBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public List<DemandBO> findAll() {
        return BeanConvertUtils.convertList(demandDao.find(new DemandDO()), DemandBO.class);
    }

    @Override
    public List<DemandBO> getReqTaskByUK(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanUtils.copyPropertiesReturnDest(demandDO, demandBO);
        return BeanConvertUtils.convertList(demandDao.getReqTaskByUK(demandDO), DemandBO.class);
    }

    @Override
    public Boolean checkNumber(String req_no) {
        boolean bool = false;
        String[] reqNo = req_no.split("-");
        if (reqNo.length == 3){
            if ((("REQ".equals(reqNo[0]) || "REQJIRA".equals(reqNo[0]))
                    && reqNo[1].matches("^\\d{8}$") && reqNo[2].matches("^\\d{4}$"))){
                bool = true;
            }
        }
        return bool;
    }

    /**
     * 默认值设置
     *
     * @param demandBO
     */
    public void setDefaultValue(DemandBO demandBO) {
        // 计划投入数 =投入资源*开发周期/22
        int inpoyRes = demandBO.getInput_res();
        int devCycle = demandBO.getDev_cycle();
        Double expInput = new BigDecimal(new Double(inpoyRes) * new Double(devCycle))
                .divide(new BigDecimal(String.valueOf(22)), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //Double expInput = (double) (inpoyRes * devCycle / 22);
        demandBO.setExp_input(expInput);
    }

    /**
     * 设置用户
     *
     * @param demandBO
     */
    public void setDefaultUser(DemandBO demandBO) {
        if (StringUtils.isBlank(demandBO.getCreat_user())){
            demandBO.setCreat_user(SecurityUtils.getLoginUserId());
            demandBO.setCreat_time(new Date());
        }
        demandBO.setUpdate_user(SecurityUtils.getLoginUserId());
        demandBO.setUpdate_time(new Date());
    }

    /**
     * 变更需求状态
     *
     * @param demandBO
     */
    public void setReqSts(DemandBO demandBO) {
        if (!"30".equals(demandBO.getReq_sts()) && !"40".equals(demandBO.getReq_sts())) {
            //修改需求状态
            if ("10".equals(demandBO.getPre_cur_period())) {
                //提出
                demandBO.setReq_sts("10");
            } else if ("180".equals(demandBO.getPre_cur_period())) {
                //完成
                demandBO.setReq_sts("50");
            } else {
                //进行中
                demandBO.setReq_sts("20");
            }
        }
    }

    /**
     * 校验需求
     *
     * @param demandBO
     */
    public void checkReqTask(DemandBO demandBO) {
        //判断编号是否规范
        String reqNo = demandBO.getReq_no();
        if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)){
            BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
        }

        //如果选择取消或暂停，月初备注不能为空
        if("30".equals(demandBO.getReq_sts()) && StringUtils.isBlank(demandBO.getMon_remark())
                ||"40".equals(demandBO.getReq_sts()) && StringUtils.isBlank(demandBO.getMon_remark())){
            BusinessException.throwBusinessException(MsgEnum.NULL_REMARK);
        }

        //主导部门和配合部门不能相同
        if(StringUtils.isNotBlank(demandBO.getDevp_coor_dept())){
            String[] devp_coor_dept=demandBO.getDevp_coor_dept().split(",");
            for (int  i = 0; i < devp_coor_dept.length;i++){
                if(devp_coor_dept[i].equals(demandBO.getDevp_lead_dept())){
                    BusinessException.throwBusinessException(MsgEnum.ERROR_DEVP);
                }
            }
        }
    }


    @Override
    public String getNextInnerSeq() {
        // 从最大的值往后排
        DemandBO reqTask = getMaxInnerSeq();
        if (reqTask == null) {
            return "XQ00000001";
        } else {
            String maxInnerSeq = reqTask.getReq_inner_seq();
            if (StringUtils.isBlank(maxInnerSeq)) {
                return "XQ00000001";
            } else {
                int nextSeq = Integer.parseInt(maxInnerSeq.substring(2)) + 1;
                String innerSeq = StringUtils.leftPad(String.valueOf(nextSeq), 8, "0");
                return "XQ" + innerSeq;
            }
        }
    }

    @Override
    public DemandBO getMaxInnerSeq() {
        return BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDao.getMaxInnerSeq());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchImport(MultipartFile file) {
        List<DemandDO> demandDOS = importExcel(file, 0, 1, DemandDO.class);
        List<DemandDO> insertList = new ArrayList<>();
        List<DemandDO> updateList = new ArrayList<>();
        demandDOS.forEach(m -> {
            if (StringUtils.isBlank(m.getReq_pro_dept())){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"归属部门不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            if (StringUtils.isBlank(m.getReq_nm())){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"需求名称不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            if (StringUtils.isBlank(m.getReq_desc())){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"需求描述不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            //判断编号是否规范
            String reqNo = m.getReq_no();
            if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)){
                BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
            }

            DictionaryDO dictionaryDO = new DictionaryDO();
            dictionaryDO.setDic_id("PRD_LINE");
            dictionaryDO.setValue(m.getReq_prd_line());
            List<DictionaryDO> dic = dictionaryDao.getDicByDicId(dictionaryDO);
            if ( dic.size() == 0 ){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"产品线字典项不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            m.setReq_prd_line(dic.get(0).getName());

            dictionaryDO.setUser_name(m.getReq_proposer());
            dic = dictionaryDao.getJdInfo(dictionaryDO);
            if (dic.size() == 0){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"需求提出人不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            dictionaryDO.setUser_name(m.getReq_mnger());
            dic = dictionaryDao.getJdInfo(dictionaryDO);
            if (dic.size() == 0){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"需求负责人不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            if (StringUtils.isNotBlank(m.getPre_cur_period())) {
                dictionaryDO.setDic_id("REQ_PEROID");
                dictionaryDO.setValue(m.getPre_cur_period());
                dic = dictionaryDao.getDicByDicId(dictionaryDO);
                if ( dic.size() == 0 ){
                    MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"最新进展字典项不存在");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
                }
               m.setPre_cur_period(dic.get(0).getName());
            }


            if (StringUtils.isNotBlank(m.getPre_mon_period())) {
                dictionaryDO.setDic_id("REQ_PEROID");
                dictionaryDO.setValue(m.getPre_mon_period());
                dic = dictionaryDao.getDicByDicId(dictionaryDO);
                if ( dic.size() == 0 ){
                    MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"月初需求阶段字典项不存在");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
                }
                m.setPre_mon_period(dic.get(0).getName());
            }
            m.setPre_cur_period(StringUtils.isBlank(m.getPre_cur_period())?m.getPre_mon_period():m.getPre_cur_period());
            m.setPre_mon_period(StringUtils.isBlank(m.getPre_mon_period())?m.getPre_cur_period():m.getPre_mon_period());

            if (StringUtils.isNotBlank(m.getCur_mon_target())) {
                dictionaryDO.setDic_id("REQ_PEROID");
                dictionaryDO.setValue(m.getCur_mon_target());
                dic = dictionaryDao.getDicByDicId(dictionaryDO);
                if ( dic.size() == 0 ){
                    MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"本月预计完成阶段字典项不存在");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
                }
                m.setCur_mon_target(dic.get(0).getName());
            }

            DemandBO tmp= new DemandBO();
            BeanUtils.copyPropertiesReturnDest(tmp, m);
            setReqSts(tmp);
            setDefaultUser(tmp);
            BeanUtils.copyPropertiesReturnDest(m, tmp);

            List<DemandDO> dem = demandDao.getReqTaskByUKImpl(m);
            if (dem.size() == 0) {
                // 默认设置需求状态为新增
                m.setReq_type("01");
                // 默认设置qa人员为刘桂娟
                m.setQa_mng("刘桂娟");
                // 默认设置配置人员为黄佳海
                m.setConfig_mng("黄佳海");
                m.setReq_abnor_type("01");
                //设置默认值
                //插入SVN为否
                m.setIs_svn_build("否");
                insertList.add(m);
            }else {
                m.setReq_inner_seq(dem.get(0).getReq_inner_seq());
                //设置默认值
                m.setReq_start_mon("");
                updateList.add(m);
            }
        });

        try {
            // 插入数据库
            insertList.forEach(m -> {
                m.setReq_inner_seq(getNextInnerSeq());
                demandDao.insert(m);
            });

            // 更新数据库
            updateList.forEach(m -> {
                demandDao.update(m);
            });

        } catch (Exception e) {
            LOGGER.error("需求记录导入失败", e);
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public Map<String, Object> doBatchDown(MultipartFile file) {
        List<DemandDO> demandDOS = importExcel(file, 0, 1, DemandDO.class);
        List<DemandDO> List = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        demandDOS.forEach(m -> {
            if (StringUtils.isBlank(m.getReq_no())){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"需求编号不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            if (StringUtils.isBlank(m.getReq_nm())){
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo()+"需求名称不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            //判断编号是否规范
            String reqNo = m.getReq_no();
            if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)){
                BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
            }

            int start = m.getReq_no().indexOf("-")+1;
            String reqMonth = m.getReq_no().substring(start,start+6);
            m.setReq_start_mon(reqMonth);
            List.add(m);
        });

        //循环文件目录
        if (List != null) {
            File srcfile[]=new File[List.size()*5];
            int num = 0;
            //要压缩的文件
            for (int i=0;  i < List.size(); i++) {
                //需求说明书、技术方案、原子功能点评估表
                String path = "/home/hims/temp/Projectdoc/" + List.get(i).getReq_start_mon() + "/"
                        + List.get(i).getReq_no() + "_" + List.get(i).getReq_nm() ;

                File file1 = new File(path + "/开发技术文档/");
                if  (!file1 .exists()  && !file1 .isDirectory()){
                    file1 .mkdir();
                }
                File[] tempFile1 = file1.listFiles();
                if(tempFile1==null){
                    tempFile1= new File[0];
                }
                for(int j = 0; j < tempFile1.length; j++){
                    if(tempFile1[j].getName().contains("原子功能点评估表(电子工单)") || tempFile1[j].getName().contains("技术方案说明书")){
                        srcfile[num]=new File(path + "/开发技术文档/" + tempFile1[j].getName());
                        num++;
                    }
                }

                File file2 = new File(path + "/产品文档/");
                if  (!file2 .exists()  && !file2 .isDirectory()){
                    file2 .mkdir();
                }
                File[] tempFile2 = file2.listFiles();
                if(tempFile2==null){
                    tempFile2= new File[0];
                }
                for(int j = 0; j < tempFile2.length; j++){
                    if(tempFile2[j].getName().contains("需求方案说明书")){
                        srcfile[num]=new File(path + "/产品文档/"+ tempFile2[j].getName());
                        num++;
                    }
                }

            }
            map.put("srcfile", srcfile);
        }

        return map;
    }

    /**
      文件压缩
     */
    public String ZipFiles(File[] srcfile, File zipfile, boolean flag){
        try {
            byte[] buf=new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream(zipfile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);

            for (int i=0; i < srcfile.length; i++) {
                if (srcfile[i] != null) {
                    FileInputStream in=new FileInputStream(srcfile[i]);
                    if(flag){
                        String demandName = srcfile[i].getPath().substring(34,srcfile[i].getPath().length());
                        String name = demandName.substring(0,demandName.indexOf("/"));
                        String  path = demandName.substring(demandName.lastIndexOf("/")+1);
                        out.putNextEntry(new ZipEntry(name+"/"+path));
                    }else{
                        out.putNextEntry(new ZipEntry(srcfile[i].getPath()));
                    }

                    int len;
                    while ((len=in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
            out.close();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }

        return "";
    }


}
