package com.bentego.cdputils.dtos.healthcheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceBadHealthcheckDto {

    private String serviceName;
    private String serviceDisplayName;
    private String serviceType;
    private BaseHealthcheckDto healthcheckDto;
}
