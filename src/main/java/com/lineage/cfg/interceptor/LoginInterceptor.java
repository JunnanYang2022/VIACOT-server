package com.lineage.cfg.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author YangJunNan
 * @description 应用全局拦截器
 * @date 2020/9/6
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * controller执行后且视图返回后调用此方法
     * 这里可得到执行controller时的异常信息
     * 这里可记录操作日志
     *
     * @param request
     * @param response
     * @param arg2
     * @param arg3
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2,
                                Exception arg3) throws Exception {
    }

    /**
     * controller执行后但未返回视图前调用此方法
     * 这里可在返回用户前对模型数据进行加工处理,比如这里加入公用信息以便页面显示
     *
     * @param request
     * @param response
     * @param arg2
     * @param arg3
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {
    }


    /**
     * Controller执行前调用此方法
     * 返回true表示继续执行，返回false中止执行，这里可以加入登录校验、权限拦截等
     *
     * @param request
     * @param response
     * @param arg2
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object arg2) throws Exception {
        // 从request中获取session
        HttpSession session = request.getSession();
        // 从session中获取username
        Object username = session.getAttribute("userName");
        // 判断username是否为null
        if (username != null) {
            // 如果不为空则放行
            return true;
        } else {
            // 如果为空则跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/login.html");
        }

        return false;
    }
}