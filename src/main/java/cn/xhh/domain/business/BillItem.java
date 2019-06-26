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

	private float discount;

	private float lowPrice;

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
		BigDecimal m = new BigDecimal(price);
		return m.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getCost() {
		BigDecimal m = new BigDecimal(cost);
		return m.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getActualWeight() {
		BigDecimal m = new BigDecimal(actualWeight);
		return m.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
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

	public float getDiscount() {
		BigDecimal m = new BigDecimal(discount);
		return m.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getLowPrice() {
		BigDecimal m = new BigDecimal(lowPrice);
		return m.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
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


	public float count(){

		if(price<lowPrice)
			return 0;

		BigDecimal b = new BigDecimal(discount / 100);
//		float discount = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

		float rebate = price * (1 - b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

		float amount=price-rebate;
		if(lowPrice >0 && amount<lowPrice)
			return price-lowPrice;

		return rebate;
	}

}
