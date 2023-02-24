package com.lineage.chart.entity;

import java.util.Date;

/**
 * @author YangJunNan
 * @description
 * @date 2020/9/8
 */
public class User {
    /**
     * uuid
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 操作时间
     */
    private Date timeFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Date getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(Date timeFlag) {
        this.timeFlag = timeFlag;
    }
}
