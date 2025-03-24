package com.bentego.cdputils.dtos.healthcheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseHealthcheckDto {
    private String healthcheckName;
    private String healthcheckSummary;
    private String healthcheckExplanation;
    private Boolean healthcheckSuppressed;
}
