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
	 * δ����¼����õ�ַ����ת��΢�ţ�����΢����Ȩ��¼
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	/**
	 * ΢�Żص����ж��û��Ƿ�ע�ᣬ�����ע�����ת�ϴη��ʵ�ַ
	 * @param code
	 * @param returnUrl
	 * @return
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	public String redirectTo(String code, String returnUrl) {
		return "";
	}
	
	/**
	 * ��¼����
	 * @return ����json��ʽ�ĵ�¼���
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
				//�����ϴ�����ĵ�ַ
				result.setResult(savedReq.getRequestUrl());
			}
		}
		return result;
		
	}
	
	/**
	 * ע��
	 * @param t
	 * @return
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String reg(String t) {
		return "account/register";
	}
	
}
