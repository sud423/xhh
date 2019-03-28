package cn.xhh.application;


import cn.xhh.domain.business.Order;
import cn.xhh.dto.OrderClientDto;
import cn.xhh.dto.OrderDriverDto;
import cn.xhh.infrastructure.ListResult;
import cn.xhh.infrastructure.OptResult;

public interface OrderService {
	public ListResult<OrderDriverDto> findOrderByDriver(int status,int pageNum);
	
	/**
	 * 
	 * @param status
	 * @param pageNum
	 * @return
	 */
	public ListResult<OrderClientDto> findOrderByClient(int status,int pageNum);
	
	/**
	 * 保存订单
	 * @param order
	 * @return
	 */
	public OptResult save(Order order);
}
