package com.lineage.chart.service.impl;

import com.lineage.chart.entity.SelectModel;
import com.lineage.chart.entity.Tree;
import com.lineage.chart.mapper.BaseDataMapper;
import com.lineage.chart.service.BaseDataService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YandJunNan
 * @description
 * @date 2020/12/28
 */
@Service
public class BaseDataServiceImpl implements BaseDataService {

    @Resource
    private BaseDataMapper mapper;

    @Override
    public List<SelectModel> queryTreeSelectModel() {
        List<SelectModel> result = new ArrayList<>();
        List<Tree> treeIdList = mapper.queryTree();
        treeIdList.forEach(e -> result.add(new SelectModel(e.getTreeId(), e.getTreeName())));
        return result;
    }

    @Override
    @Cacheable(value = "geneSelect", key = "#treeId", unless = "#result == null || #result == 0")
    public List<SelectModel> queryGeneByTreeId(String treeId) {
        List<SelectModel> result = new ArrayList<>();
        List<String> treeIdList = mapper.queryGeneByTreeId(treeId);
        treeIdList.forEach(e -> result.add(new SelectModel(e, e)));
        return result;
    }


}
