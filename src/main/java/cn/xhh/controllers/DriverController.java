package cn.xhh.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.application.OrderService;
import cn.xhh.domain.business.OrderRepository;

import cn.xhh.dto.OrderDriverDto;
import cn.xhh.infrastructure.ListResult;
import cn.xhh.infrastructure.OptResult;

@Controller
@RequestMapping(value = "/d", method = RequestMethod.GET)
public class DriverController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;
	

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "driver/index";
	}

	/**
	 * 根据当前司机的订单信息
	 * 
	 * @param status
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/q", method = RequestMethod.POST)
	public ListResult<OrderDriverDto> findOrderByDriver(int status, int page) {
		return orderService.findOrderByDriver(status, page);
	}

	/**
	 * 接单
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/r", method = RequestMethod.POST)
	public OptResult receipt(int orderId) {

		int result = orderRepository.receipt(orderId);
		if (result > 0)
			return OptResult.Successed();
		else
			return OptResult.Failed("接单失败，请稍候重试");
	}

	/**
	 * 拒单
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ra", method = RequestMethod.POST)
	public OptResult refuse(int orderId) {
		int result = orderRepository.refuseAccept(orderId);
		if (result > 0)
			return OptResult.Successed();
		else
			return OptResult.Failed("拒单失败，请稍候重试");
	}
	
	
}
