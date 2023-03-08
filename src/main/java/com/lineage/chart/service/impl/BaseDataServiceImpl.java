package com.lineage.chart.service.impl;

import com.lineage.chart.entity.SelectModel;
import com.lineage.chart.entity.Tree;
import com.lineage.chart.mapper.BaseDataMapper;
import com.lineage.chart.service.BaseDataService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<SelectModel> queryTreeSelectModel(Integer activeTab) {
        List<Tree> treeIdList = mapper.queryTree();
        List<String> active3FilterId = Arrays.asList("C.elegans", "P.Marina");
        return treeIdList.stream()
                .filter(e -> !Integer.valueOf(3).equals(activeTab) && !active3FilterId.contains(e.getTreeId()))
                .map(e -> new SelectModel(e.getTreeId(), e.getTreeName()))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "geneSelect", key = "#treeId", unless = "#result == null || #result == 0")
    public List<SelectModel> queryGeneByTreeId(String treeId) {
        List<SelectModel> result = new ArrayList<>();
        List<String> treeIdList = mapper.queryGeneByTreeId(treeId);
        treeIdList.forEach(e -> result.add(new SelectModel(e, e)));
        return result;
    }

    @Override
    public Tree queryTreeById(String treeId) {
        return mapper.queryTreeById(treeId);
    }


}
