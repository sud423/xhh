package cn.xhh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.application.OrderService;
import cn.xhh.dto.OrderDriverDto;

@Controller
@RequestMapping(value = "/d", method = RequestMethod.GET)
public class DriverController {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "driver/index";
	}
	
	/**
	 * 根据当前司机的订单信息
	 * @param status
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/q", method = RequestMethod.POST)
	private List<OrderDriverDto> findOrderByDriver(int status,int page){
		return orderService.findOrderByDriver(status, page);
	}
}
