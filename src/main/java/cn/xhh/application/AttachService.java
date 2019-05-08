package cn.xhh.application;

import cn.xhh.infrastructure.OptResult;

public interface AttachService {

	/**
	 * base 64 上传图片
	 * @param base64Img  图片base64字符串
	 * @param sourceId 关联编号
	 * @param sourceName 关联表名
	 * @param userId 操作人编号
	 * @param tenantId 所属租户编号
	 * @return
	 */
    OptResult upload(String base64Img, int sort, int sourceId, String sourceName, int userId, int tenantId);
}
