package cn.xhh.domain.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BillRepository {

	List<Bill> getBillByStatus(@Param("tenantId") int tenantId, @Param("userId") int userId, @Param("status") int status);
	
	List<BillItem> getItemByBillId(int billId);
	
}
