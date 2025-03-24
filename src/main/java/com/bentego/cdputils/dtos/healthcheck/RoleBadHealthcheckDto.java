package com.bentego.cdputils.dtos.healthcheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleBadHealthcheckDto {
    private String roleName;
    private String roleType;
    private String roleConfigGroupName;
    private String serviceName;
    private String serviceDisplayName;
    private String hostId;
    private String hostname;
    private BaseHealthcheckDto healthcheckDto;
}
