package cn.xhh.application;

import cn.xhh.domain.identity.User;
import cn.xhh.infrastructure.OptResult;

public interface UserService {

	/**
	 * 保存注册信息
	 * @param user
	 * @return
	 */
	public OptResult saveReg(User user);
}
