package cn.xhh.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.domain.identity.User;
import cn.xhh.domain.identity.UserLogin;
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.domainservice.identity.UserManager;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.Utils;
import cn.xhh.infrastructure.wechat.SignUtil;
import cn.xhh.infrastructure.wechat.WxToken;
import cn.xhh.infrastructure.wechat.WxUser;

@Controller
public class AccountController {

	@Autowired
	private UserManager userManager;


	private Logger log = Logger.getLogger(AccountController.class);

	/**
	 * 未登录进入该地址并跳转到微信，请求微信授权登录
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index(HttpServletRequest request) throws UnsupportedEncodingException {
		SavedRequest savedReq = WebUtils.getSavedRequest(request);
		String returnUrl = request.getContextPath() + "/callback";
		if (savedReq != null && savedReq.getRequestUrl() != null) {
			returnUrl = returnUrl + "?returnUrl=" + savedReq.getRequestUrl();
		}
		log.debug(returnUrl);
		// 微信授权登陆
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ Utils.getValueByKey("wechat.properties", "app_id") + "&redirect_uri="
				+ URLEncoder.encode(returnUrl, "utf-8")
				+ "&response_type=code&scope=snsapi_userinfo&state=supaotui#wechat_redirect";

		log.debug(url);
		return "redirect:" + url;
	}

	/**
	 * 微信回调，判断用户是否注册，如果已注册就跳转上次访问地址ַ
	 * 
	 * @param code
	 * @param returnUrl
	 * @return
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	public String redirectTo(String code, String returnUrl) {

		try {
			log.debug(returnUrl);
			log.debug(code);
			WxToken token = WxToken.getAuthToken(code);
			
			String url = "redirect:/reg?returnUrl=" + returnUrl;
			boolean isDriver=returnUrl.indexOf("d") > -1;
			if (isDriver) {
				url += "&t=20";
			} else {
				url += "&t=10";
			}
			if(userManager.checkUser(token.getOpenId(),returnUrl).getCode()==0) {
				
				OptResult result = userManager.signIn(token.getOpenId());

				if (result.getCode() == 0) {
					if (returnUrl == null || returnUrl == "")
						return "redirect:" + returnUrl;

				}
				return "redirect:/error";
			}
			else
			{
				Subject subject = SecurityUtils.getSubject();
				Session session = subject.getSession();
				session.setAttribute("WXUSER", WxUser.getUserByOpenId(token.getOpenId()));
				return url;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return "redirect:/error";
		}

	}

	/**
	 * 登录方法
	 * 
	 * @return 返回json格式的登录结果
	 */
	@RequestMapping(value = "/sigup", method = RequestMethod.POST)
	@ResponseBody
	public OptResult logon(HttpServletRequest request, String openId) {

		OptResult result = userManager.signIn(openId);

		if (result.getCode() == 0) {
			SavedRequest savedReq = WebUtils.getSavedRequest(request);

			if (savedReq == null || savedReq.getRequestUrl() == null) {

				if (SessionManager.getUser().getType() == 10)
					result.setResult(request.getContextPath() + "/c/index");
				else
					result.setResult(request.getContextPath() + "/d/index");
			} else {
				// 返回上次请求的地址ַ
				result.setResult(savedReq.getRequestUrl());
			}
		}
		return result;

	}

	/**
	 * 注册
	 * 
	 * @param t
	 * @return
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String reg(String t) {
		return "register";
	}

	@RequestMapping(value = "/uc/save", method = RequestMethod.POST)
	@ResponseBody
	public OptResult register(HttpServletRequest request, User user) {
		Subject currentUser = SecurityUtils.getSubject();

		Session session = currentUser.getSession();
		Object obj = session.getAttribute("WXUSER");
		if (obj == null)
			return OptResult.Failed("网络异常，请关闭后请新登录");
		WxUser wxUser = (WxUser) obj;
		UserLogin ul = new UserLogin();
		
		ul.setNickName(wxUser.getNickName());
		ul.setOpenId(wxUser.getOpenId());
		ul.setProvide((byte) 1);
		user.setUserLogin(ul);
		OptResult result = userManager.saveReg(user);
		if (result.getCode() == 0) {
			SavedRequest savedReq = WebUtils.getSavedRequest(request);

			if (savedReq == null || savedReq.getRequestUrl() == null) {
				result.setResult(request.getContextPath() + "/my?t=" + user.getType());
			} else {
				// 返回上次请求的地址
				result.setResult(savedReq.getRequestUrl());
			}
		}
		return result;
	}

	/**
	 * 微信接入
	 * 
	 * @param wc
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/connect", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8"); // 微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
		response.setCharacterEncoding("UTF-8"); // 在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
		boolean isGet = request.getMethod().toLowerCase().equals("get");

		PrintWriter out = response.getWriter();

		try {
			if (isGet) {
				String signature = request.getParameter("signature");// 微信加密签名
				String timestamp = request.getParameter("timestamp");// 时间戳
				String nonce = request.getParameter("nonce");// 随机数
				String echostr = request.getParameter("echostr");// 随机字符串

				// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
				if (SignUtil.checkSignature(Utils.getValueByKey("wechat.properties", "token"), signature, timestamp,
						nonce)) {
					log.info("Connect the weixin server is successful.");
					response.getWriter().write(echostr);
				} else {
					log.error("Failed to verify the signature!");
				}
			} else {

			}
		} catch (Exception e) {
			log.error("Connect the weixin server is error.");
		} finally {
			out.close();
		}
	}

}
