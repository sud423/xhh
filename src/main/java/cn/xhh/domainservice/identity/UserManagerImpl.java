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

import cn.xhh.domain.identity.UserRepository;
import cn.xhh.dto.UserDto;
import cn.xhh.infrastructure.OptResult;

@Service
public class UserManagerImpl implements UserManager {


	@Autowired
	private UserRepository userRepository;
	
	/**
	 * ��¼
	 * 
	 * @param userName   �û���
	 * @return ���������֤���
	 */
	public OptResult signIn(String openId) {
		
		if(openId==null)
			return OptResult.Failed("΢��openId����Ϊ��");
				
		FreesecretToken token = new FreesecretToken(openId);
		
		Subject subject = SecurityUtils.getSubject();
		try {

			subject.login(token);

			if (subject.isAuthenticated()) {
				UserDto user = (UserDto)token.getPrincipal();
				//��������¼ʱ��
				userRepository.updateLastLoginTime(user.getId());
				
				return OptResult.Successed();
			} else {
				return OptResult.Failed("�û��������벻ƥ�䣡");
			}

		} catch (IncorrectCredentialsException e) {
			System.out.println(e);
			return OptResult.Failed("�û��������벻ƥ�䣡");
		} catch (ExcessiveAttemptsException e) {
			System.out.println(e);
			return OptResult.Failed("��������¼��ʧ�ܴ����ѳ���5�Σ��˻��ѱ���������10���Ӻ����ԣ�");
		} catch (LockedAccountException e) {
			System.out.println(e);
			return OptResult.Failed("�����˻��Ѷ��ᣬ�������Ա��ϵ��");
		} catch (DisabledAccountException e) {
			System.out.println(e);
			return OptResult.Failed("�����˻��ѽ��ã��������Ա��ϵ��");
		} catch (ExpiredCredentialsException e) {// ����ѹ��ڣ�
			System.out.println(e);
			return OptResult.Failed("����ѹ��ڣ�");
		} catch (UnknownAccountException e) {// δ֪���˺�
			System.out.println(e);
			return OptResult.Failed("�˺ź����벻ƥ�䣡");
		} catch (UnauthorizedException e) {// δ֪����Ȩ
			System.out.println(e);
			return OptResult.Failed("�û��������벻ƥ�䣡");
		}

	}

	/**
	 * 登出
	 */
	public void signOut() {
		// shiro登出清理凭证信息
		SecurityUtils.getSubject().logout();
	}
}
