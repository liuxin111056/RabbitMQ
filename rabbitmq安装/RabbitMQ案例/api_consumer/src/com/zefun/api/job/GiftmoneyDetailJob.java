package com.zefun.api.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.zefun.api.mapper.EmployeeObjectiveMapper;
import com.zefun.api.service.RedisService;

/**
* @author 张进军
* @date Aug 23, 2015 9:03:36 PM 
*/
public class GiftmoneyDetailJob {
    /** 日志对象 */
//    private Logger logger = Logger.getLogger(GiftmoneyDetailJob.class);
    
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
//	    //当期时间为今天时
//	    Integer isWin = employeeObjectiveMapper.updateGiftmoneyAll(DateUtil.getCurDate());
//	    
//	    if (isWin != 0) {
//	        employeeObjectiveMapper.deleteGiftmoneyDetail(DateUtil.getCurDate());
//	    }
//	    
//	    employeeObjectiveMapper.updatePastdataGiftmoneyDetail(DateUtil.getCurDate());
	}
	
}

