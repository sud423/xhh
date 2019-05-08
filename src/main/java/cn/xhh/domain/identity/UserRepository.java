package cn.xhh.domain.identity;

import org.apache.ibatis.annotations.Param;

public interface UserRepository {

	/**
	 * 根据openId查询用户
	 * @param openId
	 * @return
	 */
    User findByOpenId(@Param("openId") String openId);
	
	/**
	 * 更新用户登录时间
	 * @param openId
	 * @return
	 */
    int updateLastLoginTime(@Param("openId") String openId);
	
	/**
	 * 注册新用户
	 * @param user
	 * @return
	 */
    int reg(User user);
	
	/**
	 * 保存第三方登录信息
	 * @param login
	 * @return
	 */
    int saveLogin(UserLogin login);
	
	/**
	 * 更新第三方信息
	 * @param login
	 * @return
	 */
    int updateLogin(UserLogin login);
	
	/**
	 * 更新用户
	 * @param user 更新的用户信息
	 * @return
	 */
    int update(User user);
}
