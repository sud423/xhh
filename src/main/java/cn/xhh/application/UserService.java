package cn.xhh.application;

import cn.xhh.domain.identity.User;
import cn.xhh.dto.UserDto;
import cn.xhh.infrastructure.OptResult;

public interface UserService {
	/**
	 * 获取用户信息
	 * @return
	 */
	public UserDto get();
	
	/**
	 * 保存注册信息
	 * @param user
	 * @return
	 */
	public OptResult saveReg(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public OptResult updateInfo(User user);
}
