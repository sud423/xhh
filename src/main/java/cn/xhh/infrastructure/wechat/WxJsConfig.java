package cn.xhh.infrastructure.wechat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

import cn.xhh.infrastructure.Utils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class WxJsConfig {

	private boolean debug;

	private String appId;

	private long timestamp;

	private String nonceStr;

	private String signature;

	private List<String> jsApiList;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public List<String> getJsApiList() {
		return jsApiList;
	}

	public void setJsApiList(List<String> jsApiList) {
		this.jsApiList = jsApiList;
	}

	/**
	 * 获取js票据
	 * 
	 * @return
	 */
	private static String getJsTicket() {
		// 1. 创建缓存管理器
		CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");
		// 2. 获取缓存对象
		Cache cache = cacheManager.getCache("wxAccessTokenCache");
		// 5. 获取缓存
		Element element = cache.get("wx_js_auth_token");

		String result;
		if (element == null) {
			WxToken token = WxToken.getAuthToken();
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("access_token", token.getAccessToken());
			params.put("type", "jsapi");

			result = WxClient.get(url, params);

			element = new Element("wx_auth_token", result);
			// 4. 将元素添加到缓存
			cache.put(element);
		}
		// 获取缓存 值
		result = element.getObjectValue().toString();
		JSONObject jsonObj = JSONObject.parseObject(result);

		return jsonObj.getString("ticket");
	}

	private static String getJsSignature(String url, long timestamp, String nonce) {

		StringBuilder jsSignature = new StringBuilder();

		jsSignature.append("jsapi_ticket=").append(getJsTicket()).append("&");
		jsSignature.append("noncestr=").append(nonce).append("&");
		jsSignature.append("timestamp=").append(timestamp).append("&");
		jsSignature.append("url=").append(url.indexOf("#") >= 0 ? url.substring(0, url.indexOf("#")) : url);

		return SHA1.encode(jsSignature.toString());

	}

	/**
	 * 创建js配置
	 * 
	 * @param url
	 * @return
	 */
	public static WxJsConfig create(String url) {
		String nonce = UUID.randomUUID().toString();
		long timestamp = new Date().getTime() / 1000;

		List<String> apis = new ArrayList<String>();
		apis.add("openLocation");
		apis.add("getLocation");
		apis.add("updateAppMessageShareData");
		apis.add("updateTimelineShareData");
		apis.add("onMenuShareTimeline");
		apis.add("onMenuShareAppMessage");
		apis.add("showMenuItems");
		apis.add("hideAllNonBaseMenuItem");
		apis.add("chooseImage");
		apis.add("getLocalImgData");

		WxJsConfig config = new WxJsConfig();
		config.setAppId(Utils.getValueByKey("wechat.properties", "app_id"));
		config.setNonceStr(nonce);
		config.setSignature(getJsSignature(url, timestamp, nonce));
		config.setTimestamp(timestamp);
		config.setDebug(false);

		return config;
	}

}
