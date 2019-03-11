package cn.xhh.application;

import java.util.List;

import cn.xhh.domain.business.PriceSearchResult;

public interface PriceService {
	/**
	 * 运费查询
	 * @param province 目的地所属省份
	 * @param city 目的所属市
	 * @param volume 体积
	 * @param weight 实际重量
	 * @param tenantId 所属租户
	 * @return
	 */
	public List<PriceSearchResult> priceCount(String province, String city, float volume, float weight, int tenantId);
}
