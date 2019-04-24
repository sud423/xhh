package cn.xhh.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.application.PriceService;
import cn.xhh.infrastructure.OptResult;

@Controller
public class DashboardController {
	
	@Autowired
	private PriceService priceService;
	
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
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public OptResult priceCount(String province, String city, String volume, String weight, String tenantId) {
		return priceService.priceCount(province, city, volume, weight, tenantId);
	}
}
