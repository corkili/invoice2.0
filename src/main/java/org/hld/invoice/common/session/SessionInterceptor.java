package org.hld.invoice.common.session;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义Session拦截器
 * @author 李浩然 2017年7月20日
 * @version 1.0
 */
@Component("SpringMVCInterceptor")
public class SessionInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger(SessionInterceptor.class);

    /**
     * @see HandlerInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        // 过滤登录、退出访问
        String[] noFilters = new String[] { "login", "register", "captcha", "init", "no_login", "verify", "mail", "resetPassword" };

        String uri = request.getRequestURI();

        String userId = (String)session.getAttribute(SessionContext.ATTR_USER_ID);

        for (String s : noFilters) {
            if(uri.contains(s)){
                return true;
            }
        }

        if(userId == null) {
            response.sendRedirect("/no_login");
            return false;
        }

        return true;
    }

    /**
     * @see HandlerInterceptor#postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * @see HandlerInterceptor#afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
