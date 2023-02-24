package com.lineage.chart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author YangJunNan
 * @description
 * @date 2021/5/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenesExpression {
    /**
     * 树id
     */
    private String treeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 基因名称
     */
    private String geneName;

    /**
     * 基因表达量
     */
    private Integer expressionOrBlot;

    public GenesExpression(Integer expressionOrBlot) {
        this.expressionOrBlot = expressionOrBlot;
    }

    @Override
    public String toString() {
        return "INSERT INTO `tb_genes_expression` VALUES (null, '" + nodeName + "', " + expressionOrBlot + ", '" + geneName +
                "', '" + treeId + "');\n";

    }
}
