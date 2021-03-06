package cn.xhh.domain.business;

import java.util.Date;

import cn.xhh.domain.Entity;

public class Bill implements Entity<Bill> {

private int id;
	
	private int tenantId;
	
	/**账单流水号*/
	private String billNumber;

	/**账期*/
	private String period;
	
	/**用户编号*/	
	private int userId;
		
	/**总价格*/
	private float price;
	
	/**折后价格*/
	private float discountPrice;

	private boolean isAdjust;
	/**
	 * 支付金额
	 */
	private float adjustPrice;
	
	/**实际到账金额*/
	private float amount;

	/**
	 * 支付渠道  10：微信 20：支付宝
	 */
	private byte payChannel;

	/**支付时间*/
	private Date payTime;
	
	/**实际到账时间*/
	private Date arrivalTime;
	
	/**状态 1：未推送 10：已推送，待支付 20:在途中 30：已支付*/
	private byte status;
	
	/**添加时间*/
	private Date addTime;
	
	/**版本号*/
	private int version;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getPeriod() {
		return period;
	}

	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public float getPrice() {
		return price;
	}

	public boolean isAdjust() {
		return isAdjust;
	}

	public float getAdjustPrice() {
		return adjustPrice;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public void setPayChannel(byte payChannel) {
		this.payChannel = payChannel;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public byte getStatus() {
		return status;
	}


	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}


	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	@Override
	public boolean sameIdentityAs(Bill other) {

		return other != null && id == other.getId();
	}
	
	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final Bill other = (Bill) object;
		return sameIdentityAs(other);
	}

	@Override
	public int hashCode() {
		return Integer.toString(id).hashCode();
	}

}
