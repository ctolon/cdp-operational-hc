package com.bentego.cdputils.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirCapacityDto {

    @Getter
    @Setter
    private String hostId;

    @Getter
    @Setter
    private String hostname;

    @Getter
    @Setter
    private String roleType;

    @Getter
    @Setter
    private String dirLocation;

    @Getter
    @Setter
    private BigDecimal capacity;

    @Getter
    @Setter
    private BigDecimal capacityUsed;

    @Getter
    private BigDecimal capacityFree;

    @Getter
    private BigDecimal capacityUsedPercentage;

    @Getter
    private BigDecimal capacityFreePercentage;

    @Setter
    @Getter
    private BigDecimal warningThresholdInGb;

    private void setCapacityFree() {
        if (capacity == null || capacityUsed == null) {
            this.capacityFree = BigDecimal.ZERO;
        }
        else {
            this.capacityFree = capacity.subtract(capacityUsed);
        }
    }

    private void setCapacityUsedPercentage() {
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) == 0 || capacityUsed == null) {
            this.capacityUsedPercentage = BigDecimal.ZERO;
        } else {
            this.capacityUsedPercentage = capacityUsed.multiply(BigDecimal.valueOf(100)).divide(capacity, 2, RoundingMode.HALF_UP);
        }
    }

    private void setCapacityFreePercentage() {
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) == 0) {
            this.capacityFreePercentage = BigDecimal.ZERO;
        } else {
            this.capacityFreePercentage = this.getCapacityFree().multiply(BigDecimal.valueOf(100)).divide(capacity, 2, RoundingMode.HALF_UP);
        }
    }

    public void setPercentagesAndFree() {
        setCapacityFree();
        setCapacityUsedPercentage();
        setCapacityFreePercentage();
    }
}
