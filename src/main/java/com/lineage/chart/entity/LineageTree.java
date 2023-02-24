package com.lineage.chart.entity;

import com.lineage.chart.vo.TreeChartVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author YangJunNan
 * @description
 * @date 2020/9/8
 */
@Data
public class LineageTree {
    /**
     * 自增id
     */
    private Integer id;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 父节点id
     */
    private String ancestorId;
    /**
     * 子节点id，多个子节点id用逗号分隔
     */
    private String childId;
    /**
     * 该节点为第几代
     */
    private Integer generation;
    /**
     * 细胞来源和数据序号
     */
    private String species;
    /**
     * 细胞类型
     */
    private String cellType;
    /**
     * 树id
     */
    private String treeId;
    /**
     * 入库时间
     */
    private Date timeFlag;

    /**
     * 子节点数据
     */
    private List<LineageTree> children;

    public String toSqlString() {
        return "INSERT tb_lineage_tree(node_id ,node_name ,ancestor_id ,generation ,tree_id)" +
                "VALUES(" +
                "'" + nodeId + "'," +
                "'" + nodeName + "'," +
                "'" + ancestorId + "'," +
                 + generation + "," +
                "'" + treeId + "'" +
                ");\n";
    }
}
