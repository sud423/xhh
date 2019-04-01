package cn.xhh.application;

import cn.xhh.dto.BillDto;
import cn.xhh.infrastructure.ListResult;

public interface BillService {

	public ListResult<BillDto> queryBillByStatus(int page,int status);
	
	public BillDto queryBillItem(int billId);
}
