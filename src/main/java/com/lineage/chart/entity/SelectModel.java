package com.lineage.chart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description select数据模型
 * @author YangJunNan
 * @date 2020/10/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectModel implements Serializable {
    private static final long serialVersionUID = -1046810744040306728L;
    /**
     * 值
     */
    private String value;
    /**
     * 描述
     */
    private String desc;
}
