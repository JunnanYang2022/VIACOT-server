package com.lineage.chart.mapper;

import com.lineage.chart.entity.Tree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YangJunNan
 * @description
 * @date 2020/12/28
 */
@Mapper
public interface BaseDataMapper {
    /**
     * 从数据表查树id
     *
     * @return 树id集合
     */
    List<Tree> queryTree();

    /**
     * 查询某棵树的基因
     *
     * @param treeId 树id
     * @return 基因名称集合
     */
    List<String> queryGeneByTreeId(@Param("treeId") String treeId);
}
