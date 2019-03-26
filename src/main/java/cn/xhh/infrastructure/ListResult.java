package cn.xhh.infrastructure;

import java.util.List;

import com.github.pagehelper.PageInfo;

public class ListResult<T> {
	private List<T> data;
	private long totalPage;
	
	public ListResult() {}
	
	public ListResult(PageInfo<T> pageInfo) {
		data=pageInfo.getList();
		long total=pageInfo.getTotal();
		int pageSize=pageInfo.getPageSize();
		totalPage = (total + pageSize -1) / pageSize;
		
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	
	
}
