package com.lineage.chart.qo;

import lombok.Data;

/**
 * @author YangJunNan
 * @description
 * @date 2021/3/23
 */
@Data
public class SearchQo {

    /**
     * 物种
     */
    private String species;

    /**
     * 细胞数量级别
     */
    private Integer cellNum;
}
