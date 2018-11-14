package com.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于web.xml生成验证码
 * 
 * @author Joe
 */
public class CaptchaFilter implements Filter {


	public void init(FilterConfig filterConfig) throws ServletException {
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setDateHeader("Expires", 0L);
		httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		httpResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setContentType("image/jpeg");
	}

	public void destroy() {
	}
}