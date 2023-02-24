package com.lineage.chart.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YangJunNan
 * @description
 * @date 2021/3/25
 */
@Mapper
public interface TreeMapper {
    /**
     * 根据基因名称  获得数id
     *
     * @param geneName 基因名称
     * @return list
     */
    List<String> getByGeneName(@Param("geneName") String geneName);
}
