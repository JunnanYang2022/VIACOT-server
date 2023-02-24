package com.lineage.chart.qo;

import lombok.Data;

/**
 * @author YangJunNan
 * @description
 * @date 2021/3/25
 */
@Data
public class ConstructQO {
    /**
     * 1-单棵谱系树 2-谱系树基因表达对比
     */
    private Integer activeTab;
    /**
     * 树id
     */
    private String treeId;
    /**
     * 基因名称1
     */
    private String geneName1;
    /**
     * 基因名称2
     */
    private String geneName2;

    /**
     * 节点名称 当此字段传值时，用此节点作为父节点构造数据
     */
    private String nodeName;

    /**
     * 树id1
     */
    private String treeId1;

    /**
     * 树id2
     */
    private String treeId2;
}
