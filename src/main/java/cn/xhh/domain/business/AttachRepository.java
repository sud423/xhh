package cn.xhh.domain.business;

public interface AttachRepository {

	/**
	 * 添加上传文件信息
	 * @param attach 上传文件信息
	 * @return
	 */
	public int add(Attach attach);
	
	/**
	 * 更新上传文件信息
	 * @param attach 上传文件信息
	 * @return
	 */
	public int update(Attach attach);
}
