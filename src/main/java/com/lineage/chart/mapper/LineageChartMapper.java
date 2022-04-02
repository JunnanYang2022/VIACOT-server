package com.lineage.chart.mapper;

import com.lineage.chart.entity.LineageTree;
import com.lineage.chart.qo.SearchQo;
import com.lineage.chart.vo.SearchVO;
import com.lineage.chart.vo.TreeChartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YangJunNan
 * @description 谱系图数据生成service
 * @date 2020/12/6
 */
@Mapper
public interface LineageChartMapper {

    /**
     * 根据treeId和节点名称查询出一条唯一数据
     *
     * @param treeId   树id
     * @param nodeName 节点名称
     * @return {@link TreeChartVO}
     */
    TreeChartVO queryOneByNodeName(@Param("treeId") String treeId, @Param("nodeName") String nodeName);

    /**
     * 查询节点数据
     *
     * @param treeId     树id 必填
     * @param generation 代数  不填默认从第一代查
     * @return List
     */
    List<TreeChartVO> queryLineageTreeData(@Param("treeId") String treeId, @Param("generation") Integer generation);

    /**
     * 批量入库
     *
     * @param target 入库list
     */
    void insertBatch(List<LineageTree> target);

    /**
     * 查询树
     *
     * @param qo 查询参数封装
     * @return List
     */
    List<SearchVO> search(@Param("qo") SearchQo qo);
}
