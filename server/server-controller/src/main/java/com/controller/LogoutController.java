package com.controller;

import com.config.CookieUtils;
import com.config.SessionUtils;
import com.config.StringUtils;
import com.token.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Joe
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {
	
	@Resource
	private TokenManager tokenManager;

	@RequestMapping(method = RequestMethod.GET)
	public String logout( String backUrl, HttpServletRequest request) {
		String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
		if (StringUtils.isNotBlank(token)) {
			tokenManager.remove(token);
		}
		SessionUtils.invalidate(request);
		return "redirect:" + (StringUtils.isBlank(backUrl) ? "/admin/admin" : backUrl);
	}
}