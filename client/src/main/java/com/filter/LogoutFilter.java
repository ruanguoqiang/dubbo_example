package com.filter;

import com.config.HTTPUtil;
import com.config.SessionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单点退出Filter
 * 
 * @author Joe
 */
public class LogoutFilter extends ClientFilter {

	// 单点退出成功后跳转页(配置当前应用上下文相对路径，不设置或为空表示项目根目录)
	private String ssoBackUrl;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (StringUtils.isEmpty(pattern)) {
			throw new IllegalArgumentException("pattern不能为空");
		}
		if (StringUtils.isEmpty(ssoBackUrl)) {
			throw new IllegalArgumentException("ssoBackUrl不能为空");
		}
	}

	public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SessionUtils.invalidate(request);
		String logoutUrl = new StringBuilder().append(ssoServerUrl)
				.append("/logout?backUrl=").append(getLocalUrl(request)).append(ssoBackUrl).toString();
	/*	Map<String,String> params=new HashMap<String, String>();
		params.put("backUrl",ssoBackUrl);
		HTTPUtil.post(logoutUrl,params);*/
		response.sendRedirect(logoutUrl);
		return false;
	}

	/**
	 * 获取当前上下文路径
	 * 
	 * @param request
	 * @return
	 */
	private String getLocalUrl(HttpServletRequest request) {
		return new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName()).append(":")
				.append(request.getServerPort() == 80 ? "" : request.getServerPort()).append(request.getContextPath())
				.toString();
	}

	public void setSsoBackUrl(String ssoBackUrl) {
		this.ssoBackUrl = ssoBackUrl;
	}
}