package com.lineage.chart.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lineage.chart.entity.CellType;
import com.lineage.chart.entity.ItemStyle;
import lombok.Data;

import java.util.List;

/**
 * @author YangJunNan
 * @description echarts树节点数据
 * @date 2020/9/8
 */
@Data
public class TreeChartVO {
    /**
     * 节点id
     */
    @JsonIgnore
    private String nodeId;
    /**
     * 父节点id
     */
    @JsonIgnore
    private String ancestorId;
    /**
     * 该节点为第几代
     */
    @JsonIgnore
    private Integer generation;
    /**
     * 该节点为第几代
     */
    @JsonIgnore
    private String species;
    /**
     * 细胞类型
     */
    @JsonIgnore
    private String cellDiscription;

    /**
     * 节点名称
     */
    private String name;
    /**
     * 展示数据
     */
    private String value;

    /**
     * 基因表达量
     */
    private Integer expressionOrBlot;

    /**
     * 细胞类型
     */
    private String cellType;

    /**
     * 子节点数据
     */
    private List<TreeChartVO> children;

    /**
     * 节点样式
     */
    private ItemStyle itemStyle;
}
