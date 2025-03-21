package com.bentego.cdputils.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HdfsCapacityDto {
    private BigDecimal dfsCapacity;
    private BigDecimal dfsCapacityUsed;
    private BigDecimal dfsCapacityUsedNonHdfs;
}
