package com.lineage.chart.mapper;

import com.lineage.chart.entity.CellType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YangJunNan
 * @description
 * @date 2021/3/25
 */
@Mapper
public interface CellTypeMapper {
    /**
     * 根据树id获取细胞类型列表
     *
     * @param treeId 树id
     * @return list
     */
    List<CellType> selectListByTreeId(@Param("treeId") String treeId);
}
