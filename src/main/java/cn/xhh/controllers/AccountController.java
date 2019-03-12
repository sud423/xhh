package cn.xhh.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.domain.identity.User;
import cn.xhh.domainservice.identity.UserManager;
import cn.xhh.infrastructure.OptResult;


@Controller
public class AccountController {

	@Autowired
	private UserManager userManager;
	
	/**
	 * 未登录进入该地址并跳转到微信，请求微信授权登录
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index(String t) {
		return "index";
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
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public OptResult logon(HttpServletRequest request,String openId) {

		OptResult result=userManager.signIn(openId);
		
		if(result.getCode()==0)
		{			
			SavedRequest savedReq=WebUtils.getSavedRequest(request);
			
			if(savedReq==null || savedReq.getRequestUrl()==null) {
				result.setResult("dashboard");
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
	public OptResult register(User user) {
		OptResult result = userManager.saveReg(user);
		
		return result;
	}
}
