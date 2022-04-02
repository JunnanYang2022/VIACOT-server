package com.lineage.chart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YangJunNan
 * @description
 * @date 2021/5/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStyle {

    private String borderColor = "rgba(0, 0, 0, 1)";

    private Integer  borderWidth;

    public ItemStyle(String borderColor) {
        this.borderColor = borderColor;
    }
}
