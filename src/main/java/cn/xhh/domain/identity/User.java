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
	 * 租户编号
	 */
	private int tenantId;

	@Size(max = 30, message = "姓名最大长度为30个字符")
	@NotNull(message = "姓名不能为空")
	private String name;

	/**
	 * 手机号
	 */
	@Size(max = 15, message = "手机号最大长度为15个字符")
	@NotNull(message = "手机号不能为空")
	private String cell;
	
	/**
	 * 身份证号
	 */
	@Size(max = 18, message = "身份证号最大长度为18个字符")
	@NotNull(message = "身份证号不能为空")
	private String idNumber;
	
	/**
	 * 启用类型 10：客户（会员） 20：驾驶员
	 */
	private byte type;

	/**
	 * 状态 1：正常 10：离职 只能查看，不能操作 20：冻结
	 */
	private byte status;

	/**
	 * 审核不通过原因
	 */
	private String auditReason;
	
	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 版本号
	 */
	private int version;

	/**
	 * 附件
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
