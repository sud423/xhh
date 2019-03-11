package cn.xhh.domainservice.identity;

public enum LoginType {
	PASSWORD("password"), //需要密码登录
	NOPASSWD("nopassword"); //免密码登录

	private String code;//登录方式
	
	
	private LoginType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
