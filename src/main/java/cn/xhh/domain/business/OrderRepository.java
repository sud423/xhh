package cn.xhh.domain.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderRepository {

	List<Order> findOrderByDriver(@Param("driverId") int driverId, @Param("tenantId") int tenantId,
                                  @Param("status") int status);

	/*
	 * 接单
	 */
    int receipt(int orderId);

	/**
	 * 拒绝接单
	 * 
	 * @param orderId
	 * @return
	 */
    int refuseAccept(int orderId);

	/**
	 * 获取客户的订单列表
	 * @param clientId 客户编号
	 * @param tenantId 租户编号
	 * @param status 状态
	 * @return
	 */
    List<Order> findOrderByClient(@Param("clientId") int clientId, @Param("tenantId") int tenantId, @Param("status") ArrayList<Integer> status);
	
	/**
	 * 创建订单
	 * @param order
	 * @return
	 */
    int add(Order order);
}
