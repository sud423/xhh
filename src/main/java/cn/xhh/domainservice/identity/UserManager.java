package cn.xhh.domainservice.identity;

import cn.xhh.infrastructure.OptResult;


public interface UserManager {
	/**
	 * 登录
	 * 
	 * @param openId   微信授权openId
	 * @return 返回身份验证结果
	 */
    OptResult signIn(String openId);


	/**
	 * 检查用户身份类型，如果不存在就更新了30
	 * @param openId
	 * @param nickName
	 * @param headImg
	 * @return
	 */
    OptResult checkUser(String openId, boolean isDriver, String nickName, String headImg);

}
