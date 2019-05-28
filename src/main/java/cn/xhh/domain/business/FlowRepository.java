package cn.xhh.domain.business;

import java.util.List;

public interface FlowRepository {
    /**
     * 新增支付订单
     * @param flow
     * @return
     */
    int add(Flow flow);

    /**
     * 更新支付订单
     * @param flow
     * @return
     */
    int update(Flow flow);

    /**
     * 获取租户5分钟内未支付成功的订单
     * @param tenantId
     * @return
     */
    List<Flow> queryByFallFiveMin(int tenantId);
}
