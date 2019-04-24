package cn.xhh.infrastructure.wechat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.xhh.infrastructure.EhcacheManager;

@Component
public class WxJsConfig {

	@Autowired
	private WxToken wxToken;
	
	private boolean debug;

	@Value("${app_id}")
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
	private String getJsTicket() {

		Object obj = EhcacheManager.getInstance().get("wxAccessTokenCache", "wx_js_auth_token");

		String result;
		if (obj == null) {
			WxToken token = wxToken.getAuthToken();
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("access_token", token.getAccessToken());
			params.put("type", "jsapi");

			result = WxClient.get(url, params);

			EhcacheManager.getInstance().put("wxAccessTokenCache", "wx_auth_token", result);

		} else
			result = obj.toString();
		
		JSONObject jsonObj = JSONObject.parseObject(result);

		return jsonObj.getString("ticket");
	}

	private String getJsSignature(String url, long timestamp, String nonce) {

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
	public WxJsConfig create(String url) {
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
		config.setAppId(appId);
		config.setNonceStr(nonce);
		config.setSignature(getJsSignature(url, timestamp, nonce));
		config.setTimestamp(timestamp);
		config.setDebug(false);

		return config;
	}

}
