package cn.xhh.domainservice.identity;

import cn.xhh.infrastructure.OptResult;


public interface UserManager {
	/**
	 * 登录
	 * 
	 * @param openId   微信授权openId
	 * @return 返回身份验证结果
	 */
	public OptResult signIn(String openId);

	/**
	 * 登出
	 */
	public void signOut();
	
	
	/**
	 * 检查用户身份类型，如果不存在就更新了30
	 * @param openId
	 * @param returnUrl
	 * @return
	 */
	public OptResult checkUser(String openId,String returnUrl);
}
