package cn.xhh.domain.identity;

import java.util.Date;

import cn.xhh.domain.Entity;

public class UserLogin implements Entity<UserLogin> {
	private int tenantId;

	private int userId;

	private byte provide;

	private String openId;

	private String nickName;

	private String headImg;
	
	private Date addTime;

	private int version;
	
	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte getProvide() {
		return provide;
	}

	public void setProvide(byte provide) {
		this.provide = provide;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Date getAddTime() {
		return addTime;
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

	@Override
	public boolean sameIdentityAs(UserLogin other) {

		return other != null && this.getUserId() == other.getUserId() && 
				this.getOpenId() == other.getOpenId();
	}
}
