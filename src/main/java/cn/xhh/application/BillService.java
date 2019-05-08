package cn.xhh.application;

import cn.xhh.dto.BillDto;
import cn.xhh.infrastructure.ListResult;

public interface BillService {

	ListResult<BillDto> queryBillByStatus(int page, int status);
	
	BillDto queryBillItem(int billId);
}
