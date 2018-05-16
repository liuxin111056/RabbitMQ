package com.zefun.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 自助收银计算提成
 * @author luhw
 *
 */
public class CashierCommissionDto implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1606392906270729940L;

	/** 项目标识 */
	private Integer projectId;
	
	/** 商品标识 */
	private Integer goodsId;
	
	/** 套餐标识 */
	private Integer comboId;
	
	/** 奖励类型*/
    private Integer appointmentRewardType;
    
    /** 预约奖励*/
    private BigDecimal appointmentReward;
	
	/** 提成方式 1:按业绩比例,2:按固定金额 */
	private Integer commissionType;
	
	/** 提成金额 */
	private Integer commissionAmount;
	
	/** 销售次数 */
	private Integer salesCount;
	
	/** 销售人数 */
	private Integer salesPeople;
	
	/** 成本价格*/
	private BigDecimal costPrice;
	
	/** 非团购业绩 */
	private BigDecimal commonCalculate;
	
	/** 团购业绩 */
	private Integer groupCalculate;

	
	
	public Integer getAppointmentRewardType() {
        return appointmentRewardType;
    }

    public void setAppointmentRewardType(Integer appointmentRewardType) {
        this.appointmentRewardType = appointmentRewardType;
    }

    public BigDecimal getAppointmentReward() {
        return appointmentReward;
    }

    public void setAppointmentReward(BigDecimal appointmentReward) {
        this.appointmentReward = appointmentReward;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getComboId() {
		return comboId;
	}

	public void setComboId(Integer comboId) {
		this.comboId = comboId;
	}

//	public Integer getAssignCommissionType() {
//		return assignCommissionType;
//	}
//
//	public void setAssignCommissionType(Integer assignCommissionType) {
//		this.assignCommissionType = assignCommissionType;
//	}
//
//	public Integer getAssignCommissionAmount() {
//		return assignCommissionAmount;
//	}
//
//	public void setAssignCommissionAmount(Integer assignCommissionAmount) {
//		this.assignCommissionAmount = assignCommissionAmount;
//	}

	public Integer getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(Integer commissionType) {
		this.commissionType = commissionType;
	}

	public Integer getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Integer commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public Integer getSalesPeople() {
		return salesPeople;
	}

	public void setSalesPeople(Integer salesPeople) {
		this.salesPeople = salesPeople;
	}

	public BigDecimal getCommonCalculate() {
		return commonCalculate;
	}

	public void setCommonCalculate(BigDecimal commonCalculate) {
		this.commonCalculate = commonCalculate;
	}

	public Integer getGroupCalculate() {
		return groupCalculate;
	}

	public void setGroupCalculate(Integer groupCalculate) {
		this.groupCalculate = groupCalculate;
	}
	
	@Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
	
}
