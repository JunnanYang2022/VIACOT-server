package com.lineage.chart.entity;

import lombok.Data;

/**
 * @author YangJunNan
 * @description
 * @date 2021/3/25
 */
@Data
public class Tree {
    /**
     * 树id
     */
    private String treeId;
    /**
     * 树名称
     */
    private String treeName;
    /**
     * 物种
     */
    private String species;
    /**
     * 拉丁名称
     */
    private String latinName;
    /**
     * 文献
     */
    private String paper;
}
