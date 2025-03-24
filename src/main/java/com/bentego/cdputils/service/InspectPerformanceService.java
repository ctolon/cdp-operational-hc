package com.bentego.cdputils.service;

import com.cloudera.api.swagger.CommandsResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.ApiClusterPerfInspectorArgs;
import com.cloudera.api.swagger.model.ApiCommand;
import com.cloudera.api.swagger.model.ApiPerfInspectorPingArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InspectPerformanceService {

    private final CommandsResourceApi commandsResourceApi;
    Logger logger = LoggerFactory.getLogger(InspectPerformanceService.class);

    public InspectPerformanceService(CommandsResourceApi commandsResourceApi) {
        this.commandsResourceApi = commandsResourceApi;
    }

    public ApiCommand runInspectorCmd(ApiCommand inspectorCmd) throws ApiException {
        BigDecimal inspectorCmdId = inspectorCmd.getId();
        ApiCommand inspectorCmdResultDto = null;
        int inspectorLoopCounter = 0;
        while (true) {
            if (inspectorLoopCounter == 60) {
                break;
            }
            ApiCommand inspectorCmdResult = commandsResourceApi.readCommand(inspectorCmdId);
            if (inspectorCmdResult.getEndTime() == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("error when sleep on thread for inspect command!");
                }
                inspectorLoopCounter++;
            } else {
                inspectorCmdResultDto = inspectorCmdResult;
                break;
            }
        }
        return inspectorCmdResultDto;
    }

    public ApiClusterPerfInspectorArgs buildDefaultApiClusterPerfInspectorArgs() {
        ApiClusterPerfInspectorArgs perfInspectorArgs = new ApiClusterPerfInspectorArgs();
        ApiPerfInspectorPingArgs apiPerfInspectorPingArgs = new ApiPerfInspectorPingArgs();
        apiPerfInspectorPingArgs.setPingTimeoutSecs(BigDecimal.valueOf(10));
        apiPerfInspectorPingArgs.setPingCount(BigDecimal.valueOf(10));
        apiPerfInspectorPingArgs.setPingPacketSizeBytes(BigDecimal.valueOf(56));
        perfInspectorArgs.setPingArgs(apiPerfInspectorPingArgs);
        return perfInspectorArgs;
    }
}
