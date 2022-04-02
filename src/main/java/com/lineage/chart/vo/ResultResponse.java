package com.lineage.chart.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description 统一返回实体
 * @author YangJunNan
 * @date 2020/12/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> {

    private static final long serialVersionUID = -7706341963776510224L;

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";

    @Builder.Default
    private String id = "-1";
    @Builder.Default
    private Integer status = 0;
    @Builder.Default
    private String code = SUCCESS;
    @Builder.Default
    private String msg = "";

    private T data;
}
