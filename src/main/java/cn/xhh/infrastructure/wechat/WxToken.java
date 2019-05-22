package cn.xhh.infrastructure.wechat;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import cn.xhh.infrastructure.EhcacheManager;
import net.sf.ehcache.Element;

@Component
public class WxToken {
	private final static Logger log = Logger.getLogger(WxToken.class);
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
	
	@Value("${app_id}")
	private String appId;

	@Value("${appsecret}")
	private String appsecret;

	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

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
	/// 微信网页授权
	/// 根据appid，secret，code获取微信openid、access token信息
	/// </summary>
	/// <param name="code">微信确认授权登录返回的code</param>
	/// <returns></returns>
	public WxToken getAuthToken(String code) throws Exception {
		
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";

		Map<String, String> params = new HashMap<>();
		params.put("appId", appId);
		params.put("secret", appsecret);
		params.put("code", code);
		params.put("grant_type", "authorization_code");
		log.debug(params);
		// 获取微信回传的openid、access token
		String result = WxClient.get(url, params);
		log.debug(result);
		WxToken token = JSONObject.parseObject(result, WxToken.class);

		// 微信回传的数据为Json格式，将Json格式转化成对象
		if (token == null || token.getOpenId() == null || token.getOpenId() == "" || token.getExpiresIn() == 0)
			throw new Exception("获取微信认证失败,result：" + result + ",openId：" + token.getOpenId() + ",expiresIn："
					+ token.getExpiresIn());

		return token;

	}

	/**
	 * 获取access_token access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token
	 * access_token的有效期目前为2个小时
	 * 
	 * @return
	 */
	public WxToken getAuthToken() {

//		// 1. 创建缓存管理器
//		CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");
//		// 2. 获取缓存对象
//		Cache cache = cacheManager.getCache("wxAccessTokenCache");
//		// 5. 获取缓存
//		Element element = cache.get("wx_auth_token");

		Object objectValue = EhcacheManager.getInstance().get("wxAccessTokenCache", "wx_auth_token");

		String result;
		if (objectValue == null) {
			String url = "https://api.weixin.qq.com/cgi-bin/token";
			Map<String, String> params = new HashMap<>();
			params.put("grant_type", "client_credential");
			params.put("appId", appId);
			params.put("secret", appsecret);
			
			result = WxClient.get(url, params);

			EhcacheManager.getInstance().put("wxAccessTokenCache", "wx_auth_token", result);

		} else
			result = objectValue.toString();
		
		log.debug(result);
		
		WxToken token = JSONObject.parseObject(result, WxToken.class);
		return token;
	}

	public static void main(String[] args) {
		
		EhcacheManager.getInstance().put("wxAccessTokenCache", "key1", "value1");
		Object objectValue = EhcacheManager.getInstance().get("wxAccessTokenCache", "key1");
		Element value = EhcacheManager.getInstance().getCache("wxAccessTokenCache").get("key1");
		System.out.println(objectValue);
		System.out.println(value);
		System.out.println(value.getObjectValue());
	}
}
