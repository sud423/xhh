package cn.xhh.application;

import cn.xhh.infrastructure.OptResult;

public interface AttachService {

	/**
	 * base 64 上传图片
	 * @param base64  图片base64字符串
	 * @param sourceId 关联编号
	 * @param sourceName 关联表名
	 * @return
	 */
	public OptResult upload(String base64Img,int sort,int sourceId,String sourceName,int userId,int tenantId);
}
