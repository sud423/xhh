package cn.xhh.domainservice.identity;

public enum LoginType {
	PASSWORD("password"), // �����¼
	NOPASSWD("nopassword"); // ���ܵ�¼

	private String code;// ״ֵ̬

	private LoginType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
