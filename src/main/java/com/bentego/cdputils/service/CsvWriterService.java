package com.bentego.cdputils.service;

import com.bentego.cdputils.dtos.healthcheck.ClusterWideBadHealthcheckDto;
import com.bentego.cdputils.dtos.healthcheck.HostBadHealthcheckDto;
import com.bentego.cdputils.dtos.healthcheck.RoleBadHealthcheckDto;
import com.bentego.cdputils.dtos.healthcheck.ServiceBadHealthcheckDto;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CsvWriterService {

    Logger logger = LoggerFactory.getLogger(CsvWriterService.class);


    public CsvWriterService() {

    }

    public void writeRoleBadHealtcheckCsvToDisk(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {

        List<RoleBadHealthcheckDto> roleBadHealthcheckDtoList = clusterWideBadHealthcheckDto.getRoleBadHealthcheckDtos();
        if (roleBadHealthcheckDtoList != null && roleBadHealthcheckDtoList.isEmpty()) {
            logger.info("all roles are healthy");
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            String[] header = {
                    "roleName",
                    "roleType",
                    "roleConfigGroupName",
                    "serviceName",
                    "serviceDisplayName",
                    "hostId",
                    "hostname",
                    "healthcheckName",
                    "healthcheckSummary",
                    "healthcheckExplanation",
                    "healthcheckSuppressed",
            };
            writer.writeNext(header);


            if (roleBadHealthcheckDtoList != null && !roleBadHealthcheckDtoList.isEmpty()) {
                logger.warn("bad issues found for roles, writing to csv...");
                for (RoleBadHealthcheckDto data : roleBadHealthcheckDtoList) {
                    String[] row = {
                            data.getRoleName(),
                            data.getRoleType(),
                            data.getRoleConfigGroupName(),
                            data.getServiceName(),
                            data.getServiceDisplayName(),
                            data.getHostId(),
                            data.getHostname(),
                            data.getHealthcheckDto().getHealthcheckName(),
                            data.getHealthcheckDto().getHealthcheckSummary(),
                            data.getHealthcheckDto().getHealthcheckExplanation(),
                            data.getHealthcheckDto().getHealthcheckSuppressed().toString()
                    };
                    writer.writeNext(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("role bad healtcheck csv successfully created on path: {}", filePath);
    }

    public void writeServiceBadHealtcheckCsvToDisk(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {

        List<ServiceBadHealthcheckDto> serviceBadHealthcheckDtoList = clusterWideBadHealthcheckDto.getServiceBadHealthcheckDtos();
        if (serviceBadHealthcheckDtoList != null && serviceBadHealthcheckDtoList.isEmpty()) {
            logger.info("all services are healthy");
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            String[] header = {
                    "serviceName",
                    "serviceDisplayName",
                    "serviceType",
                    "healthcheckName",
                    "healthcheckSummary",
                    "healthcheckExplanation",
                    "healthcheckSuppressed",
            };
            writer.writeNext(header);


            if (serviceBadHealthcheckDtoList != null && !serviceBadHealthcheckDtoList.isEmpty()) {
                logger.warn("bad issues found for services, writing to csv...");
                for (ServiceBadHealthcheckDto data : serviceBadHealthcheckDtoList) {
                    String[] row = {
                            data.getServiceName(),
                            data.getServiceDisplayName(),
                            data.getServiceType(),
                            data.getHealthcheckDto().getHealthcheckName(),
                            data.getHealthcheckDto().getHealthcheckSummary(),
                            data.getHealthcheckDto().getHealthcheckExplanation(),
                            data.getHealthcheckDto().getHealthcheckSuppressed().toString()
                    };
                    writer.writeNext(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("service bad healthcheck csv successfully created on path: {}", filePath);
    }

    public void writeHostBadHealtcheckCsvToDisk(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {

        List<HostBadHealthcheckDto> hostBadHealthcheckDtoList = clusterWideBadHealthcheckDto.getHostBadHealthcheckDtos();
        if (hostBadHealthcheckDtoList != null && hostBadHealthcheckDtoList.isEmpty()) {
            logger.info("all hosts are healthy");
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            String[] header = {
                    "hostId",
                    "hostname",
                    "healthcheckName",
                    "healthcheckSummary",
                    "healthcheckExplanation",
                    "healthcheckSuppressed",
            };
            writer.writeNext(header);


            if (hostBadHealthcheckDtoList != null && !hostBadHealthcheckDtoList.isEmpty()) {
                logger.warn("bad issues found for hosts, writing to csv...");
                for (HostBadHealthcheckDto data : hostBadHealthcheckDtoList) {
                    String[] row = {
                            data.getHostId(),
                            data.getHostname(),
                            data.getHealthcheckDto().getHealthcheckName(),
                            data.getHealthcheckDto().getHealthcheckSummary(),
                            data.getHealthcheckDto().getHealthcheckExplanation(),
                            data.getHealthcheckDto().getHealthcheckSuppressed().toString()
                    };
                    writer.writeNext(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("host bad healthcheck csv successfully created on path: {}", filePath);
    }
}
