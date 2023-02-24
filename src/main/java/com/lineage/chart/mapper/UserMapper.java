package com.lineage.chart.mapper;

import com.lineage.chart.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author YangJunNan
 * @description
 * @date 2020/9/8
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查出用户信息
     * @param userName 用户名
     * @return
     */
    User queryByUserName(@Param("userName") String userName);
}
