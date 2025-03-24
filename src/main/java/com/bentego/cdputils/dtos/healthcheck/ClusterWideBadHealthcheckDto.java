package com.bentego.cdputils.dtos.healthcheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterWideBadHealthcheckDto {
    private List<HostBadHealthcheckDto> hostBadHealthcheckDtos;
    private List<RoleBadHealthcheckDto> roleBadHealthcheckDtos;
    private List<ServiceBadHealthcheckDto> serviceBadHealthcheckDtos;


}
