package cn.xhh.domain.identity;

public interface UserRepository {

	/**
	 * 根据openId查询用户
	 * @param openId
	 * @return
	 */
	public User findByOpenId(String openId);
	
	/**
	 * 更新用户登录时间
	 * @param userId
	 * @return
	 */
	public int updateLastLoginTime(int userId);
}
