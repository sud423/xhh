package cn.xhh.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.domain.business.PriceRepository;
import cn.xhh.domain.business.PriceSearchResult;

@Controller
public class DashboardController {
	
	@Autowired
	private PriceRepository priceRepository;
	
	/**
	 * 
	 * @return 微信公众号首页
	 */
	@RequestMapping(value = { "/","/dashboard"}, method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	/**
	 * 
	 * @return hello
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(Map<String, Object> map) {
		map.put("world", "hello world");
		return "hello";
	}
	
	@ResponseBody
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public List<PriceSearchResult> priceCount(String province, String city, float volume, float weight, int tenantId) {
		return priceRepository.priceCount(province, city, volume, weight, tenantId);
	}
}
