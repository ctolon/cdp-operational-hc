package com.bentego.cdputils.service;

import com.cloudera.api.swagger.ClouderaManagerResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterGeneraInfoService {

    Logger logger = LoggerFactory.getLogger(ClusterGeneraInfoService.class);

    private final ClouderaManagerResourceApi clouderaManagerResourceApi;

    public ClusterGeneraInfoService(ClouderaManagerResourceApi clouderaManagerResourceApi) {
        this.clouderaManagerResourceApi = clouderaManagerResourceApi;
    }

    public void retrieveClusterGeneralInfo() throws ApiException {
        // Cluster General
        // CDP General Configurations
        // ApiConfigList allHostsConfig = allHostsResourceApi.readConfig(CmApiView.FULL);
        // gson.toJson(allHostsConfig, new FileWriter(healthcheckReportConfig.getOutputDir() + "/all-hosts-config.json"));


        //clouderaManagerResourceApi.getConfig(CmApiView.FULL_WITH_HEALTH_CHECK_EXPLANATION);

        //clouderaManagerResourceApi.getDeployment2(CmApiView.EXPORT);

        ApiCmServerList apiCmServerList = clouderaManagerResourceApi.readInstances();
        logger.info("{}", apiCmServerList.toString());


        ApiKerberosInfo apiKerberosInfo = clouderaManagerResourceApi.getKerberosInfo();
        logger.info("{}", apiKerberosInfo.toString());

        ApiPrincipalList apiPrincipalList = clouderaManagerResourceApi.getKerberosPrincipals(false);
        logger.info("{}", apiPrincipalList.toString());

        //clouderaManagerResourceApi.getKrb5Conf();
        //logger.info("{}", );

        ApiLicense apiLicense = clouderaManagerResourceApi.readLicense();
        logger.info("{}", apiLicense.toString());

        ApiScmDbInfo apiScmDbInfo = clouderaManagerResourceApi.getScmDbInfo();
        logger.info("{}", apiScmDbInfo.toString());

        ApiVersionInfo apiVersionInfo = clouderaManagerResourceApi.getVersion();
        logger.info("{}", apiVersionInfo.toString());

        // Certificate Check
        // certManagerResourceApi.getTruststore("PEM");
        // logger.info("{}", );
    }

}
