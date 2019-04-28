package cn.xhh.application.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.xhh.application.PriceService;
import cn.xhh.domain.business.PriceConfig;
import cn.xhh.domain.business.PriceRepository;
import cn.xhh.domain.business.PriceSearchResult;
import cn.xhh.infrastructure.OptResult;

@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	private PriceRepository priceRepository;

	@Value("${tenant_id}")
	private int tenantId;

	@Override
	public OptResult priceCount(String province, String city, String volume, String weight) {
		float vol = 0;
		float w = 0;
		try {
			vol = Float.valueOf(volume);
			w = Float.valueOf(weight);
		} catch (Exception e) {
			return OptResult.Failed("请输入有效的体积或重量");
		}
		List<PriceSearchResult> results = priceRepository.priceCount(province, city, vol, w, tenantId);

		return OptResult.Successed(results);
	}

	@Override
	public String[][] queryAddr() {
		// TODO Auto-generated method stub

		List<PriceConfig> priceConfigs = priceRepository.queryAddr(tenantId);
		if (priceConfigs == null)
			return null;

		String[][] arr = null;

		Map<String, String> map = new HashMap<String, String>();

//		String[][] arr = new String[priceConfigs.size()][];
		for (int i = 0; i < priceConfigs.size(); i++) {
			if (priceConfigs.get(i).getCity() == null || priceConfigs.get(i).getCity() == "")
				continue;

			String[] temp = priceConfigs.get(i).getCity().split(",");
			if (temp == null)
				continue;

//			String[][] l=new String[temp.length][1];

			for (int j = 0; j < temp.length; j++) {
				if (map.containsKey(temp[j]))
					continue;
				map.put(temp[j], priceConfigs.get(i).getProvince());

			}
		}
		arr = new String[map.size()][2];
		int n = 0;
		for (String key : map.keySet()) {
			arr[n][0] = map.get(key);
			arr[n][1] = key;
			n++;
		}
		return arr;
	}

}
