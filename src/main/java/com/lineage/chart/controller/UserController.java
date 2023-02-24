package com.lineage.chart.controller;

import com.lineage.chart.entity.User;
import com.lineage.chart.service.UserService;
import com.lineage.chart.vo.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author YangJunNan
 * @description 用户登录Controller
 * @date 2020/9/6
 */
@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService service;

    /**
     * 用户登录
     *
     * @param userParam 用户实体参数
     * @param session   session
     * @return
     */
    @RequestMapping("/login")
    public ResultResponse login(@RequestBody User userParam, HttpServletRequest request,
                                HttpServletResponse response, HttpSession session) {
        try {
            // 校验用户登录
            if (service.login(userParam, session)) {
                return ResultResponse.builder().code(ResultResponse.SUCCESS).msg("登陆成功！").build();
            }else {
                return ResultResponse.builder().code(ResultResponse.ERROR).msg("用户名或密码错误！").build();
            }
        } catch (Exception e) {
            LOGGER.error("登录请求发生错误：",e);
            return ResultResponse.builder().code(ResultResponse.ERROR).msg("发生错误，登录失败，请联系管理员！").build();
        }
    }

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

}
