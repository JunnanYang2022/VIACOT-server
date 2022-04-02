package com.lineage.chart.vo;

import lombok.Data;

/**
 * @author YangJunNan
 * @description
 * @date 2021/3/23
 */
@Data
public class SearchVO {

    private String treeId;

    private String treeName;

    private String species;

    private String latinName;

    private String paper;

    private String eoa;

    private Integer cellNum;
}
