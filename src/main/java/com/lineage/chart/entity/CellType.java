package com.lineage.chart.entity;

import lombok.Data;

/**
 * @author YangJunNan
 * @description
 * @date 2022/4/23
 */
@Data
public class CellType {

    /**
     * 细胞类型
     */
    private String cellType;
    /**
     * 颜色
     */
    private String color;
    /**
     * 树id
     */
    private String treeId;
}
