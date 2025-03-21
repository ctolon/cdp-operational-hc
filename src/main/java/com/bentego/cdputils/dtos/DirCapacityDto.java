package com.bentego.cdputils.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirCapacityDto {
    private String hostId;
    private String hostname;
    private String roleType;
    private String dirLocation;
    private BigDecimal capacity;
    private BigDecimal capacityUsed;
}
