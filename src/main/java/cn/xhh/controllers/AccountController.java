package cn.xhh.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

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


@Controller
public class AccountController {

	@Autowired
	private UserManager userManager;
	
	/**
	 * 未登录进入该地址并跳转到微信，请求微信授权登录
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index(HttpServletRequest request) throws UnsupportedEncodingException {
		SavedRequest savedReq=WebUtils.getSavedRequest(request);
		String returnUrl=request.getContextPath()+"/callback";
		if(savedReq!=null && savedReq.getRequestUrl()!=null) {
			returnUrl=returnUrl+"?returnUrl="+savedReq.getRequestUrl();
		}
		
		//微信授权登陆
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                            Utils.getValueByKey("wechat.properties", "app_id") + "&redirect_uri=" + URLEncoder.encode(returnUrl, "utf-8") +
                            "&response_type=code&scope=snsapi_userinfo&state=supaotui#wechat_redirect";
		
		return "redirect:"+url;
	}

	/**
	 * 微信回调，判断用户是否注册，如果已注册就跳转上次访问地址ַ
	 * @param code
	 * @param returnUrl
	 * @return
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	public String redirectTo(String code, String returnUrl) {
		return "";
	}
	
	/**
	 * 登录方法
	 * @return 返回json格式的登录结果
	 */
	@RequestMapping(value = "/sigup", method = RequestMethod.POST)
	@ResponseBody
	public OptResult logon(HttpServletRequest request,String openId) {

		OptResult result=userManager.signIn(openId);
		
		if(result.getCode()==0)
		{			
			SavedRequest savedReq=WebUtils.getSavedRequest(request);
			
			if(savedReq==null || savedReq.getRequestUrl()==null) {
				
				if(SessionManager.getUser().getType()==10)
					result.setResult(request.getContextPath()+"/c/index");
				else
					result.setResult(request.getContextPath()+"/d/index");
			}
			else {
				//返回上次请求的地址ַ
				result.setResult(savedReq.getRequestUrl());
			}
		}
		return result;
		
	}
	
	/**
	 * 注册
	 * @param t
	 * @return
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String reg(String t) {
		return "register";
	}
	
	@RequestMapping(value = "/uc/save", method = RequestMethod.POST)
	@ResponseBody
	public OptResult register(HttpServletRequest request,User user,String frontImg,String backImg) {
		
		UserLogin ul=new UserLogin();
		ul.setTenantId(ul.getTenantId());
		ul.setNickName(Utils.generateCode());
		ul.setOpenId(Utils.generateCode());
		ul.setProvide((byte)1);
		user.setUserLogin(ul);
		OptResult result = userManager.saveReg(user,frontImg,backImg);
		if(result.getCode()==0) {
			SavedRequest savedReq = WebUtils.getSavedRequest(request);

			if (savedReq == null || savedReq.getRequestUrl() == null) {
				result.setResult(request.getContextPath()+"/my?t="+user.getType());
			} else {
				// 返回上次请求的地址
				result.setResult(savedReq.getRequestUrl());
			}
		}
		return result;
	}
}
