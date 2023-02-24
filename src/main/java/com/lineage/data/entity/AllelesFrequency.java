package com.lineage.data.entity;

import lombok.*;

/**
 * @author YangJunNan
 * @description
 * @date 2021/9/26
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class AllelesFrequency {
    private String nodeName;
    private String rawName;
    private String barcodeKey;
}
