package com.lineage.chart.service;

import com.lineage.chart.entity.SelectModel;

import java.util.List;

/**
 * @author YandJunNan
 * @description
 * @date 2020/12/28
 */
public interface BaseDataService {

    /**
     * 查询树下拉框模型数据
     *
     * @return List
     */
    List<SelectModel> queryTreeSelectModel(Integer activeTab);

    /**
     * 查询某棵树的基因下拉框模型数据
     *
     * @param treeId 树id
     * @return List
     */
    List<SelectModel> queryGeneByTreeId(String treeId);
}
