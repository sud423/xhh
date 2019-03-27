package cn.xhh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.application.OrderService;
import cn.xhh.dto.OrderClientDto;
import cn.xhh.infrastructure.ListResult;

@Controller
@RequestMapping(value = "/c", method = RequestMethod.GET)
public class ClientController {
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "client/index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/q", method = RequestMethod.POST)
	public ListResult<OrderClientDto> findOrderByDriver(int status, int page) {
		return orderService.findOrderByClient(status, page);
	}
}
