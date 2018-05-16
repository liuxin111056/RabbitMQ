package com.zefun.api.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zefun.api.entity.EmployeeObjective;

/**
 * 人员目标
* @author chendb
* @date 2015年8月17日 下午3:35:58
 */
public interface EmployeeObjectiveMapper {
    
    /**
     * 修改员工业绩目标表
    * @author 王大爷
    * @date 2015年11月17日 下午8:20:04
    * @param employeeObjective 参数
    * @return 是否成功
     */
    Integer updateActual(EmployeeObjective employeeObjective);
    
    /**
     * 查询该员工服务步骤对应的业绩
    * @author 王大爷
    * @date 2015年11月20日 下午3:10:41
    * @param shiftMahjongStepId 轮牌步骤
    * @return BigDecimal
     */
    BigDecimal selectByCommonCalculate(Integer shiftMahjongStepId);
    
    /**
     * 用套餐抵扣时，对应的套餐业绩，提成计算
    * @author 王大爷
    * @date 2015年11月20日 下午3:55:41
    * @param map 参数
    * @return Map<String, Object>
     */
    BigDecimal selectByComboCommonCalculate(Map<String, Integer> map);
    
    /**
     * 查询所有连锁店、单店
    * @author 王大爷
    * @date 2015年11月20日 下午5:51:12
    * @return List<Integer>
     */
    List<Integer> selectAllStore();
    
    /**
     * 查询门店下所有员工标识
    * @author 王大爷
    * @date 2015年11月20日 下午6:24:28
    * @param storeId 门店标识
    * @return List<Integer>
     */
    List<Integer> getRecommendlist(Integer storeId);
    
    /**
     * 批量保存
    * @author 王大爷
    * @date 2015年11月20日 下午6:27:53
    * @param list 参数
    * @return int
     */
    int insertByList(List<Map<String, Object>> list);
    
    /**
     * 根据门店标识删除员工当月目标
    * @author 王大爷
    * @date 2015年11月20日 下午7:06:25
    * @param map 参数
    * @return 是否成功
     */
    int deleteByStoreId(Map<String, Object> map);
    
    /**
     * 会员账户礼金刷新
    * @author 王大爷
    * @date 2015年11月21日 下午4:02:57
    * @param startDate 开始时间
    * @return 是否成功
     */
    Integer updateGiftmoneyAll(String startDate);
    
    /**
     * 为礼金明细表打删除标识
    * @author 王大爷
    * @date 2015年11月21日 下午4:08:34
    * @param startDate 开始时间
    * @return 是否成功
     */
    Integer deleteGiftmoneyDetail(String startDate);
    
    /**
     * 刷新过期礼金
    * @author 王大爷
    * @date 2015年11月21日 下午6:49:26
    * @param endDate 结束时间
    * @return 是否成功
     */
    Integer updatePastdataGiftmoneyDetail(String endDate);
    
    /**
     * 查询门店设置
    * @author 王大爷
    * @date 2015年11月25日 下午9:23:25
    * @param storeId 门店标识
    * @return Map<String, Object>
     */
    Map<String, Object> selectStoreSetting(Integer storeId);
    
    /**
     * 查询该员工在同一个明细中步骤个数
    * @author 王大爷
    * @date 2015年11月26日 上午11:25:33
    * @param map 参数
    * @return Integer
     */
    Integer selectCount(Map<String, Integer> map);
}