package cn.xhh.domain.identity;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import cn.xhh.domain.*;

public class User implements Entity<User> {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private int id;

	/**
	 * �⻧���
	 */
	private int tenantId;

	@Size(max = 30, message = "������󳤶�Ϊ30���ַ�")
	@NotNull(message = "��������Ϊ��")
	private String name;

	/**
	 * �ֻ���
	 */
	@Size(max = 15, message = "�ֻ�����󳤶�Ϊ15���ַ�")
	@NotNull(message = "�ֻ��Ų���Ϊ��")
	private String cell;
	
	/**
	 * ���֤��
	 */
	@Size(max = 18, message = "���֤����󳤶�Ϊ18���ַ�")
	@NotNull(message = "���֤�Ų���Ϊ��")
	private String idNumber;
	
	/**
	 * �������� 10���ͻ�����Ա�� 20����ʻԱ
	 */
	private byte type;

	/**
	 * ״̬ 1������ 10����ְ ֻ�ܲ鿴�����ܲ��� 20������
	 */
	private byte status;

	/**
	 * ��˲�ͨ��ԭ��
	 */
	private String auditReason;
	
	/**
	 * ���ʱ��
	 */
	private Date addTime;

	/**
	 * �汾��
	 */
	private int version;

	/**
	 * ����
	 */
	private List<String> attach;
	
	private UserLogin userLogin;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getAuditReason() {
		return this.auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public String getAddTime() {
		if (addTime != null)
			return dateFormat.format(addTime);
		else
			return "";
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
		
	public List<String> getAttach() {
		return attach;
	}

	public void setAttach(List<String> attach) {
		this.attach = attach;
	}

	public UserLogin getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(UserLogin userLogin) {
		this.userLogin = userLogin;
	}

	@Override
	public boolean sameIdentityAs(User other) {
		return other != null && this.getId() == other.getId();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final User other = (User) object;
		return sameIdentityAs(other);
	}

	@Override
	public int hashCode() {
		return Integer.toString(getId()).hashCode();
	}

}
