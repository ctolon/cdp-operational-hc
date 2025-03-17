package com.bentego.cdputils.commands;
import com.bentego.cdputils.configuration.HealthcheckReportConfig;
import com.bentego.cdputils.contants.CmApiView;
import com.bentego.cdputils.contants.HdfsRoleConfigGroupName;
import com.bentego.cdputils.service.CommonHealthcheckService;
import com.cloudera.api.swagger.*;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.*;
import com.google.gson.GsonBuilder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class CDPClusterHealthcheck {

    private final HealthcheckReportConfig healthcheckReportConfig;

    private final CommonHealthcheckService commonHealthcheckService;
    private final ServicesResourceApi servicesResourceApi;
    private final RoleConfigGroupsResourceApi roleConfigGroupsResourceApi;
    private final AllHostsResourceApi allHostsResourceApi;
    private final CertManagerResourceApi certManagerResourceApi;
    private final ClouderaManagerResourceApi clouderaManagerResourceApi;

    public CDPClusterHealthcheck(
            HealthcheckReportConfig healthcheckReportConfig,
            CommonHealthcheckService commonHealthcheckService,
            ServicesResourceApi servicesResourceApi,
            RoleConfigGroupsResourceApi roleConfigGroupsResourceApi,
            AllHostsResourceApi allHostsResourceApi,
            CertManagerResourceApi certManagerResourceApi,
            ClouderaManagerResourceApi clouderaManagerResourceApi

    ) {
        this.healthcheckReportConfig = healthcheckReportConfig;
        this.commonHealthcheckService = commonHealthcheckService;
        this.servicesResourceApi = servicesResourceApi;
        this.roleConfigGroupsResourceApi = roleConfigGroupsResourceApi;
        this.allHostsResourceApi = allHostsResourceApi;
        this.certManagerResourceApi = certManagerResourceApi;
        this.clouderaManagerResourceApi = clouderaManagerResourceApi;
    }


    @ShellMethod(key = "healthcheck", value = "Say hello")
    public void cdpOperationalHealthcheck () throws ApiException, IOException {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // CDP General Configurations
        ApiConfigList allHostsConfig = allHostsResourceApi.readConfig(CmApiView.FULL);
        gson.toJson(allHostsConfig, new FileWriter(healthcheckReportConfig.getOutputDir() + "/all-hosts-config.json"));

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

        // ServiceList
        List<String> serviceTypes = new ArrayList<>();
        List<String> servicesInMaintenanceMode = new ArrayList<>();
        ApiServiceList serviceList = servicesResourceApi.readServices("test", CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);
        for (ApiService apiService: serviceList.getItems()) {
            serviceTypes.add(apiService.getName());
            if (apiService.getMaintenanceMode()) {
                servicesInMaintenanceMode.add(apiService.getName());
            }
        }

        // HDFS Role Config list
        if (serviceTypes.contains("hdfs")) {

            // Namenode
            ApiRoleConfigGroup hdfsNamenodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup("test", HdfsRoleConfigGroupName.NAMENODE, "hdfs");
            for (ApiConfig apiConfig: hdfsNamenodeRoleConfigGroup.getConfig().getItems()) {
                if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                    String hdfsNamenodeDir = apiConfig.getValue();
                }
            }

            // Datanode
            ApiRoleConfigGroup hdfsDatanodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup("test", HdfsRoleConfigGroupName.DATANODE, "hdfs");
            for (ApiConfig apiConfig: hdfsDatanodeRoleConfigGroup.getConfig().getItems()) {
                if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                    String hdfsDatanodeDir = apiConfig.getValue();
                }
            }

            // Journalnode
            ApiRoleConfigGroup hdfsJournalnodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup("test", HdfsRoleConfigGroupName.JOURNALNODE, "hdfs");
            for (ApiConfig apiConfig: hdfsNamenodeRoleConfigGroup.getConfig().getItems()) {
                if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                    String hdfsJournalnodeDir = apiConfig.getValue();
                }
            }
        }

    }
}

