package cn.xhh.application.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.xhh.application.PriceService;
import cn.xhh.domain.business.PriceRepository;
import cn.xhh.domain.business.PriceSearchResult;
import cn.xhh.infrastructure.OptResult;

@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	private PriceRepository priceRepository;

	@Override
	public OptResult priceCount(String province, String city, String volume, String weight, String tenantId) {
		float vol = 0;
		float w = 0;
		int t=0;
		try {
			vol = Float.valueOf(volume);
			w = Float.valueOf(weight);
			t=Integer.valueOf(tenantId);
		} catch (Exception e) {
			return OptResult.Failed("请输入有效的体积或重量");
		}
		List<PriceSearchResult> results = priceRepository.priceCount(province, city, vol, w, t);

		return OptResult.Successed(results);
	}

}
