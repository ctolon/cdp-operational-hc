package com.bentego.cdputils.service;

import com.cloudera.api.swagger.client.ApiClient;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.client.ApiResponse;
import org.springframework.stereotype.Service;
import com.cloudera.api.swagger.CertManagerResourceApi;

import java.io.File;

@Service
public class CertifcateService {

    private  ApiClient cmClient;
    private CertManagerResourceApi certManagerResourceApi;

    public CertifcateService(ApiClient cmClient) {
        this.cmClient = cmClient;
        this.certManagerResourceApi = new CertManagerResourceApi(cmClient);

    }

    public ApiResponse<File> checkCertificateExpireTime() throws ApiException {
        return certManagerResourceApi.getTruststoreWithHttpInfo("JKS");// PEM

    }

}
