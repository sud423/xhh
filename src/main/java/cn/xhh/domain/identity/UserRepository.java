package cn.xhh.domain.identity;

import org.apache.ibatis.annotations.Param;

public interface UserRepository {

	/**
	 * 根据openId查询用户
	 * @param openId
	 * @return
	 */
	public User findByOpenId(@Param("openId")String openId);
	
	/**
	 * 更新用户登录时间
	 * @param userId
	 * @return
	 */
	public int updateLastLoginTime(@Param("openId")String openId);
	
	/**
	 * 注册新用户
	 * @param user
	 * @return
	 */
	public int reg(User user);
	
	/**
	 * 保存第三方登录信息
	 * @param login
	 * @return
	 */
	public int saveLogin(UserLogin login);
	
	/**
	 * 更新用户
	 * @param user 更新的用户信息
	 * @return
	 */
	public int update(User user);
}
