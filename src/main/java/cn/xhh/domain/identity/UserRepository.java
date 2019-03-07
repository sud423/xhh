package cn.xhh.domain.identity;

public interface UserRepository {

	/**
	 * 根据openid
	 * @param openId
	 * @return
	 */
	public User findByOpenId(String openId);
	
	/**
	 * 更新登录 时间
	 * @param userId
	 * @return
	 */
	public int updateLastLoginTime(int userId);
}
