package com.rabbitmq.fanout.receive;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class NoticeRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String HEAD = "head";
	
	public static final String BODY = "body";

	private String org;
	/**
	 *  原交易流水号
	 */
	private String serviceSn;
	/**
	 * 交易类型代码
	 */
	private String serviceId;
	/**
	 * 渠道号
	 */
	private String channelId;
	/**
	 * 请求时间
	 */
	private Date requestTime;
	/**
	 * 内部服务交易码
	 */
	private String serviceCode;
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getServiceSn() {
		return serviceSn;
	}
	public void setServiceSn(String serviceSn) {
		this.serviceSn = serviceSn;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}
