package cn.xhh.domain.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderRepository {

	public List<Order> findOrderByDriver(@Param("driverId") int driverId, @Param("tenantId") int tenantId,
			@Param("status") int status);
	
	/*
	 * 接单
	 */
	public int receipt(int orderId);
	
	/**
	 * 拒绝接单
	 * @param orderId
	 * @return
	 */
	public int refuseAccept(int orderId);
	
}
