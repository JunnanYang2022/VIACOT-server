package com.lineage.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YangJunNan
 * @description
 * @date 2021/7/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplement {
    private String gene;
    private String id;
    private String lineage;
    private String estimate;
}
