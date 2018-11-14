package com.config;

import com.common.model.SessionUser;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {

    public static String SESSION_USER = "_sessionUser";

    public static SessionUser getSessionUser(HttpServletRequest request) {
        return (SessionUser) WebUtils.getSessionAttribute(request,SESSION_USER);
    }

    public static void setSessionUser(HttpServletRequest request,SessionUser sessionUser){
        WebUtils.setSessionAttribute(request,SESSION_USER,sessionUser);
    }
    public static void invalidate(HttpServletRequest request){
        setSessionUser(request, null);
    }
}
