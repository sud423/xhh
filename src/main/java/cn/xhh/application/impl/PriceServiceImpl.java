package cn.xhh.application.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.xhh.application.PriceService;
import cn.xhh.domain.business.PriceRepository;
import cn.xhh.domain.business.PriceSearchResult;

@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	private PriceRepository priceRepository;
	
	@Override
	public List<PriceSearchResult> priceCount(String province, String city, float volume, float weight, int tenantId) {
		return priceRepository.priceCount(province, city, volume, weight, tenantId);

	}

}
