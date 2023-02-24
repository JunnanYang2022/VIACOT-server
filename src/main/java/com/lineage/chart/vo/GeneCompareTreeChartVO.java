package com.lineage.chart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YangJunNan
 * @description echarts树节点数据
 * @date 2020/9/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneCompareTreeChartVO {
    /**
     * 树数据1 位于页面左边
     */
    private List<TreeChartVO> treeData1;
    /**
     * 树数据2 位于页面右边
     */
    private List<TreeChartVO> treeData2;
}
