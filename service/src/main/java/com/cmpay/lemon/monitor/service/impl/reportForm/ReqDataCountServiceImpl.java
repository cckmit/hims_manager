package com.cmpay.lemon.monitor.service.impl.reportForm;

import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ReqDataCountBO;
import com.cmpay.lemon.monitor.bo.ReqMngBO;
import com.cmpay.lemon.monitor.dao.IReqDataCountDao;
import com.cmpay.lemon.monitor.entity.ReqDataCountDO;
import com.cmpay.lemon.monitor.entity.ReqMngDO;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.DateUtil;


import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(rollbackFor = Exception.class)
@Service
public class ReqDataCountServiceImpl implements ReqDataCountService {

	@Autowired
	private IReqDataCountDao reqDataCountDao;

	@Autowired
	private ReqPlanService reqPlanService;
	
	/**
	 * 本月需求完成情况：进行中、已投产需求数
	 */
	@Override
	public Map getProg(String report_start_mon ,String report_end_mon) {
		Map DataMap = new TreeMap();
		
		ReqDataCountDO reqDataCountDO = reqDataCountDao.getProg(report_start_mon, report_end_mon);

	    if ((reqDataCountDO == null)) {
	      return DataMap;
	    }
	    
	    DataMap.put("已投产", reqDataCountDO.getReqOper());
	    DataMap.put("进行中", reqDataCountDO.getReqIng());

		return DataMap;
	}
	
	/**
	 * 本月需求进度情况：需求阶段、开发阶段、测试阶段、预投产阶段、投产上线阶段
	 */
	@Override
	public Map getProgDetl( String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();
		
		ReqDataCountDO reqDataCountD0 = reqDataCountDao.getProgDetl(report_start_mon, report_end_mon);

	    if ((reqDataCountD0 == null)) {
	      return DataMap;
	    }
	    
	    DataMap.put("需求阶段", reqDataCountD0.getReqPrd());
	    DataMap.put("开发阶段", reqDataCountD0.getReqDevp());
	    DataMap.put("测试阶段", reqDataCountD0.getReqTest());
	    DataMap.put("预投产阶段", reqDataCountD0.getReqPre());
	    DataMap.put("已投产", reqDataCountD0.getReqOper());

		return DataMap;
	}

	/**
	 * 按部门统计需求实施异常图表
	 */
	@Override
	public Map getAbnoByDept(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();
		
		List<ReqDataCountDO> list = reqDataCountDao.getAbnoByDept(report_start_mon, report_end_mon);

	    if ((list == null)) {
	      return DataMap;
	    }
	    
	    for (int i = 0; i < list.size(); i++) {
	    	DataMap.put(list.get(i).getDevpLeadDept(), list.get(i).getReqUnusual());
		}
	    
		return DataMap;
	}

	/**
	 * 按产品线统计需求实施异常图表
	 */
	@Override
	public Map getAbnoByLine(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();

		List<ReqDataCountDO> list = reqDataCountDao.getAbnoByLine(report_start_mon, report_end_mon);

	    if ((list == null)) {
	      return DataMap;
	    }
	    
	    for (int i = 0; i < list.size(); i++) {
	    	DataMap.put(list.get(i).getReqPrdLine(), list.get(i).getReqUnusual());
		}

		return DataMap;
	}

	/**
	 * 各部门需求完成情况：进行中、已投产需求数（饼图）
	 */
	@Override
	public Map getProgByDept(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();
		
		List<ReqDataCountDO> list = reqDataCountDao.getProgByDept(report_start_mon, report_end_mon);

	    if ((list == null)) {
	      return DataMap;
	    }
	    
	    for (int i = 0; i < list.size(); i++) {
	    	Map Data = new TreeMap();
	    	Data.put("已投产", list.get(i).getReqOper());
	    	Data.put("进行中", list.get(i).getReqIng());
	    	
	    	DataMap.put(list.get(i).getDevpLeadDept(), Data);
		}
	    
		return DataMap;
	}

