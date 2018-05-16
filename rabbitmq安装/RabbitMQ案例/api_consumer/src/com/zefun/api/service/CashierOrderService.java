package com.zefun.api.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zefun.api.dto.CashierCommissionDto;
import com.zefun.api.dto.CashierOrderDetailDto;
import com.zefun.api.entity.EmployeeCommission;
import com.zefun.api.entity.EmployeeObjective;
import com.zefun.api.entity.OrderDetail;
import com.zefun.api.mapper.EmployeeObjectiveMapper;
import com.zefun.api.mapper.OrderDetailMapper;
import com.zefun.api.utils.DateUtil;

/**
 * 自助收银-店员提成计算
 *
 */
@Service
public class CashierOrderService {
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired EmployeeObjectiveMapper employeeObjectiveMapper;
	
	/**
	 * 计算并添加员工提成 
	 * @param orderId 订单标识
	 * 
	 */
	@Transactional
	public void insertCashierCommission(Integer orderId) {
		List<CashierOrderDetailDto> orderDetails = orderDetailMapper.selectOrderDetailByOrderId(orderId);
		if(orderDetails == null || orderDetails.isEmpty()) {
			return;
		}
		Integer memberIdConsumption = orderDetailMapper.selectMemberIdByOrderId(orderId);
		//还有问题
		//计算会员账户均值
		if (memberIdConsumption != null) {
		    orderDetailMapper.updateAvgConsume(memberIdConsumption);
		    List<String> createTimeList = orderDetailMapper.selectCreateTime(memberIdConsumption);
		    
		    int avgConsumeDays = 0;
		    Integer num = createTimeList.size();
		    int totalNum = 0;
		    if (num > 1) {
		        for (int i = 1; i < createTimeList.size(); i++) {
	                String smdate = createTimeList.get(i);
	                String bdate = createTimeList.get(i - 1);
	                try {
                        int daysBetween = DateUtil.daysBetween(smdate, bdate);
                        totalNum = totalNum + daysBetween;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
	            }
		        avgConsumeDays = totalNum / (num - 1);
		    }
		    Map<String, Integer> map = new HashMap<String, Integer>();
		    map.put("avgConsumeDays", avgConsumeDays);
		    map.put("memberId", memberIdConsumption);
		    orderDetailMapper.updateAvgConsumeDays(map);
		}
		
		Map<String, Integer> commissionParams = null;
		CashierCommissionDto commissionDto = null;
		int length = orderDetails.size();
		List<Integer> detailIds = new ArrayList<Integer>(length);
		List<Integer> projectIds = new ArrayList<Integer>(length);
		List<Integer> projectCount = new ArrayList<Integer>(length);
		List<Integer> projectPeople = new ArrayList<Integer>(length);
		List<Integer> goodsIds = new ArrayList<Integer>(length);
		List<Integer> goodsCount = new ArrayList<Integer>(length);
		List<Integer> goodsPeople = new ArrayList<Integer>(length);
		List<Integer> comboIds = new ArrayList<Integer>(length);
		List<Integer> comboCount = new ArrayList<Integer>(length);
		List<Integer> comboPeople = new ArrayList<Integer>(length);
		Integer detailId = null;
		Integer projectId = null;
		Integer employeeId = null;
		Integer orderType = null;
		Integer salesCount = null;
		Integer index = null;
		// 计算提成
		for(CashierOrderDetailDto orderDetail : orderDetails) {
			detailId = orderDetail.getDetailId();
			
			OrderDetail obj = orderDetailMapper.selectByDetailId(detailId);
			
			commissionParams = new HashMap<String, Integer>();
			projectId = orderDetail.getProjectId();
			commissionParams.put("projectId", projectId);
			orderType = orderDetail.getOrderType();
			switch (orderType) {
				case 1 : // 项目
				    //查询轮牌标识
				    Integer shiftMahjongId = orderDetailMapper.selectByShiftMahjongStepId(orderDetail.getShiftMahjongStepId());
				    
					commissionParams.put("levelId", orderDetail.getLevelId());
					commissionParams.put("isAssign", orderDetail.getIsAssign());
					commissionParams.put("shiftMahjongId", shiftMahjongId);
					
					commissionDto = orderDetailMapper.selectProjectCommissionInfo(commissionParams);
					
					if(commissionDto != null) {
						employeeId = orderDetail.getEmployeeId();
						// 计算项目提成
						calculateCommonCommission(obj, orderDetail.getShiftMahjongStepId(), orderDetail.getProjectCount(), orderType, employeeId, commissionDto, orderDetail.getIsAppoint());
						if(detailIds.contains(detailId)) {
							continue;
						}
						detailIds.add(detailId);
						if(!projectIds.contains(projectId)) {
							projectIds.add(projectId);
							projectCount.add(1);
							projectPeople.add(1);
						} else {
							index = projectIds.indexOf(projectId);
							salesCount = projectCount.get(index);
							projectCount.set(index, salesCount + 1);
						}
					}
					break;
				case 2 : // 商品
					commissionDto = orderDetailMapper.selectGoodsCommissionInfo(commissionParams);
					if(commissionDto != null) {
						employeeId = orderDetail.getLastOperatorId();
						// 计算商品提成
						calculateCommonCommission(obj, null, orderDetail.getProjectCount(), orderType, employeeId, commissionDto, 2);
						if(!goodsIds.contains(projectId)) {
							goodsIds.add(projectId);
							goodsCount.add(orderDetail.getProjectCount());
							goodsPeople.add(1);
						} else {
							index = goodsIds.indexOf(projectId);
							salesCount = goodsCount.get(index);
							goodsCount.set(index, salesCount + orderDetail.getProjectCount());
						}
						
					}
					break;
				case 3 : // 套餐
					commissionDto = orderDetailMapper.selectComboCommissionInfo(commissionParams);
					if(commissionDto != null) {
						if(!comboIds.contains(projectId)) {
							employeeId = orderDetail.getLastOperatorId();
							Integer memberId = orderDetail.getMemberId();
							insertMemberComboInfo(projectId, employeeId, memberId);
							// 计算套餐提成
							calculateCommonCommission(obj, null, orderDetail.getProjectCount(), orderType, employeeId, commissionDto, 2);
							if(!comboIds.contains(projectId)) {
								comboIds.add(projectId);
								comboCount.add(orderDetail.getProjectCount());
								comboPeople.add(1);
							} else {
								index = comboIds.indexOf(projectId);
								salesCount = comboCount.get(index);
								comboCount.set(index, salesCount + orderDetail.getProjectCount());
							}
						}
					}
					break;
				default :
					break;
			}
		}
		
		// 修改项目信息表，更新累计人次
		if(!projectIds.isEmpty()) {
			addProjectSalesInfo(projectIds, projectCount, projectPeople);
		}
		// 修改商品信息表，更新累计人次
		if(!goodsIds.isEmpty()) {
			addGoodsSalesInfo(goodsIds, goodsCount, goodsPeople);
		}
		// 修改套餐信息表，更新累计人次
		if(!comboIds.isEmpty()) {
			addComboSalesInfo(comboIds, comboCount, comboPeople);
		}
		
	}
	
	/**
	 * 计算订单的提成
	 * @param detailId 详情标识
	 * @param orderType 订单类型 1.项目 2.商品 3.套餐
	 * @param shiftMahjongStepId 轮牌步骤标识
	 * @param comboId 套餐标识
	 * @param count 数量
	 * @param employeeId 员工标识
	 * @param commissionDto 提成对象
	 * @param isAppoint 是否预约
	 */
	protected void calculateCommonCommission(OrderDetail orderDetail, Integer shiftMahjongStepId, 
	        Integer count, Integer orderType, Integer employeeId, 
	        CashierCommissionDto commissionDto, Integer isAppoint) {
	    
	    BigDecimal hundred = new BigDecimal(100);
	    
	    Map<String, Object> storeSettingMap = employeeObjectiveMapper.selectStoreSetting(orderDetail.getStoreId());
	    //提成是否扣除成本(0:不扣除，1:扣除)
	    Integer costCommissionType = Integer.valueOf(storeSettingMap.get("costCommissionType").toString());
	    //礼金减扣比例
	    Integer giftCommissionRate = Integer.valueOf(storeSettingMap.get("giftCommissionRate").toString());
	    //优惠券减扣比例
	    Integer couponCommissionRate = Integer.valueOf(storeSettingMap.get("couponCommissionRate").toString());
	    
	    // 员工提成
        BigDecimal empCommission = null;
        // 提成方式
        Integer commissionType = null;
        // 提成金额
        BigDecimal commissionAmount =  null;
        // 业绩计算
        BigDecimal commonCalculate = null;
	    /*//套餐计算比例
	    BigDecimal comboPerformanceCal = null;*/
        if (orderDetail.getComboId() != null) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("comboId", orderDetail.getComboId());
            map.put("projectId", orderDetail.getProjectId());
            //套餐计算比例
            commonCalculate = employeeObjectiveMapper.selectByComboCommonCalculate(map);
            /*commonCalculate = comboPerformanceCal.multiply(orderDetail.getDiscountAmount()).divide(hundred);*/
        }
        else if (orderDetail.getCouponId() != null) {
            //项目实际金额 + (项目折扣价格 -实际金额 - 预约优惠) *  优惠券减扣比例
            commonCalculate = orderDetail.getRealPrice().add((orderDetail.getDiscountAmount().subtract(orderDetail.getRealPrice()).subtract(orderDetail.getAppointOff())).multiply(new BigDecimal(couponCommissionRate)).divide(hundred));
        }
        else if (orderDetail.getGiftAmount().compareTo(new BigDecimal(0)) == 1) {
            //项目实际金额 + (项目折扣价格 -实际金额 - 预约优惠) *  礼金减扣比例
            commonCalculate = orderDetail.getRealPrice().add((orderDetail.getDiscountAmount().subtract(orderDetail.getRealPrice()).subtract(orderDetail.getAppointOff())).multiply(new BigDecimal(giftCommissionRate)).divide(hundred));
        }
        else {
            commonCalculate = orderDetail.getRealPrice();
        }
        
        if (costCommissionType == 1) {
            commonCalculate = commonCalculate.subtract(commissionDto.getCostPrice());
        }
        
	    commissionType = commissionDto.getCommissionType();
	    commissionAmount = new BigDecimal(commissionDto.getCommissionAmount());
		
		switch(commissionType) {
			case 1 : // 按比例
				empCommission = commonCalculate.multiply(commissionAmount).divide(hundred);
				break;
			case 2 : // 按固定金额
				empCommission = commissionAmount.multiply(new BigDecimal(count));
				break;
			default :
				break;
		}
		
		//当为预约时
		if (isAppoint == 1) {
		   empCommission = empCommission.add(commissionDto.getAppointmentReward());
		}
		
		if(empCommission != null) {
		    Map<String, Integer> map = new HashMap<String, Integer>();
		    map.put("detailId", orderDetail.getDetailId());
		    map.put("employeeId", employeeId);
		    //如果单个订单出现重复提成人，将只留一条业绩数据
            Integer commissionCount = employeeObjectiveMapper.selectCount(map);
		    
            BigDecimal saveCommonCalculate = null;
            if (commissionCount > 0) {
                saveCommonCalculate = new BigDecimal(0);
            }
            else {
                                
                EmployeeObjective employeeObjective = new EmployeeObjective();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                //修改本月实际完成项目销售目标
                if (orderType == 1) {
                    Map<String, Object> isAssignStepPerformanceMap = orderDetailMapper.selectIsAssignStepPerformance(shiftMahjongStepId);
                    //是否指定
                    Integer isAssign = Integer.valueOf(isAssignStepPerformanceMap.get("isAssign").toString());
                    //步骤业绩计算
                    BigDecimal stepPerformance = new BigDecimal(isAssignStepPerformanceMap.get("stepPerformance").toString());
                    //步骤业绩计算方式(2:固定,1:比例)
                    Integer stepPerformanceType = Integer.valueOf(isAssignStepPerformanceMap.get("stepPerformanceType").toString());
                    //该项目中固定步骤值
                    BigDecimal fixedValue = new BigDecimal(isAssignStepPerformanceMap.get("fixedValue").toString());
                    
                    //比例
                    if (stepPerformanceType == 1) {
                        saveCommonCalculate = (commonCalculate.subtract(fixedValue)).multiply(stepPerformance).divide(new BigDecimal(100));
                    }
                    //固定值
                    else {
                        saveCommonCalculate = stepPerformance;
                    }
                    employeeObjective.setActualTotalProjectSales(saveCommonCalculate);
                    if (isAssign  == 1) {
                        employeeObjective.setActualAssignProjectSales(saveCommonCalculate);
                    }
                }
                //修改本月实际完成商品销售目标
                else if (orderType == 2){
                    employeeObjective.setActualGoodsSales(commonCalculate);
                }
                //修改本月实际完成套餐销售目标
                else {
                    employeeObjective.setActualComboSales(commonCalculate);
                }
                employeeObjective.setEmployeeId(employeeId);
                employeeObjective.setObjectiveMonth(dateFormat.format(new Date()));
                employeeObjective.setUpdateTime(DateUtil.getCurTimeStr());
                employeeObjectiveMapper.updateActual(employeeObjective);
            }
			EmployeeCommission commission = new EmployeeCommission(orderDetail.getDetailId(), shiftMahjongStepId, orderType, employeeId, 
			        saveCommonCalculate, empCommission, DateUtil.getCurDateMinStr());
			orderDetailMapper.insertEmployeeCommission(commission);
		}
		
	}
	
