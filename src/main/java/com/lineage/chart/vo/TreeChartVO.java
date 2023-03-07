package com.lineage.chart.vo;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(serialize = false)
    private String nodeId;
    /**
     * 父节点id
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String ancestorId;
    /**
     * 该节点为第几代
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private Integer generation;
    /**
     * 该节点为第几代
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String species;
    /**
     * 细胞类型
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String cellDiscription;

    /**
     * 节点名称
     */
    @JSONField(name = "label")
    private String name;
    /**
     * 展示数据
     */
    @JSONField(serialize = false)
    private String value;

    /**
     * 基因表达量
     */
    @JSONField(serialize = false)
    private Integer expressionOrBlot;

    /**
     * 细胞类型
     */
    @JSONField(serialize = false)
    private String cellType;

    /**
     * 子节点数据
     */
    private List<TreeChartVO> children;

    /**
     * 节点样式
     */
    @JSONField(serialize = false)
    private ItemStyle itemStyle;
}