	/**
	 * 各部门需求进度情况：需求阶段、开发阶段、测试阶段、预投产阶段、投产上线阶段（饼图）
	 */
	@Override
	public Map getProgDetlByDept(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();
		
		List<ReqDataCountDO> list = reqDataCountDao.getProgDetlByDept(report_start_mon, report_end_mon);

	    if ((list == null)) {
	      return DataMap;
	    }
	    
	    for (int i = 0; i < list.size(); i++) {
	    	Map Data = new TreeMap();
	    	Data.put("需求阶段", list.get(i).getReqPrd());
	    	Data.put("开发阶段", list.get(i).getReqDevp());
	    	Data.put("测试阶段", list.get(i).getReqTest());
	    	Data.put("预投产阶段", list.get(i).getReqPre());
	    	Data.put("已投产", list.get(i).getReqOper());
	    	
	    	DataMap.put(list.get(i).getReqPrdLine(), Data);
		}
	    

		return DataMap;
	}
	
	
	/**
	 * map转json
	 * @param DataMap
	 * @return jsonStr
	 */
	public String strToJson(Map DataMap){
		String bigJsonStr = "";
		
		Set treeSet = new TreeSet();
	    treeSet = DataMap.keySet();

	    String smallJson = "";

	    StringBuilder bigJson = new StringBuilder();

	    for (Iterator localIterator = treeSet.iterator(); localIterator.hasNext(); ) { String amtSrc = (String)localIterator.next();

	      smallJson = new StringBuilder().append("{\"value\":\"").append((String)DataMap.get(amtSrc)).append("\",\"name\":\"").append(amtSrc).append("\"},").toString();
	      bigJson.append(smallJson);
	    }

	    bigJsonStr = bigJson.toString();

	    bigJsonStr = new StringBuilder().append("{\"LineData\":[").append(bigJsonStr.substring(0, bigJsonStr.length() - 1)).append("]}").toString();

	    return bigJsonStr;
		
	}

