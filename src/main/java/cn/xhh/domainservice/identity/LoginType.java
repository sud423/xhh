package cn.xhh.domainservice.identity;

public enum LoginType {
	PASSWORD("password"), // ÃÜÂëµÇÂ¼
	NOPASSWD("nopassword"); // ÃâÃÜµÇÂ¼

	private String code;// ×´Ì¬Öµ

	private LoginType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
