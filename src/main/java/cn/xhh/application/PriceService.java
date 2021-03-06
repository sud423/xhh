package cn.xhh.application;

import cn.xhh.infrastructure.OptResult;

public interface PriceService {
	/**
	 * 运费查询
	 * 
	 * @param province 目的地所属省份
	 * @param city     目的所属市
	 * @param volume   体积
	 * @param weight   实际重量
	 * @return
	 */
	OptResult priceCount(String province, String city, String volume, String weight);
	
	/**
	 * 查询已配置的运费地址
	 * @return
	 */
	String[][] queryAddr();
}
