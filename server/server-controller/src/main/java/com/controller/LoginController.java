package com.controller;

import com.anonation.ValidateParam;
import com.common.model.LoginUser;
import com.config.CookieUtils;
import com.config.StringUtils;
import com.controller.common.BaseController;
import com.model.Validator;
import com.provider.IdProvider;
import com.token.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {
    // 登录页
    private static final String LOGIN_PATH = "/login";

    @Resource
    private TokenManager tokenManager;
    @RequestMapping(method = RequestMethod.GET)
    public String login(@ValidateParam({ Validator.NOT_BLANK }) String backUrl,
                        HttpServletRequest request){
        String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
        if (StringUtils.isNotBlank(token) && tokenManager.validate(token) != null) {
            return "redirect:" + authBackUrl(backUrl, token);
        }
        else {
            return goLoginPath(backUrl, request);
        }
    }
    @RequestMapping(method = RequestMethod.POST)
    public String login(String username,String password,String backUrl,HttpServletRequest request,HttpServletResponse response) throws Exception{
        if (!username.equals("abc")&&!password.equals("123")) {
            request.setAttribute("errorMessage", "密码或用户名错误");
            return goLoginPath(backUrl, request);
        }
        else {
            LoginUser loginUser = new LoginUser(1, "abc");
            String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
            if (StringUtils.isBlank(token) || tokenManager.validate(token) == null) {// 没有登录的情况
                token = createToken(loginUser);
                addTokenInCookie(token, request, response);
            }

            // 跳转到原请求
            backUrl = URLDecoder.decode(backUrl, "utf-8");
            return "redirect:" + authBackUrl(backUrl, token);
        }
    }

    private String authBackUrl(String backUrl, String token) {
        StringBuilder sbf = new StringBuilder(backUrl);
        if (backUrl.indexOf("?") > 0) {
            sbf.append("&");
        }
        else {
            sbf.append("?");
        }
        sbf.append("__vt_param__").append("=").append(token);
        return sbf.toString();
    }
    private String goLoginPath(String backUrl, HttpServletRequest request) {
        request.setAttribute("backUrl", backUrl);
        return LOGIN_PATH;
    }

    private String createToken(LoginUser loginUser) {
        // 生成token
        String token = IdProvider.createUUIDId();

        // 缓存中添加token对应User
        tokenManager.addToken(token, loginUser);
        return token;
    }

    private void addTokenInCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        // Cookie添加token
        Cookie cookie = new Cookie(TokenManager.TOKEN, token);
        cookie.setPath("/");
        if ("https".equals(request.getScheme())) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
