package com.bentego.cdputils.commands;
import com.bentego.cdputils.service.CommonHealthcheckService;
import com.cloudera.api.swagger.ServicesResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.ApiCommand;
import com.cloudera.api.swagger.model.ApiServiceList;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class CDPClusterHealthcheck {


    private final CommonHealthcheckService commonHealthcheckService;
    private final ServicesResourceApi servicesResourceApi;

    public CDPClusterHealthcheck(
            CommonHealthcheckService commonHealthcheckService,
            ServicesResourceApi servicesResourceApi

    ) {
        this.commonHealthcheckService = commonHealthcheckService;
        this.servicesResourceApi = servicesResourceApi;
    }


    @ShellMethod(key = "healthcheck", value = "Say hello")
    public ApiServiceList hello () throws ApiException {
        ApiServiceList serviceList = servicesResourceApi.readServices("test", "FULL_WITH_HEALTH_CHECK_EXPLANATION");
        return serviceList;


        //return commonHealthcheckService.inspectHosts();
    }
}

