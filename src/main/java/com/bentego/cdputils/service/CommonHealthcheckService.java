package com.bentego.cdputils.service;

import com.cloudera.api.swagger.ClouderaManagerResourceApi;
import com.cloudera.api.swagger.ClustersResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.ApiCommand;

public class CommonHealthcheckService {

    private final ClouderaManagerResourceApi clouderaManagerResourceApi;
    private final ClustersResourceApi clustersResourceApi;

    public CommonHealthcheckService(ClouderaManagerResourceApi clouderaManagerResourceApi, ClustersResourceApi clustersResourceApi) {
        this.clouderaManagerResourceApi = clouderaManagerResourceApi;
        this.clustersResourceApi = clustersResourceApi;
    }

    public ApiCommand inspectHosts() throws ApiException {
        return clouderaManagerResourceApi.inspectHostsCommand();
    }
}
