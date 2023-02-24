package com.lineage.chart.service;


import com.lineage.chart.entity.User;

import javax.servlet.http.HttpSession;

/**
 * @author YangJunNan
 * @description
 * @date 2020/9/8
 */
public interface UserService {

    /**
     * 登录操作登录成功返回
     * @param userParam 前端传过来的用户参数封装
     * @param session session
     * @return 登录成功返回true  登录失败返回false
     */
    boolean login(User userParam, HttpSession session);
}