	@Override
	public List<ReqDataCountBO> getImpl(String ReqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		List<ReqDataCountDO> impl = reqDataCountDao.getImpl(ReqImplMon);
		impl.forEach(m->
						reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
				);
		System.out.println(reqDataCountBOS.toString());
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getImplByDept(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getImplByDept(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		System.out.println(reqDataCountBOS.toString());
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getComp(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		List<ReqDataCountDO> comp = reqDataCountDao.getComp(reqImplMon);
		comp.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getCompByDept(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getCompByDept(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);

		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getReqSts(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		List<ReqDataCountDO> impl = reqDataCountDao.getReqSts(reqImplMon);
		impl.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getStageByJd(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getStageByJd(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> selectDetl(ReqMngBO vo) {
		RowBounds rb = new RowBounds(vo.getStartIndex(), vo.getPageSize());
		List<ReqDataCountDO> lst = reqDataCountDao.selectDetl(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo),BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), rb));
		String time= DateUtil.date2String(new Date(), "yyyy-MM-dd");
		for (int i =0; i < lst.size();i++){
			String reqAbnorType=lst.get(i).getReqUnusual();
			DemandBO demand = reqPlanService.findById(lst.get(i).getReqInnerSeq());
			String reqAbnorTypeAll = "";
			//当需求定稿时间、uat更新时间、测试完成时间、需求当前阶段、需求状态都不为空的时候，执行进度实时显示逻辑。
			if (StringUtils.isNotBlank(demand.getPrdFinshTm()) && StringUtils.isNotBlank(demand.getUatUpdateTm()) && StringUtils.isNotBlank(demand.getTestFinshTm()) && StringUtils.isNotBlank(demand.getPreCurPeriod()) && StringUtils.isNotBlank(demand.getReqSts())) {
				//当前时间大于预计时间，并且所处阶段小于30,并且需求状态不为暂停或取消（30，40）,则该需求进度异常
				if (time.compareTo(demand.getPrdFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) < 30 && "30".compareTo(demand.getReqSts()) != 0 && "40".compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "需求进度滞后,";
				}
				if (time.compareTo(demand.getUatUpdateTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= 30 && Integer.parseInt(demand.getPreCurPeriod()) < 120 && "30".compareTo(demand.getReqSts()) != 0 && "40".compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "开发进度滞后,";
				}
				if (time.compareTo(demand.getTestFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= 120 && Integer.parseInt(demand.getPreCurPeriod()) < 140 && "30".compareTo(demand.getReqSts()) != 0 && "40".compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "测试进度滞后";
				}
				if (StringUtils.isBlank(reqAbnorTypeAll)) {
					reqAbnorTypeAll += "正常";
				}
			} else if(reqAbnorType.indexOf("01") != -1){
				lst.get(i).setReqUnusual("正常");
				continue;
			}else{
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
			if (reqAbnorTypeAll.length() >= 1 && ',' == reqAbnorTypeAll.charAt(reqAbnorTypeAll.length()-1)){
				reqAbnorTypeAll = reqAbnorTypeAll.substring(0,reqAbnorTypeAll.length()-1);
				lst.get(i).setReqUnusual(reqAbnorTypeAll);
			}else{
				lst.get(i).setReqUnusual(reqAbnorTypeAll);
			}
		}
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		lst.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}
	
	@Override
	public Integer findNumberByCondition(ReqMngBO vo) throws Exception {
		return reqDataCountDao.findNumberByCondition(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));
	}

	@Override
	public Map selectByCenter(ReqMngBO vo) {
		Map reMap = new TreeMap();
		Map DataMap = new TreeMap();
		
		ReqDataCountDO reqDataCount = reqDataCountDao.selectByCenter(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));

	    if ((reqDataCount == null)) {
	      return reMap;
	    }
	    
	    DataMap.put("需求阶段", reqDataCount.getReqPrd());
	    DataMap.put("开发阶段", reqDataCount.getReqDevp());
	    DataMap.put("测试阶段", reqDataCount.getReqTest());
	    DataMap.put("预投产阶段", reqDataCount.getReqPre());
	    DataMap.put("已投产", reqDataCount.getReqOper());
		DataMap.put("需求异常", reqDataCount.getReqAbnormal());
	    DataMap.put("取消需求", reqDataCount.getReqCancel());
	    DataMap.put("暂停需求", reqDataCount.getReqSuspend());
	    
	    reMap.put("DataMap", DataMap);
	    reMap.put("totle", reqDataCount.getTotal());
	    
		return reMap;
	}

	@Override
	public Map selectByProduct(ReqMngBO vo) {
		Map reMap = new TreeMap();
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCount = reqDataCountDao.selectByProduct(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));

	    if ((reqDataCount == null)) {
	      return reMap;
	    }

	    DataMap.put("需求分析", reqDataCount.getReqPrd());
	    DataMap.put("需求撰写", reqDataCount.getReqDevp());
	    DataMap.put("需求定稿", reqDataCount.getReqTest());
	    DataMap.put("预投产测试", reqDataCount.getReqPre());
		DataMap.put("需求进度异常", reqDataCount.getReqAbnormal());
	    DataMap.put("取消需求", reqDataCount.getReqCancel());
	    DataMap.put("暂停需求", reqDataCount.getReqSuspend());
	    
	    reMap.put("DataMap", DataMap);
	    reMap.put("totle", reqDataCount.getTotal());
	    
		return reMap;
	}

	@Override
	public Map selectByTest(ReqMngBO vo) {
		Map reMap = new TreeMap();
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCount = reqDataCountDao.selectByTest(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));

	    if ((reqDataCount == null)) {
	      return reMap;
	    }

	    DataMap.put("未启动测试", reqDataCount.getReqPrd());
	    DataMap.put("测试进行中", reqDataCount.getReqDevp());
	    DataMap.put("已完成测试", reqDataCount.getReqTest());
		DataMap.put("测试异常", reqDataCount.getReqAbnormal());
	    DataMap.put("取消需求", reqDataCount.getReqCancel());
	    DataMap.put("暂停需求", reqDataCount.getReqSuspend());
	    
	    reMap.put("DataMap", DataMap);
	    reMap.put("totle", reqDataCount.getTotal());
	    
		return reMap;
	}

	@Override
	public Map selectByEng(ReqMngBO vo) {
		// TODO Auto-generated method stub
		return null;
	}
}
