package cn.xhh.domainservice.identity;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.xhh.domain.identity.User;
import cn.xhh.domain.identity.UserLogin;
import cn.xhh.domain.identity.UserRepository;
import cn.xhh.infrastructure.OptResult;

@Service
public class UserManagerImpl implements UserManager {


	@Autowired
	private UserRepository userRepository;
	
	
	/**
	 * 登录
	 * 
	 * @param userName   用户名
	 * @return 返回身份验证结果
	 */
	public OptResult signIn(String openId) {
		
		if(openId==null)
			return OptResult.Failed("微信openId不能为空");
				
		FreesecretToken token = new FreesecretToken(openId);
		
		Subject subject = SecurityUtils.getSubject();
		try {

			subject.login(token);

			if (subject.isAuthenticated()) {
				
				//更新最后登录时间
				userRepository.updateLastLoginTime(openId);
				
				return OptResult.Successed();
			} else {
				return OptResult.Failed("用户名和密码不匹配！");
			}

		} catch (IncorrectCredentialsException e) {
			System.out.println(e);
			return OptResult.Failed("用户名和密码不匹配！");
		} catch (ExcessiveAttemptsException e) {
			System.out.println(e);
			return OptResult.Failed("由于您登录的失败次数已超过5次，账户已被锁定，请10分钟后再试！");
		} catch (LockedAccountException e) {
			System.out.println(e);
			return OptResult.Failed("您的账户已冻结，请与管理员联系！");
		} catch (DisabledAccountException e) {
			System.out.println(e);
			return OptResult.Failed("您的账户已禁用，请与管理员联系！");
		} catch (ExpiredCredentialsException e) {// 身份已过期！
			System.out.println(e);
			return OptResult.Failed("身份已过期！");
		} catch (UnknownAccountException e) {// 未知的账号
			System.out.println(e);
			return OptResult.Failed("账号和密码不匹配！");
		} catch (UnauthorizedException e) {// 未知的授权
			System.out.println(e);
			return OptResult.Failed("用户名和密码不匹配！");
		}

	}

	public OptResult checkUser(String openId,boolean isDriver,String nickName,String headImg) {
		User user=userRepository.findByOpenId(openId);
		if(user==null || user.getId()==0)
			return OptResult.Failed("用户不存在");
		//为驾驶员
		if(isDriver && user.getType()==10) {
			user.setType((byte)30);
			userRepository.update(user);
		}
		if(!isDriver && user.getType()==20) {
			user.setType((byte)30);
			userRepository.update(user);			
		}
		
		UserLogin ul=user.getUserLogin();
		ul.setHeadImg(headImg);
		ul.setNickName(nickName);
		
		userRepository.updateLogin(ul);
		return OptResult.Successed();
	}
	
	
	
}
