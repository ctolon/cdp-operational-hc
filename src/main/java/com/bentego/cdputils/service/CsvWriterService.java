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

@Service
public class CsvWriterService {

    Logger logger = LoggerFactory.getLogger(CsvWriterService.class);


    public CsvWriterService() {

    }

    public void writeRoleBadHealtcheckCsvToDisk(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {
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

            for (RoleBadHealthcheckDto data : clusterWideBadHealthcheckDto.getRoleBadHealthcheckDtos()) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("role bad healtcheck csv successfully created on path: {}", filePath);
    }

    public void writeServiceBadHealtcheckCsvToDisk(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {
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

            for (ServiceBadHealthcheckDto data : clusterWideBadHealthcheckDto.getServiceBadHealthcheckDtos()) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("service bad healtcheck csv successfully created on path: {}", filePath);
    }

    public void writeHostBadHealtcheckCsvToDisk(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {
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

            for (HostBadHealthcheckDto data : clusterWideBadHealthcheckDto.getHostBadHealthcheckDtos()) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("host bad healtcheck csv successfully created on path: {}", filePath);
    }
}
