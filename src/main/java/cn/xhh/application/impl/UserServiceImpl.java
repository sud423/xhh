package cn.xhh.application.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.xhh.application.AttachService;
import cn.xhh.application.UserService;
import cn.xhh.domain.identity.User;
import cn.xhh.domain.identity.UserLogin;
import cn.xhh.domain.identity.UserRepository;
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.dto.UserDto;
import cn.xhh.infrastructure.OptResult;

@Service
public class UserServiceImpl implements UserService {

	@Value("${tenant_id}")
	private int tenantId;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AttachService attachService;
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public UserDto get() {
		User user=userRepository.findByOpenId(SessionManager.getUser().getOpenId());
		PropertyMap<User, UserDto> userMap = new PropertyMap<User, UserDto>() {
			@Override
			protected void configure() {
				map().setNickName(source.getUserLogin().getNickName());
				map().setOpenId(source.getUserLogin().getOpenId());
				map().setHeadImg(source.getUserLogin().getHeadImg());
//				map().setAddTime(source.getAddTime());
			}
		};
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(userMap);

		UserDto userDto = modelMapper.map(user, UserDto.class);
		return userDto;
	}
	
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
		
		OptResult res=attachService.upload(user.getFrontImg(), 1, user.getId(), "user", user.getId(),user.getTenantId());
		if(res.getCode()!=0)
			return res;
		res=attachService.upload(user.getBackImg(), 2, user.getId(), "user", user.getId(),user.getTenantId());
		if(res.getCode()!=0)
			return res;
		
		return OptResult.Successed();
	}

	@Override
	public OptResult updateInfo(User user) {
		if(user.getStatus()==30)
			user.setStatus((byte)2);
		int result=userRepository.update(user);
		if(result==0)
			return OptResult.Failed("信息修改失败，请稍候重试");
		
		OptResult res=attachService.upload(user.getFrontImg(), 1, user.getId(), "user", user.getId(),user.getTenantId());
		if(res.getCode()!=0)
			return res;
		res=attachService.upload(user.getBackImg(), 2, user.getId(), "user", user.getId(),user.getTenantId());
		if(res.getCode()!=0)
			return res;
		
		return OptResult.Successed();
	}
	
	
}
