package cn.xhh.domainservice.identity;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义token 通过构造方法来区分 账号密码登陆和 免密登陆
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

	/** 免密登录 */
	public FreesecretToken(String username) {
		super(username, "", false, null);
		this.type = LoginType.NOPASSWD;
	}

	/** 账号密码登录 */
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