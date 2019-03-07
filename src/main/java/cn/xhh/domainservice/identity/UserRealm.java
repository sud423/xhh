package cn.xhh.domainservice.identity;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

import cn.xhh.domain.identity.User;
import cn.xhh.domain.identity.UserRepository;
import cn.xhh.dto.UserDto;

public class UserRealm extends AuthorizingRealm {
	@Autowired
	private UserRepository userRepository;

	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("userRealm");
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		UserDto user = (UserDto)principals.getPrimaryPrincipal();
//		String username=user.getUserName();
//		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//		Set<String> roles=userRepository.findRoleByUserName(username,user.getTenantId());
//        authorizationInfo.setRoles(roles);
//        Set<String> resources =userRepository.findResourceByUserName(username,user.getTenantId()) ;
//        authorizationInfo.setStringPermissions(resources);
//        System.out.println(userService.findResourceByUserName(username));
//        return authorizationInfo;
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		FreesecretToken freeToken = (FreesecretToken) token;

		String openId = freeToken.getUsername();
		User user = userRepository.findByOpenId(openId);
		if (user == null) {
			throw new UnknownAccountException();// 没找到帐号
		}
		if (user.getStatus() == 20) {
			throw new LockedAccountException(); // 帐号锁定
		}

		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();

		PropertyMap<User, UserDto> userMap = new PropertyMap<User, UserDto>() {
			@Override
			protected void configure() {
				map().setNickName(source.getUserLogin().getNickName());
				map().setOpenId(source.getUserLogin().getOpenId());
				map().setHeadImg(source.getUserLogin().getHeadImg());
			}
		};
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(userMap);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		session.setAttribute("user", userDto);

		return new SimpleAuthenticationInfo(openId, userDto, getName());

	}
}
