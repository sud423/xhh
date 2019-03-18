package cn.xhh.domain.business;

import java.util.Date;

import cn.xhh.domain.Entity;

public class Order implements Entity<Order> {

	private int id;
	
	private int tenantId;
	
	private int userId;
	
	private String goods;
	
	private int num;
	
	private float weight;
	
	private String addr;
	
	private int driverId;
	
	private int carId;
	
	private String remark;
	
	private int version;
	
	private Date addTime;
	
	
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


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getGoods() {
		return goods;
	}


	public void setGoods(String goods) {
		this.goods = goods;
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public float getWeight() {
		return weight;
	}


	public void setWeight(float weight) {
		this.weight = weight;
	}


	public String getAddr() {
		return addr;
	}


	public void setAddr(String addr) {
		this.addr = addr;
	}


	public int getDriverId() {
		return driverId;
	}


	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}


	public int getCarId() {
		return carId;
	}


	public void setCarId(int carId) {
		this.carId = carId;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public Date getAddTime() {
		return addTime;
	}


	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}


	@Override
	public boolean sameIdentityAs(Order other) {
		return other != null && this.getId() == other.getId();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final Order other = (Order) object;
		return sameIdentityAs(other);
	}

	@Override
	public int hashCode() {
		return Integer.toString(getId()).hashCode();
	}
}
