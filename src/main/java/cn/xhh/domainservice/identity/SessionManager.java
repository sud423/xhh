package cn.xhh.domainservice.identity;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import cn.xhh.dto.UserDto;

/**
 * �û���ݹ���
 * @author TY
 *
 */
public class SessionManager {

	/**
	 * ��ȡ�û����
	 * @return
	 */
	public static int getUserId() {
		UserDto user=getUser();
		if(user==null) return 0;
		return user.getId();
	}
	
	public static int getTenantId() {
		UserDto user=getUser();
		if(user==null) return 0;
		return user.getTenantId();
	}
	
	/**
	 * ��ȡ�û���Ϣ����
	 * @return
	 */
	public static UserDto getUser() {
		Subject currentUser = SecurityUtils.getSubject();

		Session session = currentUser.getSession();
		Object obj=session.getAttribute("user");
		if(obj==null)
			return null;
		
		return (UserDto)obj;
	}
	
}
