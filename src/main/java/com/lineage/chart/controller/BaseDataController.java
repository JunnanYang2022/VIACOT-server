package com.lineage.chart.controller;

import com.lineage.chart.entity.SelectModel;
import com.lineage.chart.entity.Tree;
import com.lineage.chart.qo.ConstructQO;
import com.lineage.chart.service.BaseDataService;
import com.lineage.response.ResultBean;
import com.lineage.response.ResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YangJunNan
 * @description 基础数据下拉框接口集合
 * @date 2020/10/29
 */
@Slf4j
@RestController
@RequestMapping("/api/lineage/baseData")
public class BaseDataController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseDataController.class);

    @Resource
    private BaseDataService service;

    /**
     * 获取树下拉框数据
     *
     * @return ResultBean
     */
    @RequestMapping("/tree")
    public ResultBean<Object> tree(ConstructQO qo) {
        try {
            List<SelectModel> vo = service.queryTreeSelectModel(qo.getActiveTab());
            return ResultHandler.ok(vo);
        } catch (Exception e) {
            LOGGER.error("树基础下拉框数据：", e);
            return ResultHandler.error("构造树基础下拉框数据发生错误，请联系管理员！");
        }
    }

    /**
     * 获取树下拉框数据
     *
     * @return ResultBean
     */
    @RequestMapping("/treeById")
    public ResultBean<Object> treeById(ConstructQO qo) {
        try {
            Tree tree = service.queryTreeById(qo.getTreeId());
            return ResultHandler.ok(tree);
        } catch (Exception e) {
            LOGGER.error("树基础下拉框数据：", e);
            return ResultHandler.error("构造树基础下拉框数据发生错误，请联系管理员！");
        }
    }

    /**
     * 根据树id获取该树所有基因的下拉框数据
     *
     * @return ResultBean
     */
    @RequestMapping("/geneByTreeId")
    public ResultBean<Object> geneByTreeId(String treeId) {
        try {
            if (StringUtils.isBlank(treeId)) {
                return ResultHandler.ok();
            }

            List<SelectModel> vo = service.queryGeneByTreeId(treeId);
            return ResultHandler.ok(vo);
        } catch (Exception e) {
            LOGGER.error("基因的下拉框数据：", e);
            return ResultHandler.error("构造基因的下拉框数据发生错误，请联系管理员！");
        }
    }

}
