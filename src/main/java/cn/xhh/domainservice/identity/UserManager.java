package cn.xhh.domainservice.identity;

import cn.xhh.infrastructure.OptResult;

public interface UserManager {
	/**
	 * 登录
	 * 
	 * @param userName   用户�?
	 * @param password   密码
	 * @param rememberMe 记住�?
	 * @return 返回身份验证结果
	 */
	public OptResult signIn(String userName, String password, boolean rememberMe);

	/**
	 * 登出
	 */
	public void signOut();
}
