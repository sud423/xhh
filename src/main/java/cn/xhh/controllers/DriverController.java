package cn.xhh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/d", method = RequestMethod.GET)
public class DriverController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "driver/index";
	}
	
}
