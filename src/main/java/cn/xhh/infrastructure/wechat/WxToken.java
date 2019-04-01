package cn.xhh.infrastructure.wechat;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import cn.xhh.infrastructure.Utils;

public class WxToken {

	@JSONField(name = "access_token")
	private String accessToken;

	@JSONField(name = "expires_in")
	private int expiresIn;

	@JSONField(name = "refresh_token")
	private String refreshToken;

	@JSONField(name = "openid")
	private String openId;

	@JSONField(name = "scope")
	private String scope;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/// <summary>
	/// 根据appid，secret，code获取微信openid、access token信息
	/// </summary>
	/// <param name="code"></param>
	/// <returns></returns>
	public static WxToken getAuthToken(String code) throws Exception {
		String appId = Utils.getValueByKey("wechat.properties", "app_id");
		String appsecret = Utils.getValueByKey("wechat.properties", "appsecret");

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appId", appId);
		params.put("secret", appsecret);
		params.put("code", code);
		params.put("grant_type", "authorization_code");
		// 获取微信回传的openid、access token
		String result = WxClient.get(url, params);

		JSONObject jsonObj = JSONObject.parseObject(result);
		WxToken token = (WxToken) JSONObject.toJavaObject(jsonObj, WxToken.class);

		// 微信回传的数据为Json格式，将Json格式转化成对象
		if (token == null || token.getOpenId() == null ||  token.getOpenId()=="" || token.getExpiresIn() == 0)
			throw new Exception("获取微信认证失败,result：" + result + ",openId：" + token.getOpenId() + ",expiresIn："
					+ token.getExpiresIn());

		return token;

	}
	
	
	public static WxToken getAuthToken() {
		
		
		return null;
	}
}
