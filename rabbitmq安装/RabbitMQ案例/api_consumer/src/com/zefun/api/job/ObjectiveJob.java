package com.zefun.api.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zefun.api.mapper.EmployeeObjectiveMapper;
import com.zefun.api.service.RedisService;
import com.zefun.api.utils.DateUtil;

/**
* @author 张进军
* @date Aug 23, 2015 9:03:36 PM 
*/
public class ObjectiveJob {
    /** 日志对象 */
    private Logger logger = Logger.getLogger(ObjectiveJob.class);
    
    @Autowired
    private RedisService redisService;
    
    @Autowired
    private EmployeeObjectiveMapper employeeObjectiveMapper;
    
    /**
     * 定时器执行内容
    * @author 张进军
    * @date Aug 23, 2015 9:04:47 PM
     */
	public void execute() {
	    logger.info("ObjectiveJob execute start... ");
	    try {
	        List<Integer> storeList = employeeObjectiveMapper.selectAllStore();
	        for (int i = 0; i < storeList.size(); i++) {
	            selfMotionObjecttive(storeList.get(i));
	        }
        }
        catch (Exception e) {
            logger.error("ObjectiveJob execute error : ", e);
        }
	    logger.info("ObjectiveJob execute finish! ");
	}
	
	/**
     * 自动生成单月目标
    * @author 王大爷
    * @date 2015年11月18日 下午9:22:59
    * @param storeId 门店标识
     */
    public void selfMotionObjecttive(Integer storeId){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String objectiveMonth = dateFormat.format(new Date());
        String dates = DateUtil.getCurDateMinStr();
        Map<String, Object> deleteMap = new HashMap<String, Object>();
        deleteMap.put("storeId", storeId);
        deleteMap.put("objectiveMonth", objectiveMonth);
        employeeObjectiveMapper.deleteByStoreId(deleteMap);
        
        List<Integer> employeeIdList= employeeObjectiveMapper.getRecommendlist(storeId);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("objectiveMonth", objectiveMonth);
        map.put("createTime", dates);
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < employeeIdList.size(); i++) {
            map.put("employeeId", employeeIdList.get(i));
            Map<String, Object> listMap = new HashMap<String, Object>(map);
            list.add(listMap);
        } 
        if (list.size() > 0) {
            employeeObjectiveMapper.insertByList(list);
        }
    }
}

