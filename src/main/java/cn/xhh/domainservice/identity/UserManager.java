package cn.xhh.domainservice.identity;

import cn.xhh.domain.identity.User;
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
	 * 保存注册信息
	 * @param user
	 * @return
	 */
	public OptResult saveReg(User user,String frontImg,String backImg);
}
