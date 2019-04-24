package cn.xhh.infrastructure.wechat;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;


@Component
public class WxUser {

	private final static Logger log = Logger.getLogger(WxUser.class);
	
	@Autowired
	private WxToken wxToken;
	
	/// <summary>
	/// 用户的唯一标识
	/// </summary>
	@JSONField(name = "openid")
	private String openId;

	/// <summary>
	/// 用户昵称
	/// </summary>
	@JSONField(name = "nickname")
	private String nickName;

	/// <summary>
	/// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	/// </summary>
	public String sex;

	/// <summary>
	/// 用户个人资料填写的省份
	/// </summary>
	public String province;

	/// <summary>
	/// 普通用户个人资料填写的城市
	/// </summary>
	public String city;

	/// <summary>
	/// 国家，如中国为CN
	/// </summary>
	public String country;

	/// <summary>
	/// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	/// </summary>
	@JSONField(name = "headimgurl")
	public String headImgUrl;

	/// <summary>
	/// 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	/// </summary>
	public String unionId;

	/// <summary>
	/// 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	/// </summary>
	public int subscribe;

	/// <summary>
	/// 关注时间
	/// </summary>
	@JSONField(name = "subscribe_time")
	public int subscribeTime;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public int getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(int subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	/**
	 * 根据确认登录返回的code获取用户基本信息
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public WxUser getUserByCode(String code) throws Exception {
		WxToken token = wxToken.getAuthToken(code);
		String url = "https://api.weixin.qq.com/sns/userinfo";

		return getUser(url, token);
	}

	/**
	 * 根据用户唯一授权码获取用户基础信息
	 * 
	 * @param openId
	 * @return
	 */
	public WxUser getUserByOpenId(String openId) {
		WxToken token = wxToken.getAuthToken();
		token.setOpenId(openId);
		String url = "https://api.weixin.qq.com/cgi-bin/user/info";

		return getUser(url, token);
	}

	/**
	 * 请求微信接口获取用户信息
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	private static WxUser getUser(String url, WxToken token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", token.getAccessToken());
		params.put("openid", token.getOpenId());
		params.put("lang", "zh_CN");

		String result = WxClient.get(url, params);
		log.debug(result);
		WxUser user = JSONObject.parseObject(result, WxUser.class);

		return user;
	}
}
