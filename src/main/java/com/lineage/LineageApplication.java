package com.lineage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @description SpringBoot启动类
 * @author YangJunNan
 * @date 2020/12/6
 */
@EnableCaching
@SpringBootApplication
public class LineageApplication {

    public static void main(String[] args) {
        SpringApplication.run(LineageApplication.class, args);
    }

}
