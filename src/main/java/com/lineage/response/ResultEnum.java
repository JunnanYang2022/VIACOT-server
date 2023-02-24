package com.lineage.response;

/**
 * @author YangJunNan
 * @description server返回统一状态码封装
 * @date 2021/1/29
 */
public enum ResultEnum {

    /**
     * 状态码
     */
    SUCCESS(200, "操作成功"),
    SUCCESS_NONE(204, "无执行包"),
    EXCEPTION(501, "业务异常");

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取响应码
     *
     * @return Integer
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取响应消息
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }
}
