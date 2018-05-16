package com.zefun.api.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zefun.api.service.RedisService;
import com.zefun.api.utils.App;

/**
 * 员工考勤定时任务
* @author 张进军
* @date Dec 6, 2015 2:45:29 PM 
*/
public class EmployeeAttendanceJob {
    
    /** 日志对象 */
    private Logger logger = Logger.getLogger(EmployeeAttendanceJob.class);
    
    /** 缓存服务对象 */
    @Autowired
    private RedisService redisService;

    /**
     * 考勤处理函数
    * @author 张进军
    * @date Dec 6, 2015 2:50:29 PM
     */
    public void execute() {
        logger.info("EmployeeAttendanceJob execute start...");
        redisService.del(App.Redis.EMPLOYEE_ATTENDANCE_STATUS_HASH);
        logger.info("EmployeeAttendanceJob execute finish...");
    }
}
