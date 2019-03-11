package cn.xhh.domain.business;

public class PriceSearchResult {
	/**
	 * 快递公司
	 */
	public String express;
	
	/**
	 * 价格
	 */
	private float price;

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
}
