package cn.xhh.domain.identity;

public interface UserRepository {

	/**
	 * ����openid
	 * @param openId
	 * @return
	 */
	public User findByOpenId(String openId);
	
	/**
	 * ���µ�¼ ʱ��
	 * @param userId
	 * @return
	 */
	public int updateLastLoginTime(int userId);
}
