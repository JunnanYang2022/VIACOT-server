package com.lineage.chart.service;

import com.lineage.chart.entity.CellType;
import com.lineage.chart.qo.ConstructQO;
import com.lineage.chart.qo.SearchQo;
import com.lineage.chart.vo.GeneCompareTreeChartVO;
import com.lineage.chart.vo.SearchVO;
import com.lineage.chart.vo.SimilarityVO;
import com.lineage.chart.vo.TreeChartVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author YangJunNan
 * @description 谱系图数据生成service
 * @date 2020/12/6
 */
public interface LineageChartService {
    /**
     * 构造谱系树数据
     *
     * @param qo 查询参数封装
     * @return TreeChartVO
     */
    List<TreeChartVO> constructLineageTreeData(ConstructQO qo);

    /**
     * 上传json文件解析到库
     *
     * @param file json文件
     * @throws IOException IO异常
     */
    void upload(MultipartFile file);

    /**
     * Newick文件解析
     *
     * @param file json文件
     * @throws IOException IO异常
     */
    List<TreeChartVO> uploadNewick(MultipartFile file) throws IOException;

    /**
     * 查询树
     *
     * @param qo 查询参数封装
     * @return List
     */
    List<SearchVO> search(SearchQo qo);

    /**
     * 构造基因表达对比 谱系树数据
     *
     * @param qo 查询参数封装
     * @return GeneCompareTreeChartVO
     */
    GeneCompareTreeChartVO constructGeneCompareTreeData(ConstructQO qo);

    /**
     * 构造谱系树对比数据
     *
     * @param qo 查询参数封装
     * @return GeneCompareTreeChartVO
     */
    GeneCompareTreeChartVO constructCompareTreeData(ConstructQO qo);

    /**
     * 计算相关系数
     *
     * @param qo 查询参数封装
     * @return SimilarityVO
     */
    SimilarityVO getSimilarity(ConstructQO qo);

    /**
     * 根据树id查询细胞类型列表
     *
     * @param treeId 树id
     * @return list
     */
    List<CellType> getCellTypeList(String treeId);

    String constructNewickData(String treeId) throws IOException;
}
