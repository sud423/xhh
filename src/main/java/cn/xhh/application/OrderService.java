package cn.xhh.application;


import cn.xhh.dto.OrderClientDto;
import cn.xhh.dto.OrderDriverDto;
import cn.xhh.infrastructure.ListResult;

public interface OrderService {
	public ListResult<OrderDriverDto> findOrderByDriver(int status,int pageNum);
	
	/**
	 * 
	 * @param status
	 * @param pageNum
	 * @return
	 */
	public ListResult<OrderClientDto> findOrderByClient(int status,int pageNum);
}
