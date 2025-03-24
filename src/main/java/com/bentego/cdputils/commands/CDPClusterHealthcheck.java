package com.bentego.cdputils.commands;
import com.bentego.cdputils.builder.TimeseriesQueryBuilder;
import com.bentego.cdputils.configuration.HealthcheckReportConfig;
import com.bentego.cdputils.contants.api.CmApiView;
import com.bentego.cdputils.contants.roleconfig.HdfsRoleConfigGroupName;
import com.bentego.cdputils.dtos.DirCapacityDto;
import com.bentego.cdputils.dtos.HdfsCapacityDto;
import com.bentego.cdputils.dtos.ServiceTypesDto;
import com.bentego.cdputils.dtos.SslCertificateDetailsDto;
import com.bentego.cdputils.enums.DataUnit;
import com.bentego.cdputils.enums.RoleConfigUIBinding;
import com.bentego.cdputils.service.*;
import com.bentego.cdputils.utils.CustomStringUtils;
import com.cloudera.api.swagger.*;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.google.gson.Gson;
import org.springframework.shell.standard.ShellOption;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
public class CDPClusterHealthcheck {

    private final Gson gson;

    private final HealthcheckReportConfig healthcheckReportConfig;

    private final FileManagerService fileManagerService;
    private final SSLCertificateService sslCertificateService;
    private final ShellCommandExecutorService shellCommandExecutorService;
    private final InspectPerformanceService inspectPerformanceService;
    private final TimeSeriesService timeSeriesService;

    private final ServicesResourceApi servicesResourceApi;
    private final RoleConfigGroupsResourceApi roleConfigGroupsResourceApi;
    private final AllHostsResourceApi allHostsResourceApi;
    private final CertManagerResourceApi certManagerResourceApi;
    private final ClouderaManagerResourceApi clouderaManagerResourceApi;
    private final ClustersResourceApi clustersResourceApi;
    private final HostsResourceApi hostsResourceApi;
    private final RolesResourceApi rolesResourceApi;


    public CDPClusterHealthcheck(
            Gson gson,
            HealthcheckReportConfig healthcheckReportConfig,
            FileManagerService fileManagerService,
            SSLCertificateService sslCertificateService,
            ShellCommandExecutorService shellCommandExecutorService,
            InspectPerformanceService inspectPerformanceService,
            TimeSeriesService timeSeriesService,
            ServicesResourceApi servicesResourceApi,
            RoleConfigGroupsResourceApi roleConfigGroupsResourceApi,
            AllHostsResourceApi allHostsResourceApi,
            CertManagerResourceApi certManagerResourceApi,
            ClouderaManagerResourceApi clouderaManagerResourceApi,
            ClustersResourceApi clustersResourceApi,
            HostsResourceApi hostsResourceApi,
            RolesResourceApi rolesResourceApi

    ) {
        this.gson = gson;
        this.healthcheckReportConfig = healthcheckReportConfig;
        this.fileManagerService = fileManagerService;
        this.sslCertificateService = sslCertificateService;
        this.shellCommandExecutorService = shellCommandExecutorService;
        this.inspectPerformanceService = inspectPerformanceService;
        this.timeSeriesService = timeSeriesService;
        this.servicesResourceApi = servicesResourceApi;
        this.roleConfigGroupsResourceApi = roleConfigGroupsResourceApi;
        this.allHostsResourceApi = allHostsResourceApi;
        this.certManagerResourceApi = certManagerResourceApi;
        this.clouderaManagerResourceApi = clouderaManagerResourceApi;
        this.clustersResourceApi = clustersResourceApi;
        this.hostsResourceApi = hostsResourceApi;
        this.rolesResourceApi = rolesResourceApi;
    }

