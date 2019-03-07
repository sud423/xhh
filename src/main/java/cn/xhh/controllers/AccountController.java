package cn.xhh.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xhh.domainservice.identity.UserManager;
import cn.xhh.infrastructure.OptResult;


@Controller
public class AccountController {

	@Autowired
	private UserManager userManager;
	
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
				//返回上次请求的地址
				result.setResult(savedReq.getRequestUrl());
			}
		}
		return result;
		
	}
}
