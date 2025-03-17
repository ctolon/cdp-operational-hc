package com.bentego.cdputils.configuration;



import com.cloudera.api.swagger.ActivitiesResourceApi;
import com.cloudera.api.swagger.AllHostsResourceApi;
import com.cloudera.api.swagger.AuthRoleMetadatasResourceApi;
import com.cloudera.api.swagger.AuthRolesResourceApi;
import com.cloudera.api.swagger.AuthServiceRoleCommandsResourceApi;
import com.cloudera.api.swagger.AuthServiceRoleConfigGroupsResourceApi;
import com.cloudera.api.swagger.AuthServiceRolesResourceApi;
import com.cloudera.api.swagger.BatchResourceApi;
import com.cloudera.api.swagger.CdpResourceApi;
import com.cloudera.api.swagger.ClouderaManagerResourceApi;
import com.cloudera.api.swagger.ClustersResourceApi;
import com.cloudera.api.swagger.CertManagerResourceApi;
import com.cloudera.api.swagger.CmPeersResourceApi;
import com.cloudera.api.swagger.CommandsResourceApi;
import com.cloudera.api.swagger.ControlPlanesResourceApi;
import com.cloudera.api.swagger.DashboardsResourceApi;
import com.cloudera.api.swagger.DataContextsResourceApi;
import com.cloudera.api.swagger.EventsResourceApi;
import com.cloudera.api.swagger.ExternalAccountsResourceApi;
import com.cloudera.api.swagger.ExternalUserMappingsResourceApi;
import com.cloudera.api.swagger.HostTemplatesResourceApi;
import com.cloudera.api.swagger.HostsResourceApi;
import com.cloudera.api.swagger.ImpalaQueriesResourceApi;
import com.cloudera.api.swagger.MgmtRoleCommandsResourceApi;
import com.cloudera.api.swagger.MgmtRoleConfigGroupsResourceApi;
import com.cloudera.api.swagger.MgmtRolesResourceApi;
import com.cloudera.api.swagger.MgmtServiceResourceApi;
import com.cloudera.api.swagger.NameservicesResourceApi;
import com.cloudera.api.swagger.ParcelResourceApi;
import com.cloudera.api.swagger.ParcelsResourceApi;
import com.cloudera.api.swagger.ProcessResourceApi;
import com.cloudera.api.swagger.ReplicationsResourceApi;
import com.cloudera.api.swagger.RoleCommandsResourceApi;
import com.cloudera.api.swagger.RoleConfigGroupsResourceApi;
import com.cloudera.api.swagger.RolesResourceApi;
import com.cloudera.api.swagger.ServicesResourceApi;
import com.cloudera.api.swagger.SnapshotsResourceApi;
import com.cloudera.api.swagger.TagsResourceApi;
import com.cloudera.api.swagger.TimeSeriesResourceApi;
import com.cloudera.api.swagger.ToolsResourceApi;
import com.cloudera.api.swagger.UsersResourceApi;
import com.cloudera.api.swagger.WatchedDirResourceApi;
import com.cloudera.api.swagger.YarnApplicationsResourceApi;

