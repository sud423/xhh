package cn.xhh.domain.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PriceRepository {

	/**
	 * 运费查询
	 * @param province 目的地所属省份
	 * @param city 目的所属市
	 * @param volume 体积
	 * @param weight 实际重量
	 * @param tenantId 所属租户
	 * @return
	 */
	public List<PriceSearchResult> priceCount(@Param("province") String province, @Param("city") String city,
			@Param("volume") float volume, @Param("weight") float weight, @Param("tenantId") int tenantId);
	
	/**
	 * 查询配置的运费地址
	 * @param tenantId
	 * @return
	 */
	public List<PriceConfig> queryAddr(@Param("tenantId") int tenantId);

}
