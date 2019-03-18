package cn.xhh.domain.business;

import java.util.List;

public interface OrderRepository {

	public List<Order> findOrderByDriver(int driverId,int tenantId,int status);
}
