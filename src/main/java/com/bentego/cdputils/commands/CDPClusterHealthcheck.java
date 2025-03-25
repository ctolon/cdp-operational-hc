package com.bentego.cdputils.commands;
import com.bentego.cdputils.builder.TimeseriesQueryBuilder;
import com.bentego.cdputils.configuration.HealthcheckReportConfig;
import com.bentego.cdputils.contants.api.CmApiView;
import com.bentego.cdputils.contants.roleconfig.HdfsRoleConfigGroupName;
import com.bentego.cdputils.dtos.*;
import com.bentego.cdputils.dtos.healthcheck.ClusterWideBadHealthcheckDto;
import com.bentego.cdputils.enums.DataUnit;
import com.bentego.cdputils.enums.RoleConfigUIBinding;
import com.bentego.cdputils.service.*;
import com.bentego.cdputils.utils.CustomStringUtils;
import com.cloudera.api.swagger.*;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(CDPClusterHealthcheck.class);

    private final Gson gson;

    private final HealthcheckReportConfig healthcheckReportConfig;

    private final FileManagerService fileManagerService;
    private final SSLCertificateService sslCertificateService;
    private final ShellCommandExecutorService shellCommandExecutorService;
    private final InspectPerformanceService inspectPerformanceService;
    private final TimeSeriesService timeSeriesService;
    private final ClusterGeneraInfoService clusterGeneraInfoService;
    private final DirLocatorService dirLocatorService;
    private final ClusterGeneralHealthcheckService clusterGeneralHealthcheckService;
    private final CsvWriterService csvWriterService;

    private final ServicesResourceApi servicesResourceApi;
    private final RoleConfigGroupsResourceApi roleConfigGroupsResourceApi;
    private final AllHostsResourceApi allHostsResourceApi;
    private final CertManagerResourceApi certManagerResourceApi;
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
            ClusterGeneraInfoService clusterGeneraInfoService,
            DirLocatorService dirLocatorService,
            ClusterGeneralHealthcheckService clusterGeneralHealthcheckService,
            CsvWriterService csvWriterService,
            ServicesResourceApi servicesResourceApi,
            RoleConfigGroupsResourceApi roleConfigGroupsResourceApi,
            AllHostsResourceApi allHostsResourceApi,
            CertManagerResourceApi certManagerResourceApi,
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
        this.clusterGeneraInfoService = clusterGeneraInfoService;
        this.dirLocatorService = dirLocatorService;
        this.clusterGeneralHealthcheckService = clusterGeneralHealthcheckService;
        this.csvWriterService = csvWriterService;
        this.servicesResourceApi = servicesResourceApi;
        this.roleConfigGroupsResourceApi = roleConfigGroupsResourceApi;
        this.allHostsResourceApi = allHostsResourceApi;
        this.certManagerResourceApi = certManagerResourceApi;
        this.clustersResourceApi = clustersResourceApi;
        this.hostsResourceApi = hostsResourceApi;
        this.rolesResourceApi = rolesResourceApi;
    }

    // defaultValue = ShellOption.NULL
    @ShellMethod(key = "healthcheck", value = "CDP Healthcheck Command")
    public ApiCommand cdpOperationalHealthcheck (
            @ShellOption(value = { "--cluster-name", "-c" }) String clusterName,
            @ShellOption(defaultValue = ShellOption.NULL, value = { "--kinit-user", "-k"}) String kinitUser)
            throws ApiException, IOException {

        logger.info("Starting healthcheck for CDP Cluster: {}", clusterName);

        logger.info("General informations for Cluster:");
        clusterGeneraInfoService.retrieveClusterGeneralInfo();

        // Create Healthcheck report dir
        logger.info("creating logging/reporting root directory as {} with permission 755...", healthcheckReportConfig.getOutputDir());
        fileManagerService.mkdirIfNotExistsAs755(healthcheckReportConfig.getOutputDir());

        // Get Host List
        ApiHostList apiHostList = hostsResourceApi.readHosts(null, null, CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);


        // Get ServiceList
        List<String> serviceTypes = new ArrayList<>();
        List<String> servicesInMaintenanceMode = new ArrayList<>();
        logger.info("getting service types in cluster...");
        ApiServiceList serviceList = servicesResourceApi.readServices(clusterName, CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);
        for (ApiService apiService: serviceList.getItems()) {
            serviceTypes.add(apiService.getName());
            logger.info("service type found: {}", apiService.getName());
            if (apiService.getMaintenanceMode()) {
                servicesInMaintenanceMode.add(apiService.getName());
                logger.warn("service found in maintenance mode: {}", apiService.getName());
            }
        }
        logger.info("total service in cluster: {}", serviceTypes.size());

        if (servicesInMaintenanceMode.isEmpty()) {
            logger.info("no service found in maintenance mode.");
        }

        // Get RoleList
        logger.info("get roles in cluster...");
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

        // Get Bad healtchecks
        logger.info("get cluster-wide healtcheck reports as csv...");
        ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto = clusterGeneralHealthcheckService.getClusterWideHealthcheck(clusterName, apiHostList, serviceList, serviceTypes);

        csvWriterService.writeHostBadHealtcheckCsvToDisk(clusterWideBadHealthcheckDto, healthcheckReportConfig.getOutputDir() + "/host_bad_healtcheck.csv");
        csvWriterService.writeServiceBadHealtcheckCsvToDisk(clusterWideBadHealthcheckDto, healthcheckReportConfig.getOutputDir() + "/service_bad_healtcheck.csv");
        csvWriterService.writeRoleBadHealtcheckCsvToDisk(clusterWideBadHealthcheckDto, healthcheckReportConfig.getOutputDir() + "/role_bad_healtcheck.csv");


        // Check CDP UI Certificates
        logger.info("checking SSL Certificates for services w/web interface...");
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
        List<DirCapacityDto> dirCapacityDtos = new ArrayList<>();
        HdfsDirLocationDto hdfsDirLocationDto = new HdfsDirLocationDto();
        if (serviceTypes.contains("hdfs")) {

            logger.info("Cluster has hdfs service. Checking Directory Path for Namenode, Journalnode and Namenode in configuration...");

            dirLocatorService.setHdfsDirLocations(hdfsDirLocationDto ,clusterName);

            // HDFS General Charts
            HdfsCapacityDto hdfsCapacityDto = timeSeriesService.getGeneralHdfsCapacity();

            // Get Capacity of Directories for HDFS
            for (ApiHost apiHost: apiHostList.getItems()) {
                for (ApiRoleRef apiRoleRef: apiHost.getRoleRefs()) {

                    // HDFS DataNode
                    if (apiRoleRef.getRoleName().contains(HdfsRoleConfigGroupName.DATANODE_0)) {
                        DirCapacityDto hdfsDataNodeDirCapacity = timeSeriesService.getDataNodeHdfsCapacity(hdfsDirLocationDto.getDataNodeDir(), apiHost);
                        hdfsDataNodeDirCapacity.setPercentagesAndFree();
                        // no need set warning threshold for datanode
                        dirCapacityDtos.add(hdfsDataNodeDirCapacity);
                    }

                    // HDFS JournalNode
                    if (apiRoleRef.getRoleName().contains(HdfsRoleConfigGroupName.JOURNALNODE_0)) {
                        DirCapacityDto hdfsJournalNodeDirCapacity = timeSeriesService.getJournalNodeHdfsCapacity(hdfsDirLocationDto.getJournalNodeDir(), apiHost);
                        hdfsJournalNodeDirCapacity.setPercentagesAndFree();
                        hdfsJournalNodeDirCapacity.setWarningThresholdInGb(BigDecimal.valueOf(200)); // 200 GB Free space warning threshold
                        if (hdfsJournalNodeDirCapacity.getCapacityFree().compareTo(hdfsJournalNodeDirCapacity.getWarningThresholdInGb()) <= 0) {
                            logger.warn("hdfs journalnode on host {} have not enought free space. current used space: {}gb, free space: {}gb warning threshold: {}gb", apiHost.getHostname(), hdfsJournalNodeDirCapacity.getCapacityUsed(), hdfsJournalNodeDirCapacity.getCapacityFree(), hdfsJournalNodeDirCapacity.getWarningThresholdInGb());
                        } else {
                            logger.info("hdfs journalnode have enough free space.");
                        }
                        dirCapacityDtos.add(hdfsJournalNodeDirCapacity);
                    }

                    // HDFS NameNode
                    if (apiRoleRef.getRoleName().contains(HdfsRoleConfigGroupName.NAMENODE_0)) {
                        DirCapacityDto hdfsNameNodeDirCapacity = timeSeriesService.getNameNodeHdfsCapacity(hdfsDirLocationDto.getNameNodeDir(), apiHost);
                        hdfsNameNodeDirCapacity.setPercentagesAndFree();
                        hdfsNameNodeDirCapacity.setWarningThresholdInGb(BigDecimal.valueOf(200)); // 200 GB Free space warning threshold
                        if (hdfsNameNodeDirCapacity.getCapacityFree().compareTo(hdfsNameNodeDirCapacity.getWarningThresholdInGb()) <= 0) {
                            logger.warn("hdfs namenode on host {} have not enought free space. current used space: {}gb, free space: {}gb warning threshold: {}gb", apiHost.getHostname(), hdfsNameNodeDirCapacity.getCapacityUsed(), hdfsNameNodeDirCapacity.getCapacityFree(), hdfsNameNodeDirCapacity.getWarningThresholdInGb());
                        } else {
                            logger.info("hdfs namenode have enough free space.");
                        }
                        dirCapacityDtos.add(hdfsNameNodeDirCapacity);
                    }
                }
            }

            // apiHost.getHostId();
            // apiHost.getNumCores();
            // apiHost.getNumPhysicalCores();
            // apiHost.getHostname();
        }

        // Shell Command Utilities
        logger.info("Checking hdfs safe mode...");
        String hdfsSafeModeCheck = shellCommandExecutorService.executeCommand("hdfs dfsadmin -safemode get");
        Path hdfsSafeModeOutputPath = Paths.get(healthcheckReportConfig.getOutputDir() + "/hdfs-safe-mode.txt");
        Files.write(hdfsSafeModeOutputPath, hdfsSafeModeCheck.getBytes(StandardCharsets.UTF_8));

        String hdfsFsck = shellCommandExecutorService.executeCommand("hdfs fsck /");
        Path fsckOutputPath = Paths.get(healthcheckReportConfig.getOutputDir() + "/hdfs-fsck.txt");
        Files.write(fsckOutputPath, hdfsFsck.getBytes(StandardCharsets.UTF_8));

        if (kinitUser != null) {
            logger.info("login with kinit with user: {}...", kinitUser);
            String kerberosLogin = shellCommandExecutorService.executeCommand("kinit" + " " + kinitUser);
            Path kerberosLoginOutputPath = Paths.get(healthcheckReportConfig.getOutputDir() + "/kinit.txt");
            Files.write(kerberosLoginOutputPath, kerberosLogin.getBytes(StandardCharsets.UTF_8));
        }

        // Inspect Hosts API For Performance/Networking Check
        logger.info("running inspect host command for performance/networking check...");
        ApiCommand inspectHostCmd = clustersResourceApi.inspectHostsCommand(clusterName);
        ApiCommand inspectHostCmdResult = inspectPerformanceService.runInspectorCmd(inspectHostCmd);

        logger.info("running performance inspector command for performance/networking check...");
        // Inspect Network Performance API for Performance/Networking Check
        ApiClusterPerfInspectorArgs perfInspectorArgs = inspectPerformanceService.buildDefaultApiClusterPerfInspectorArgs();
        ApiCommand perfInspectorCmd = clustersResourceApi.perfInspectorCommand(clusterName, perfInspectorArgs);
        ApiCommand perfInspectCmdResult = inspectPerformanceService.runInspectorCmd(perfInspectorCmd);


        return perfInspectCmdResult;

    }
}
