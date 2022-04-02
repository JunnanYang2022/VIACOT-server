package com.lineage.response;


/**
 * @author YangJunNan
 * @description 返回实体封装操作类
 * @date 2021/1/29
 */
public class ResultHandler {

    /**
     * 成功时生成ResultBean对象
     *
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok() {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功时生成ResultBean对象
     *
     * @param data 数据
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok(T data) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功时生成ResultBean对象
     *
     * @param data    数据
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok(T data, String message) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), message, data);
    }


    /**
     * 成功时生成ResultBean对象
     *
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> okMsg(String message) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 成功单无数据返回时生成ResultBean对象
     *
     * @return ResultBean
     */
    public static <T> ResultBean<T> none() {
        return new ResultBean<>(ResultEnum.SUCCESS_NONE.getCode(), ResultEnum.SUCCESS_NONE.getMessage(), null);
    }

    /**
     * 失败时生成ResultBean对象
     *
     * @return ResultBean
     */
    public static <T> ResultBean<T> error() {
        return new ResultBean<>(ResultEnum.EXCEPTION.getCode(), "操作失败", null);
    }

    /**
     * 失败时生成ResultBean对象
     *
     * @param error 错误类型
     * @return ResultBean
     */
    public static <T> ResultBean<T> error(ResultEnum error) {
        return new ResultBean<>(error.getCode(), error.getMessage(), null);
    }

    /**
     * 失败时生成ResultBean对象
     *
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> error(String message) {
        return new ResultBean<>(ResultEnum.EXCEPTION.getCode(), message, null);
    }
}
