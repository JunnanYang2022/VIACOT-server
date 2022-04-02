package com.lineage.chart.service.impl;

import com.lineage.chart.entity.User;
import com.lineage.chart.mapper.UserMapper;
import com.lineage.chart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author YangJunNan
 * @description
 * @date 2020/9/8
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper mapper;

    @Override
    public boolean login(User userParam, HttpSession session) {

        User user = mapper.queryByUserName(userParam.getUserName());
        if (user == null){
            return false;
        }
        //密码校验成功，将userName存到session中，并返回true
        if (userParam.getUserPassword().equals(user.getUserPassword())) {
            // 把用户名放到session中
            session.setAttribute("userName", userParam);
            return true;
        }
        return false;
    }
}
