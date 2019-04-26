package cn.xhh.application.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.xhh.application.AttachService;
import cn.xhh.application.UserService;
import cn.xhh.domain.identity.User;
import cn.xhh.domain.identity.UserLogin;
import cn.xhh.domain.identity.UserRepository;
import cn.xhh.infrastructure.OptResult;

@Service
public class UserServiceImpl implements UserService {

	@Value("${tenant_id}")
	private int tenantId;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AttachService attachService;
	
	@Override
	@Transactional
	public OptResult saveReg(User user) {
		user.setStatus((byte)2);
		user.setAddTime(new Date());
		user.setTenantId(tenantId);
		int result=userRepository.reg(user);
		if(result==0)
			return OptResult.Failed("注册失败，请稍候重试");
		
		UserLogin login=user.getUserLogin();
		
		login.setUserId(user.getId());
		login.setTenantId(user.getTenantId());
		login.setAddTime(new Date());
		
		result=userRepository.saveLogin(login);
		if(result==0)
			return OptResult.Failed("注册失败，请稍候重试");
		
		OptResult res=attachService.upload(user.getFrontImg(), 1, user.getId(), "user");
		if(res.getCode()!=0)
			return res;
		res=attachService.upload(user.getBackImg(), 2, user.getId(), "user");
		if(res.getCode()!=0)
			return res;
		
		return OptResult.Successed();
	}
}
