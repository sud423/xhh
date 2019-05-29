package cn.xhh.domain.business;

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
     * 根据主键编号获取支付流水
     * @param id
     * @return
     */
    Flow get(String id);
}
