package com.bentego.cdputils.service;

import com.bentego.cdputils.contants.api.CmApiView;
import com.bentego.cdputils.dtos.ServiceTypesDto;
import com.bentego.cdputils.dtos.healthcheck.*;
import com.bentego.cdputils.utils.CustomStringUtils;
import com.cloudera.api.swagger.ClustersResourceApi;
import com.cloudera.api.swagger.HostsResourceApi;
import com.cloudera.api.swagger.RolesResourceApi;
import com.cloudera.api.swagger.ServicesResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClusterGeneralHealthcheckService {

    Logger logger = LoggerFactory.getLogger(ClusterGeneralHealthcheckService.class);

    private final ClustersResourceApi clustersResourceApi;
    private final RolesResourceApi rolesResourceApi;

    public ClusterGeneralHealthcheckService(
            ClustersResourceApi clustersResourceApi,
            RolesResourceApi rolesResourceApi) {
        this.clustersResourceApi = clustersResourceApi;
        this.rolesResourceApi = rolesResourceApi;
    }

    public ClusterWideBadHealthcheckDto getClusterWideHealthcheck(
            String clusterName,
            ApiHostList apiHostList,
            ApiServiceList apiServiceList,
            List<String> serviceTypes
    ) throws ApiException {

        ClusterWideBadHealthcheckDto clusterWideHealthcheckDto = new ClusterWideBadHealthcheckDto();
        List<HostBadHealthcheckDto> hostBadHealthcheckDtos = new ArrayList<>();
        List<ServiceBadHealthcheckDto> serviceBadHealthcheckDtos = new ArrayList<>();
        List<RoleBadHealthcheckDto> roleBadHealthcheckDtos = new ArrayList<>();

        ApiCluster apiCluster = clustersResourceApi.readCluster(clusterName);
        // clusterWideHealthcheckDto.setClusterStatus(apiCluster.getEntityStatus().getValue());

        // Host based
        for (ApiHost apiHost : apiHostList.getItems()) {
            for (ApiHealthCheck healthCheck : apiHost.getHealthChecks()) {
                String hostHcName = healthCheck.getName();
                String hostHcExplanation = healthCheck.getExplanation();
                String hostHcSummary = healthCheck.getSummary().getValue();
                Boolean hostHcSuppressed = healthCheck.getSuppressed();

                if (hostHcSummary.equals("CONCERNING") || hostHcSummary.equals("BAD")) {

                    // Set host metadata
                    HostBadHealthcheckDto hostBadHealthcheckDto = new HostBadHealthcheckDto();
                    hostBadHealthcheckDto.setHostId(apiHost.getHostId());
                    hostBadHealthcheckDto.setHostname(apiHost.getHostname());
                    // get entity status also TODO

                    // Set host healthcheck model
                    BaseHealthcheckDto hostHealthcheckDto = new BaseHealthcheckDto();
                    hostHealthcheckDto.setHealthcheckName(hostHcName);
                    hostHealthcheckDto.setHealthcheckExplanation(hostHcExplanation);
                    hostHealthcheckDto.setHealthcheckSummary(hostHcSummary);
                    hostHealthcheckDto.setHealthcheckSuppressed(hostHcSuppressed);
                    hostBadHealthcheckDto.setHealthcheckDto(hostHealthcheckDto);

                    hostBadHealthcheckDtos.add(hostBadHealthcheckDto);
                }
            }
        }

        // Service Based
        for (ApiService apiService : apiServiceList.getItems()) {
            for (ApiHealthCheck healthCheck : apiService.getHealthChecks()) {
                String serviceHcName = healthCheck.getName();
                String serviceHcExplanation = healthCheck.getExplanation();
                String serviceHcSummary = healthCheck.getSummary().getValue();
                Boolean serviceHcSuppressed = healthCheck.getSuppressed();

                if (serviceHcSummary.equals("CONCERNING") || serviceHcSummary.equals("BAD")) {

                    // Set service metadata
                    ServiceBadHealthcheckDto serviceBadHealthcheckDto = new ServiceBadHealthcheckDto();
                    serviceBadHealthcheckDto.setServiceName(apiService.getName());
                    serviceBadHealthcheckDto.setServiceDisplayName(apiService.getDisplayName());
                    serviceBadHealthcheckDto.setServiceType(apiService.getType());
                    // get entity status also TODO

                    // Set service healthcheck model
                    BaseHealthcheckDto serviceHealthcheckDto = new BaseHealthcheckDto();
                    serviceHealthcheckDto.setHealthcheckName(serviceHcName);
                    serviceHealthcheckDto.setHealthcheckExplanation(serviceHcExplanation);
                    serviceHealthcheckDto.setHealthcheckSummary(serviceHcSummary);
                    serviceHealthcheckDto.setHealthcheckSuppressed(serviceHcSuppressed);
                    serviceBadHealthcheckDto.setHealthcheckDto(serviceHealthcheckDto);

                    serviceBadHealthcheckDtos.add(serviceBadHealthcheckDto);
                }
            }

        }

        // Role Based
        for (String serviceName : serviceTypes) {
            ApiRoleList apiRoleList = rolesResourceApi.readRoles(clusterName, serviceName, null, CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);
            for (ApiRole apiRole : apiRoleList.getItems()) {
                for (ApiHealthCheck healthCheck : apiRole.getHealthChecks()) {
                    String roleHcName = healthCheck.getName();
                    String roleHcExplanation = healthCheck.getExplanation();
                    String roleHcSummary = healthCheck.getSummary().getValue();
                    Boolean roleHcSuppressed = healthCheck.getSuppressed();

                    if (roleHcSummary.equals("CONCERNING") || roleHcSummary.equals("BAD")) {

                        RoleBadHealthcheckDto roleBadHealthcheckDto = new RoleBadHealthcheckDto();

                        // set role metadata
                        roleBadHealthcheckDto.setRoleName(apiRole.getName());
                        roleBadHealthcheckDto.setRoleType(apiRole.getType());
                        roleBadHealthcheckDto.setRoleConfigGroupName(CustomStringUtils.removeBaseSuffix(apiRole.getRoleConfigGroupRef().getRoleConfigGroupName()));
                        roleBadHealthcheckDto.setServiceName(apiRole.getServiceRef().getServiceType());
                        roleBadHealthcheckDto.setServiceDisplayName(apiRole.getServiceRef().getServiceDisplayName());
                        roleBadHealthcheckDto.setHostId(apiRole.getHostRef().getHostId());
                        roleBadHealthcheckDto.setHostname(apiRole.getHostRef().getHostname());

                        // Set role healthcheck model
                        BaseHealthcheckDto roleHealthcheckDto = new BaseHealthcheckDto();
                        roleHealthcheckDto.setHealthcheckName(roleHcName);
                        roleHealthcheckDto.setHealthcheckExplanation(roleHcExplanation);
                        roleHealthcheckDto.setHealthcheckSummary(roleHcSummary);
                        roleHealthcheckDto.setHealthcheckSuppressed(roleHcSuppressed);
                        roleBadHealthcheckDto.setHealthcheckDto(roleHealthcheckDto);

                        roleBadHealthcheckDtos.add(roleBadHealthcheckDto);
                    }
                }
            }
        }
        clusterWideHealthcheckDto.setHostBadHealthcheckDtos(hostBadHealthcheckDtos);
        clusterWideHealthcheckDto.setServiceBadHealthcheckDtos(serviceBadHealthcheckDtos);
        clusterWideHealthcheckDto.setRoleBadHealthcheckDtos(roleBadHealthcheckDtos);

        return clusterWideHealthcheckDto;
    }
}

