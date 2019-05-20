package cn.xhh.domain.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BillRepository {

	/**
	 * 根据状态获取用户的账单列表
	 * @param tenantId 所属租户
	 * @param userId 客户编号
	 * @param status 状态
	 * @return
	 */
	List<Bill> getBillByStatus(@Param("tenantId") int tenantId, @Param("userId") int userId, @Param("status") int status);

	/**
	 * 根据账单编号 获取账单项
	 * @param billId 账单编号
	 * @return
	 */
	List<BillItem> getItemByBillId(int billId);

	/**
	 * 根据账单编号获取账单详情
	 * @param billId
	 * @return
	 */
	Bill getBillById(int billId);

	/**
	 * 支付订单号查询 账单
	 * @param billNumber
	 * @return
	 */
	Bill getBillByNumber(String billNumber);

	/**
	 * 更新账单信息
	 * @param bill
	 * @return
	 */
	int update(Bill bill);

}