	/**
	 * 添加用户套餐信息
	 * @param comboId 套餐标识
	 * @param employeeId 员工标识
	 */
	protected void insertMemberComboInfo(Integer comboId, Integer employeeId, Integer memberId) {
	    
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("comboId", comboId);
		map.put("employeeId", employeeId);
		map.put("memberId", memberId);
		// 添加用户套餐
		Integer recordId = orderDetailMapper.insertMemberComboRecord(map);
		
		Map<String, Integer> projectMap = new HashMap<String, Integer>();
		projectMap.put("recordId", recordId);
		projectMap.put("employeeId", employeeId);
		projectMap.put("comboId", comboId);
        //会员套餐项目明细表
		orderDetailMapper.insertMemberComboProject(map);
	}
	
	protected void addProjectSalesInfo(List<Integer> projectIds, List<Integer> projectCount, List<Integer> projectPeople) {
		Map<String, Integer> salesMap = null;
		for(int i = 0, len = projectIds.size(); i < len; i++) {
			salesMap = new HashMap<String, Integer>();
			salesMap.put("projectId", projectIds.get(i));
			salesMap.put("projectCount", projectCount.get(i));
			salesMap.put("projectPeople", projectPeople.get(i));
			orderDetailMapper.updateProjectSalesInfo(salesMap);
		}
	}
	
	protected void addGoodsSalesInfo(List<Integer> goodsIds, List<Integer> goodsCount, List<Integer> goodsPeople) {
		Map<String, Integer> salesMap = null;
		for(int i = 0, len = goodsIds.size(); i < len; i++) {
			salesMap = new HashMap<String, Integer>();
			salesMap.put("goodsId", goodsIds.get(i));
			salesMap.put("goodsCount", goodsCount.get(i));
			salesMap.put("goodsPeople", goodsPeople.get(i));
			orderDetailMapper.updateGoodsSalesInfo(salesMap);
			
		}
	}
	
	protected void addComboSalesInfo(List<Integer> comboIds, List<Integer> comboCount, List<Integer> comboPeople) {
		Map<String, Integer> salesMap = null;
		for(int i = 0, len = comboIds.size(); i < len; i++) {
			salesMap = new HashMap<String, Integer>();
			salesMap.put("comboId", comboIds.get(i));
			salesMap.put("comboCount", comboCount.get(i));
			salesMap.put("comboPeople", comboPeople.get(i));
			orderDetailMapper.updateComboSalesInfo(salesMap);
		}
	}
	
}
