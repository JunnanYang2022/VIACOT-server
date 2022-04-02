package com.lineage.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author YangJunNan
 * @description 返回实体统一封装
 * @date 2021/1/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultBean<T> {

    /**
     * 响应码
     */
    @Builder.Default
    private Integer code = 200;

    /**
     * 响应消息
     */
    @Builder.Default
    private String message = "处理成功";

    /**
     * 数据
     */
    private T data;

    /**
     * 是否成功
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return ResultEnum.SUCCESS.getCode().equals(code);
    }
}