    // defaultValue = ShellOption.NULL
    @ShellMethod(key = "healthcheck", value = "CDP Healthcheck Command")
    public ApiCommand cdpOperationalHealthcheck (@ShellOption(value = { "--cluster-name", "-c" }) String clusterName) throws ApiException, IOException {


        // Create Healthcheck report dir
        fileManagerService.mkdirIfNotExistsAs755(healthcheckReportConfig.getOutputDir());

        // Get ServiceList
        List<String> serviceTypes = new ArrayList<>();
        List<String> servicesInMaintenanceMode = new ArrayList<>();
        ApiServiceList serviceList = servicesResourceApi.readServices(clusterName, CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);
        for (ApiService apiService: serviceList.getItems()) {
            serviceTypes.add(apiService.getName());
            if (apiService.getMaintenanceMode()) {
                servicesInMaintenanceMode.add(apiService.getName());
            }
        }

        // Get RoleList
        List<ServiceTypesDto> serviceTypesDtos = new ArrayList<>();
        for (String serviceName: serviceTypes) {
            for (ApiRole apiRole: rolesResourceApi.readRoles(clusterName, serviceName, null, CmApiView.SUMMARY).getItems()) {

                ServiceTypesDto serviceTypesDto = new ServiceTypesDto();
                serviceTypesDto.setHostId(apiRole.getHostRef().getHostId());
                serviceTypesDto.setHostName(apiRole.getHostRef().getHostname());
                serviceTypesDto.setServiceName(serviceName);
                serviceTypesDto.setServiceType(apiRole.getServiceRef().getServiceType());
                serviceTypesDto.setServiceDisplayName(apiRole.getServiceRef().getServiceDisplayName());
                serviceTypesDto.setRoleState(apiRole.getRoleState().getValue());
                serviceTypesDto.setRoleName(apiRole.getName());
                serviceTypesDto.setRoleConfigGroupName(CustomStringUtils.removeBaseSuffix(apiRole.getRoleConfigGroupRef().getRoleConfigGroupName()));
                serviceTypesDtos.add(serviceTypesDto);

                // Role Healthchecks for all services
                //for (ApiHealthCheck healthCheck :apiRole.getHealthChecks()) {
                //    if healthCheck.
                //}
            }
        }

        // Check CDP UI Certificates
        List<ServiceTypesDto> modifiedServiceTypesDtos = new ArrayList<>();
        for (ServiceTypesDto serviceTypesDto: serviceTypesDtos) {
            Optional<RoleConfigUIBinding> roleConfigUIBinding = RoleConfigUIBinding.findByRoleConfigName(serviceTypesDto.getRoleConfigGroupName());
            if (roleConfigUIBinding.isPresent()) {
                RoleConfigUIBinding existRoleConfigUIBinding = roleConfigUIBinding.get();
                //String webInterfaceUrl = "http://" + serviceTypesDto.getHostName() + ":" + existRoleConfigUIBinding.getPort();
                SslCertificateDetailsDto sslCertificateDetailsDto = sslCertificateService.getSSLCertificateDetails(serviceTypesDto.getHostName(), Integer.parseInt(existRoleConfigUIBinding.getPort()));
                serviceTypesDto.setSslDetails(sslCertificateDetailsDto);
                modifiedServiceTypesDtos.add(serviceTypesDto);
            }
        }

        // return modifiedServiceTypesDtos;


        // HDFS Role Config list for checking and finding directories path
        String hdfsNamenodeDir = "";
        String hdfsDatanodeDir = "";
        String hdfsJournalnodeDir = "";

        if (serviceTypes.contains("hdfs")) {

            // Namenode
            ApiRoleConfigGroup hdfsNamenodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup(clusterName, HdfsRoleConfigGroupName.NAMENODE, "hdfs");
            for (ApiConfig apiConfig: hdfsNamenodeRoleConfigGroup.getConfig().getItems()) {
                if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                    hdfsNamenodeDir = apiConfig.getValue();
                }
            }

            // Datanode
            ApiRoleConfigGroup hdfsDatanodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup(clusterName, HdfsRoleConfigGroupName.DATANODE, "hdfs");
            for (ApiConfig apiConfig: hdfsDatanodeRoleConfigGroup.getConfig().getItems()) {
                if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                    hdfsDatanodeDir = apiConfig.getValue();
                }
            }

