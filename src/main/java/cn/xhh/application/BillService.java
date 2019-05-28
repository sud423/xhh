package cn.xhh.application;


import cn.xhh.dto.BillDto;
import cn.xhh.infrastructure.ListResult;
import cn.xhh.infrastructure.OptResult;

public interface BillService {

	/**
	 * 根据状态查询 客户的账单
	 * @param page 当前页码
	 * @param status 状态
	 * @return
	 */
	ListResult<BillDto> queryBillByStatus(int page, int status);

	/**
	 * 查询账单项
	 * @param billId
	 * @return
	 */
	BillDto queryBillItem(int billId);

	/**
	 * 获取支付账单
	 * @param billId
	 * @return
	 */
	OptResult createPay(int billId, String channel, String ip);

	/**
	 * 微信支付成功后回调
	 * @param outTradeNo 订单交易号
	 * @param fee 实付金额
	 * @return
	 */
	int updateCallback(String outTradeNo,String fee);
}
