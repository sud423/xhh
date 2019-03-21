package cn.xhh.application;

import java.util.List;


import cn.xhh.dto.OrderDriverDto;

public interface OrderService {
	public List<OrderDriverDto> findOrderByDriver(int status,int page);
}