            // Journalnode
            ApiRoleConfigGroup hdfsJournalnodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup(clusterName, HdfsRoleConfigGroupName.JOURNALNODE, "hdfs");
            for (ApiConfig apiConfig: hdfsNamenodeRoleConfigGroup.getConfig().getItems()) {
                if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                    hdfsJournalnodeDir = apiConfig.getValue();
                }
            }

            // HDFS General Charts
            HdfsCapacityDto hdfsCapacityDto = timeSeriesService.getGeneralHdfsCapacity();

        }

        // Get Capacity of Directories for HDFS
        List<DirCapacityDto> dirCapacityDtos = new ArrayList<>();
        ApiHostList apiHostList = hostsResourceApi.readHosts(null, null, CmApiView.FULL);
        for (ApiHost apiHost: apiHostList.getItems()) {
            for (ApiRoleRef apiRoleRef: apiHost.getRoleRefs()) {

                // HDFS DataNode
                if (apiRoleRef.getRoleName().contains(HdfsRoleConfigGroupName.DATANODE_0)) {
                    DirCapacityDto hdfsDataNodeDirCapacity = timeSeriesService.getDataNodeHdfsCapacity(hdfsDatanodeDir, apiHost);
                    dirCapacityDtos.add(hdfsDataNodeDirCapacity);
                }

                // HDFS JournalNode
                if (apiRoleRef.getRoleName().contains(HdfsRoleConfigGroupName.JOURNALNODE_0)) {
                    DirCapacityDto hdfsJournalNodeDirCapacity = timeSeriesService.getDataNodeHdfsCapacity(hdfsDatanodeDir, apiHost);
                    dirCapacityDtos.add(hdfsJournalNodeDirCapacity);
                }

                // HDFS NameNode
                if (apiRoleRef.getRoleName().contains(HdfsRoleConfigGroupName.NAMENODE_0)) {
                    DirCapacityDto hdfsNameNodeDirCapacity = timeSeriesService.getDataNodeHdfsCapacity(hdfsNamenodeDir, apiHost);
                    dirCapacityDtos.add(hdfsNameNodeDirCapacity);
                }
            }

            // apiHost.getHostId();
            // apiHost.getNumCores();
            // apiHost.getNumPhysicalCores();
            // apiHost.getHostname();
        }

        // Shell Command Utilities
        String hdfsSafeModeCheck = shellCommandExecutorService.executeCommand("hdfs dfsadmin -safemode get");
        Path hdfsSafeModeOutputPath = Paths.get(healthcheckReportConfig.getOutputDir() + "/hdfs-safe-mode.txt");
        Files.write(hdfsSafeModeOutputPath, hdfsSafeModeCheck.getBytes(StandardCharsets.UTF_8));

        String hdfsFsck = shellCommandExecutorService.executeCommand("hdfs fsck /");
        Path fsckOutputPath = Paths.get(healthcheckReportConfig.getOutputDir() + "/hdfs-fsck.txt");
        Files.write(fsckOutputPath, hdfsFsck.getBytes(StandardCharsets.UTF_8));

        //String kerberosLogin = shellCommandExecutorService.executeCommand("kinit <user>");
        //Path kerberosLoginOutputPath = Paths.get(healthcheckReportConfig.getOutputDir() + "/kinit.txt");
        //Files.write(kerberosLoginOutputPath, kerberosLogin.getBytes(StandardCharsets.UTF_8));

        // Inspect Hosts API For Performance/Networking Check
        ApiCommand inspectHostCmd = clustersResourceApi.inspectHostsCommand(clusterName);
        ApiCommand inspectHostCmdResult = inspectPerformanceService.runInspectorCmd(inspectHostCmd);

        // Inspect Network Performance API for Performance/Networking Check
        ApiClusterPerfInspectorArgs perfInspectorArgs = new ApiClusterPerfInspectorArgs();
        ApiPerfInspectorPingArgs apiPerfInspectorPingArgs = new ApiPerfInspectorPingArgs();
        apiPerfInspectorPingArgs.setPingTimeoutSecs(BigDecimal.valueOf(10));
        apiPerfInspectorPingArgs.setPingCount(BigDecimal.valueOf(10));
        apiPerfInspectorPingArgs.setPingPacketSizeBytes(BigDecimal.valueOf(56));
        perfInspectorArgs.setPingArgs(apiPerfInspectorPingArgs);
        ApiCommand perfInspectorCmd = clustersResourceApi.perfInspectorCommand(clusterName, perfInspectorArgs);
        ApiCommand perfInspectCmdResult = inspectPerformanceService.runInspectorCmd(perfInspectorCmd);


        return perfInspectCmdResult;


        // return dirCapacityDtos;


        // CDP General Configurations
        // ApiConfigList allHostsConfig = allHostsResourceApi.readConfig(CmApiView.FULL);
        // gson.toJson(allHostsConfig, new FileWriter(healthcheckReportConfig.getOutputDir() + "/all-hosts-config.json"));

        /*
        clouderaManagerResourceApi.getConfig(CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);

        clouderaManagerResourceApi.getDeployment2(CmApiView.EXPORT);

        clouderaManagerResourceApi.readInstances();

        clouderaManagerResourceApi.getKerberosInfo();

        clouderaManagerResourceApi.getKerberosPrincipals(false);

        clouderaManagerResourceApi.getKrb5Conf();

        clouderaManagerResourceApi.readLicense();

        clouderaManagerResourceApi.getScmDbInfo();

        clouderaManagerResourceApi.getVersion();

        // Certificate Check
        certManagerResourceApi.getTruststore("PEM");
        */

    }
}

