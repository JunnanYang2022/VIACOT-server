package com.lineage.chart.mapper;

import com.lineage.chart.entity.GenesExpression;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YangJunNan
 * @description
 * @date 2021/5/13
 */
@Mapper
public interface GenesExpressionMapper {
    /**
     * 查询某棵树某个基因表达情况
     *
     * @param treeId   数id
     * @param geneName 基因名称
     * @return {@link List<GenesExpression>}
     */
    List<GenesExpression> queryByTreeIdAndGeneName(@Param("treeId") String treeId, @Param("geneName") String geneName);
}
