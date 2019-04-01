package cn.xhh.dto;

import java.util.List;

import cn.xhh.domain.business.BillItem;

public class BillDto {
	private int id;
	
	private String period;
	
	private float price;
	
	private List<BillItem> items;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<BillItem> getItems() {
		return items;
	}

	public void setItems(List<BillItem> items) {
		this.items = items;
	}
	
	
}
