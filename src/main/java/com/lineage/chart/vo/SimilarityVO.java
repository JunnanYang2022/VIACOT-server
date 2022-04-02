package com.lineage.chart.vo;

import lombok.Data;

/**
 * @author YangJunNan
 * @description
 * @date 2021/12/5
 */
@Data
public class SimilarityVO {
    /**
     * 相关系数
     */
    private Double similarity;

    /**
     * 是否不准确
     */
    private Boolean inaccurate;

    /**
     * 节点数量
     */
    private Integer nodeNum;
}
