package com.bentego.cdputils.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceTypesDto {
    private String hostId;
    private String hostName;
    private String serviceName;
    private String serviceType;
    private String serviceDisplayName;
    private String roleState; // STARTED
    private String roleName;
    private String roleConfigGroupName;
    private SslCertificateDetailsDto sslDetails;
}
