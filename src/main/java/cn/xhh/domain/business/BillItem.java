package cn.xhh.domain.business;

import java.math.BigDecimal;

public class BillItem {
	private int id;

	private String express;

	private String expressNo;

	private float price;

	private float cost;

	private float actualWeight;

	private int count;

	private String addr;

	private String remark;

	private float volume;

	private float dis;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(float actualWeight) {
		this.actualWeight = actualWeight;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public float getDis() {
		return dis;
	}

	public void setDis(float dis) {
		this.dis = dis;
	}

	public float getVolume() {
		if (volume != 0) {
			float f=volume/1000000;
			BigDecimal  bd = new BigDecimal (f);
			return bd.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
			
		}
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

}
