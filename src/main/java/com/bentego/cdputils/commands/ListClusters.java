/*
package com.bentego.cdputils.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cloudera.api.swagger.ClustersResourceApi;
import com.cloudera.api.swagger.ClouderaManagerResourceApi;
import com.cloudera.api.swagger.client.ApiClient;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.client.Configuration;
import com.cloudera.api.swagger.model.ApiCluster;
import com.cloudera.api.swagger.model.ApiClusterList;

public class ListClusters {

    public static void main(String[] args) throws IOException {
        ApiClient cmClient = Configuration.getDefaultApiClient();

        // Configure HTTP basic authorization: basic
        cmClient.setBasePath("https://cm-host:7183/api/v40");
        cmClient.setUsername("username");
        cmClient.setPassword("password");

        // Configure TLS for secure communication
        cmClient.setVerifyingSsl(true);

        Path truststorePath = Paths.get("/path/to/ca_cert_file.pem");
        byte[] truststoreBytes = Files.readAllBytes(truststorePath);
        cmClient.setSslCaCert(new ByteArrayInputStream(truststoreBytes));


        ClouderaManagerResourceApi api = new ClouderaManagerResourceApi(cmClient);

        api.hostInstallCommand()

        ClustersResourceApi apiInstance = new ClustersResourceApi(cmClient);
        try {
            ApiClusterList clusterList = apiInstance.readClusters("ANY", "SUMMARY");
            for (ApiCluster cluster : clusterList.getItems()) {
                System.out.printf("Name: %s, Version: %s", cluster.getDisplayName(),
                        cluster.getFullVersion());
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling ClustersResourceApi#readClusters");
            e.printStackTrace();
        }
    }
}
 */