package cn.xhh.domainservice.identity;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * �Զ���token ͨ�����췽�������� �˺������½�� ���ܵ�½
 * 
 * @author susd
 *
 */
public class FreesecretToken extends UsernamePasswordToken {
	
	private static final long serialVersionUID = -2564928913725078138L;
	
	private LoginType type;

	public FreesecretToken() {
		super();
	}

	public FreesecretToken(String username, String password, LoginType type, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.type = type;
	}

	/** ���ܵ�¼ */
	public FreesecretToken(String username) {
		super(username, "", false, null);
		this.type = LoginType.NOPASSWD;
	}

	/** �˺������¼ */
	public FreesecretToken(String username, String password) {
		super(username, password, false, null);
		this.type = LoginType.PASSWORD;
	}

	public LoginType getType() {
		return type;
	}

	public void setType(LoginType type) {
		this.type = type;
	}
}
