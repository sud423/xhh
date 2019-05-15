package cn.xhh.domain.business;

import org.apache.ibatis.annotations.Param;

public interface DiscountRepository {
    /**
     * 根据快递及当前时间与推送时间的间距天数
     * @param express
     * @param ts
     * @return
     */
    Discount findByExpress(@Param("express") String express,@Param("ts") int ts,@Param("teantnId") int tenantId);
}
