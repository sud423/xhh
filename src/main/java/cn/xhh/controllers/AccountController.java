package cn.xhh.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.application.UserService;
import cn.xhh.domain.identity.User;
import cn.xhh.domain.identity.UserLogin;
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.domainservice.identity.UserManager;
import cn.xhh.dto.UserDto;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.wechat.WxToken;
import cn.xhh.infrastructure.wechat.WxUser;

@Controller
public class AccountController {

	@Autowired
	private UserManager userManager;
	@Autowired
	private UserService userService;
	
	@Autowired
	private WxToken wxToken;

	@Autowired
	private WxUser wxUser;

	@Value("${app_id}")
	private String appId;

//	@Value("$appsecret")
//	private String appsecret;

	@Value("${token}")
	private String appToken;

	@Value("${domain}")
	private String domain;
	private Logger log = Logger.getLogger(AccountController.class);

	/**
	 * 未登录进入该地址并跳转到微信，请求微信授权登录
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/login")
	public String index(HttpServletRequest request) throws UnsupportedEncodingException {

//		SavedRequest savedReq = WebUtils.getSavedRequest(request);
//		String returnUrl = domain + "/callback";
//		if (savedReq != null && savedReq.getRequestUrl() != null) {
//			returnUrl = returnUrl + "?returnUrl=" + savedReq.getRequestUrl();
//		}
//		log.debug(returnUrl);
//		// 微信授权登陆
//		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
//				+ URLEncoder.encode(returnUrl, "utf-8")
//				+ "&response_type=code&scope=snsapi_userinfo&state=supaotui#wechat_redirect";
//
//		log.debug(url);
//		return "redirect:" + url;
		return "login";
	}

	/**
	 * 微信回调，判断用户是否注册，如果已注册就跳转上次访问地址ַ
	 * 
	 * @param code
	 * @param returnUrl
	 * @return
	 */
	@RequestMapping(value = "/callback")
	public String redirectTo(HttpServletRequest request, String code, String returnUrl) {

		try {
			log.debug(returnUrl);
			log.debug(code);
			WxToken token = wxToken.getAuthToken(code);
			WxUser wxuser = wxUser.getUserByOpenId(token.getOpenId());
			String url = "redirect:/reg?returnUrl=" + returnUrl;
			boolean isDriver = appId.indexOf("wxb2da43818db39fa2") > -1;
			if (isDriver) {
				url += "&t=20";
			} else {
				url += "&t=10";
			}
			if (userManager.checkUser(token.getOpenId(), isDriver, wxuser.getNickName(), wxuser.getHeadImgUrl())
					.getCode() == 0) {

				OptResult result = userManager.signIn(token.getOpenId());
				log.debug(returnUrl);
				if (result.getCode() == 0) {
					if (returnUrl == null || returnUrl == "")
						if (isDriver)
							return "redirect:" + request.getContextPath() + "/d/index";
						else
							return "redirect:" + request.getContextPath() + "/c/index";
					else {
						return "redirect:" + returnUrl;
					}
				}
				return "redirect:/error";
			} else {
				Subject subject = SecurityUtils.getSubject();
				Session session = subject.getSession();
				session.setAttribute("WXUSER", wxuser);
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
			return OptResult.Failed("网络异常，请关闭微信后重新操作");
		WxUser wxUser = (WxUser) obj;
		UserLogin ul = new UserLogin();

		ul.setNickName(wxUser.getNickName());
		ul.setOpenId(wxUser.getOpenId());
		ul.setProvide((byte) 1);
		ul.setHeadImg(wxUser.getHeadImgUrl());
		user.setUserLogin(ul);
		OptResult result = userService.saveReg(user);
		if (result.getCode() == 0) {
			result = userManager.signIn(wxUser.getOpenId());
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
	
	@ResponseBody
	@RequestMapping(value = "/personalinfo", method = RequestMethod.GET)
	public UserDto get() {
		UserDto user=userService.get();
		return user;
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info() {
		return "info";
	}
	
	@ResponseBody
	@RequestMapping(value="/pi/save",method=RequestMethod.POST)
	public OptResult infoSave(User user) {
		OptResult result= userService.updateInfo(user);
		
		return result;
	}
	

	/**
	 * 协议
	 * @return
	 */
	@RequestMapping(value="/term",method=RequestMethod.GET)
	public String protocol() {
		return "term";
	}
}
