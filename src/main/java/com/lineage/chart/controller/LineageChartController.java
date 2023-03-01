package com.lineage.chart.controller;

import com.lineage.chart.entity.CellType;
import com.lineage.chart.qo.ConstructQO;
import com.lineage.chart.qo.SearchQo;
import com.lineage.chart.service.LineageChartService;
import com.lineage.chart.vo.GeneCompareTreeChartVO;
import com.lineage.chart.vo.SearchVO;
import com.lineage.chart.vo.SimilarityVO;
import com.lineage.chart.vo.TreeChartVO;
import com.lineage.response.ResultBean;
import com.lineage.response.ResultHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YangJunNan
 * @description 谱系图数据生成controller
 * @date 2020/12/6
 */
@RestController
@RequestMapping("api/lineage/chart")
public class LineageChartController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LineageChartController.class);

    @Resource
    private LineageChartService service;

    @RequestMapping("/treeData")
    public ResultBean<Object> treeData(ConstructQO qo) {
        try {
            List<TreeChartVO> vo = service.constructLineageTreeData(qo);
            return ResultHandler.ok(vo);
        } catch (Exception e) {
            LOGGER.error("构造谱系树数据发生错误：", e);
            return ResultHandler.error("构造谱系树数据发生错误，请联系管理员！");
        }
    }

    @RequestMapping("/geneCompareTreeData")
    public ResultBean<Object> geneCompareTreeData(ConstructQO qo) {
        try {
            GeneCompareTreeChartVO vo = service.constructGeneCompareTreeData(qo);
            return ResultHandler.ok(vo);
        } catch (Exception e) {
            LOGGER.error("构造谱系树基因表达对比数据发生错误：", e);
            return ResultHandler.error("构造谱系树基因表达对比数据发生错误，请联系管理员！");
        }
    }

    @RequestMapping("/compareTreeData")
    public ResultBean<Object> compareTreeData(ConstructQO qo) {
        try {
            GeneCompareTreeChartVO vo = service.constructCompareTreeData(qo);
            return ResultHandler.ok(vo);
        } catch (Exception e) {
            LOGGER.error("构造谱系树对比数据发生错误：", e);
            return ResultHandler.error("构造谱系树对比数据发生错误，请联系管理员！");
        }
    }

    /**
     * 上传文件
     *
     * @param file 谱系树json文件
     * @return
     */
    @RequestMapping("/upload")
    public ResultBean<Object> uploadFile(MultipartFile file) {
        try {
            service.upload(file);
            return ResultHandler.okMsg("上传成功");
        } catch (Exception e) {
            LOGGER.error("上传文件出错:", e);
            return ResultHandler.error("上传失败，请稍后重试！");
        }
    }

    /**
     * 上传文件
     *
     * @param qo 查询参数封装
     * @return list
     */
    @RequestMapping("/search")
    public ResultBean<Object> search(SearchQo qo) {
        try {
            List<SearchVO> result = service.search(qo);
            return ResultHandler.ok(result);
        } catch (Exception e) {
            LOGGER.error("查询出错:", e);
            return ResultHandler.error("查询出错，请稍后重试！");
        }
    }

    /**
     * 计算两个基因的相关系数
     *
     * @param qo 计算两个基因的相关系数
     * @return list
     */
    @RequestMapping("/getSimilarity")
    public ResultBean<SimilarityVO> getSimilarity(ConstructQO qo) {
        try {
            SimilarityVO result = service.getSimilarity(qo);
            return ResultHandler.ok(result);
        } catch (Exception e) {
            LOGGER.error("计算两个基因的相关系数出错:", e);
            return ResultHandler.error("计算两个基因的相关系数出错，请稍后重试！");
        }
    }

    /**
     * 根据树id查询细胞类型列表
     *
     * @param treeId 树id
     * @return list
     */
    @RequestMapping("/getCellTypeList")
    public ResultBean<List<CellType>> getCellTypeList(String treeId) {
        try {
            if (StringUtils.isBlank(treeId)) {
                return ResultHandler.error("树id不可为空！");
            }
            List<CellType> result = service.getCellTypeList(treeId);
            return ResultHandler.ok(result);
        } catch (Exception e) {
            LOGGER.error("查询细胞类型列表出错:", e);
            return ResultHandler.error("查询细胞类型列表出错，请稍后重试！");
        }
    }

    /**
     * Newick文件上传并解析
     *
     * @param file 谱系树json文件
     * @return
     */
    @RequestMapping("/uploadNewick")
    public ResultBean<Object> uploadNewick(MultipartFile file) {
        try {
            List<TreeChartVO> vo = service.uploadNewick(file);
            return ResultHandler.ok(vo);
        } catch (Exception e) {
            LOGGER.error("上传文件出错:", e);
            return ResultHandler.error("上传失败，请稍后重试！");
        }
    }

}