import com.cloudera.api.swagger.client.ApiClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CmApiConfiguration {

    private final CmApiClientConfig apiClientConfig;

    public CmApiConfiguration(CmApiClientConfig apiClientConfig) {
        this.apiClientConfig = apiClientConfig;
    }

    @Bean
    public ApiClient apiClient() {
        ApiClient cmClient = com.cloudera.api.swagger.client.Configuration.getDefaultApiClient();
        cmClient.setBasePath(apiClientConfig.getBaseUrl());
        cmClient.setUsername(apiClientConfig.getUsername());
        cmClient.setPassword(apiClientConfig.getPassword());
        return cmClient;
    }

    @Bean
    public ActivitiesResourceApi activitiesResourceApi(ApiClient apiClient) {
        return new ActivitiesResourceApi(apiClient);
    }

    @Bean
    public AllHostsResourceApi allHostsResourceApi(ApiClient apiClient) {
        return new AllHostsResourceApi(apiClient);
    }

    @Bean
    public AuthRoleMetadatasResourceApi authRoleMetadatasResourceApi(ApiClient apiClient) {
        return new AuthRoleMetadatasResourceApi(apiClient);
    }

    @Bean
    public AuthRolesResourceApi authRolesResourceApi(ApiClient apiClient) {
        return new AuthRolesResourceApi(apiClient);
    }

    @Bean
    public AuthServiceRoleCommandsResourceApi authServiceRoleCommandsResourceApi(ApiClient apiClient) {
        return new AuthServiceRoleCommandsResourceApi(apiClient);
    }

    @Bean
    public AuthServiceRoleConfigGroupsResourceApi authServiceRoleConfigGroupsResourceApi(ApiClient apiClient) {
        return new AuthServiceRoleConfigGroupsResourceApi(apiClient);
    }

    @Bean
    public AuthServiceRolesResourceApi authServiceRolesResourceApi(ApiClient apiClient) {
        return new AuthServiceRolesResourceApi(apiClient);
    }

    @Bean
    public BatchResourceApi batchResourceApi(ApiClient apiClient) {
        return new BatchResourceApi(apiClient);
    }

    @Bean
    public CdpResourceApi cdpResourceApi(ApiClient apiClient) {
        return new CdpResourceApi(apiClient);
    }

    @Bean
    public ClouderaManagerResourceApi clouderaManagerResourceApi(ApiClient apiClient) {
        return new ClouderaManagerResourceApi(apiClient);
    }

    @Bean
    public ClustersResourceApi clustersResourceApi(ApiClient apiClient) {
        return new ClustersResourceApi(apiClient);
    }

    @Bean
    public CertManagerResourceApi certManagerResourceApi(ApiClient apiClient) {
        return new CertManagerResourceApi(apiClient);
    }

    @Bean
    public CmPeersResourceApi cmPeersResourceApi(ApiClient apiClient) {
        return new CmPeersResourceApi(apiClient);
    }

    @Bean
    public CommandsResourceApi commandsResourceApi(ApiClient apiClient) {
        return new CommandsResourceApi(apiClient);
    }

    @Bean
    public ControlPlanesResourceApi controlPlanesResourceApi(ApiClient apiClient) {
        return new ControlPlanesResourceApi(apiClient);
    }

    @Bean
    public DashboardsResourceApi dashboardsResourceApi(ApiClient apiClient) {
        return new DashboardsResourceApi(apiClient);
    }

    @Bean
    public DataContextsResourceApi dataContextsResourceApi(ApiClient apiClient) {
        return new DataContextsResourceApi(apiClient);
    }

    @Bean
    public EventsResourceApi eventsResourceApi(ApiClient apiClient) {
        return new EventsResourceApi(apiClient);
    }

    @Bean
    public ExternalAccountsResourceApi externalAccountsResourceApi(ApiClient apiClient) {
        return new ExternalAccountsResourceApi(apiClient);
    }

    @Bean
    public ExternalUserMappingsResourceApi externalUserMappingsResourceApi(ApiClient apiClient) {
        return new ExternalUserMappingsResourceApi(apiClient);
    }

    @Bean
    public HostTemplatesResourceApi hostTemplatesResourceApi(ApiClient apiClient) {
        return new HostTemplatesResourceApi(apiClient);
    }

    @Bean
    public HostsResourceApi hostsResourceApi(ApiClient apiClient) {
        return new HostsResourceApi(apiClient);
    }

    @Bean
    public ImpalaQueriesResourceApi impalaQueriesResourceApi(ApiClient apiClient) {
        return new ImpalaQueriesResourceApi(apiClient);
    }

    @Bean
    public MgmtRoleCommandsResourceApi mgmtRoleCommandsResourceApi(ApiClient apiClient) {
        return new MgmtRoleCommandsResourceApi(apiClient);
    }

    @Bean
    public MgmtRoleConfigGroupsResourceApi mgmtRoleConfigGroupsResourceApi(ApiClient apiClient) {
        return new MgmtRoleConfigGroupsResourceApi(apiClient);
    }

    @Bean
    public MgmtRolesResourceApi mgmtRolesResourceApi(ApiClient apiClient) {
        return new MgmtRolesResourceApi(apiClient);
    }

    @Bean
    public MgmtServiceResourceApi mgmtServiceResourceApi(ApiClient apiClient) {
        return new MgmtServiceResourceApi(apiClient);
    }

    @Bean
    public NameservicesResourceApi nameservicesResourceApi(ApiClient apiClient) {
        return new NameservicesResourceApi(apiClient);
    }

    @Bean
    public ParcelResourceApi parcelResourceApi(ApiClient apiClient) {
        return new ParcelResourceApi(apiClient);
    }

    @Bean
    public ParcelsResourceApi parcelsResourceApi(ApiClient apiClient) {
        return new ParcelsResourceApi(apiClient);
    }

    @Bean
    public ProcessResourceApi processResourceApi(ApiClient apiClient) {
        return new ProcessResourceApi(apiClient);
    }

    @Bean
    public ReplicationsResourceApi replicationsResourceApi(ApiClient apiClient) {
        return new ReplicationsResourceApi(apiClient);
    }

    @Bean
    public RoleCommandsResourceApi roleCommandsResourceApi(ApiClient apiClient) {
        return new RoleCommandsResourceApi(apiClient);
    }

    @Bean
    public RoleConfigGroupsResourceApi roleConfigGroupsResourceApi(ApiClient apiClient) {
        return new RoleConfigGroupsResourceApi(apiClient);
    }

    @Bean
    public RolesResourceApi rolesResourceApi(ApiClient apiClient) {
        return new RolesResourceApi(apiClient);
    }

    @Bean
    public ServicesResourceApi servicesResourceApi(ApiClient apiClient) {
        return new ServicesResourceApi(apiClient);
    }

    @Bean
    public SnapshotsResourceApi snapshotsResourceApi(ApiClient apiClient) {
        return new SnapshotsResourceApi(apiClient);
    }

    @Bean
    public TagsResourceApi tagsResourceApi(ApiClient apiClient) {
        return new TagsResourceApi(apiClient);
    }

    @Bean
    public TimeSeriesResourceApi timeSeriesResourceApi(ApiClient apiClient) {
        return new TimeSeriesResourceApi(apiClient);
    }

    @Bean
    public ToolsResourceApi toolsResourceApi(ApiClient apiClient) {
        return new ToolsResourceApi(apiClient);
    }

    @Bean
    public UsersResourceApi usersResourceApi(ApiClient apiClient) {
        return new UsersResourceApi(apiClient);
    }

    @Bean
    public WatchedDirResourceApi watchedDirResourceApi(ApiClient apiClient) {
        return new WatchedDirResourceApi(apiClient);
    }

    @Bean
    public YarnApplicationsResourceApi yarnApplicationsResourceApi(ApiClient apiClient) {
        return new YarnApplicationsResourceApi(apiClient);
    }

}
