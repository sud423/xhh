package cn.xhh.domainservice.identity;

import cn.xhh.infrastructure.OptResult;

public interface UserManager {
	/**
	 * ��¼
	 * 
	 * @param openId   ΢����ȨopenId
	 * @return ���������֤���
	 */
	public OptResult signIn(String openId);

	/**
	 * �ǳ�
	 */
	public void signOut();
}
