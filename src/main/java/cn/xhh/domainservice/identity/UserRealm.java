package cn.xhh.domainservice.identity;

import java.text.ParseException;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

import cn.xhh.domain.identity.UserRepository;
import cn.xhh.dto.UserDto;

public class UserRealm extends AuthorizingRealm {
//	@Autowired
//	private UserRepository userRepository;
	
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
		
//		String username = (String)token.getPrincipal();
//        SysUser user = userRepository.findByUserName(username);
//        if(user == null) {
//            throw new UnknownAccountException();//没找到帐号
//        }
//        if(user.getStatus()==20) {
//            throw new LockedAccountException(); //帐号锁定
//        }
//                
//        Subject subject = SecurityUtils.getSubject();
//        Session session = subject.getSession();
//        
//        PropertyMap<SysUser, UserDto> userMap = new PropertyMap<SysUser, UserDto>() {
//			@Override
//			protected void configure() {
//				try {
//					map().setCreated(source.getAddTime());
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		};
//		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.addMappings(userMap);
//		
//
//		UserDto userDto = modelMapper.map(user, UserDto.class);
//        
//        session.setAttribute("user", userDto);
//        
//        return new SimpleAuthenticationInfo(userDto,user.getPassword(),ByteSource.Util.bytes(user.getSalt()),getName());
		return null;
        
	}
}
