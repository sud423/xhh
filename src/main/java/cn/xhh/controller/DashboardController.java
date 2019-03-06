package cn.xhh.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardController {
	/**
	 * 
	 * @return hello
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(Map<String, Object> map) {
		map.put("world", "hello world");
		return "hello";
	}
}
