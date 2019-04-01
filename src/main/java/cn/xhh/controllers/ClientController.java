package cn.xhh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.application.BillService;
import cn.xhh.application.OrderService;
import cn.xhh.domain.business.Order;
import cn.xhh.dto.BillDto;
import cn.xhh.dto.OrderClientDto;
import cn.xhh.infrastructure.ListResult;
import cn.xhh.infrastructure.OptResult;

@Controller
@RequestMapping(value = "/c", method = RequestMethod.GET)
public class ClientController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private BillService billService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "client/index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/q", method = RequestMethod.POST)
	public ListResult<OrderClientDto> findOrderByDriver(int status, int page) {
		return orderService.findOrderByClient(status, page);
	}
	
	/**
	 * 创建订单
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/s", method = RequestMethod.POST)
	public OptResult createOrder(Order order) {
		return orderService.save(order);
	}
	

	@ResponseBody
	@RequestMapping(value = "/b", method = RequestMethod.POST)
	public ListResult<BillDto> queryByStatus(int status,int page){
		return billService.queryBillByStatus(page, status);
	}
	
	@ResponseBody
	@RequestMapping(value = "/i", method = RequestMethod.POST)
	public BillDto getBillItem(int billId) {
		return billService.queryBillItem(billId);
	}
}
