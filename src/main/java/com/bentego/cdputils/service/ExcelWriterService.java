package com.bentego.cdputils.service;

import com.bentego.cdputils.dtos.healthcheck.ClusterWideBadHealthcheckDto;
import com.bentego.cdputils.dtos.healthcheck.HostBadHealthcheckDto;
import com.bentego.cdputils.dtos.healthcheck.RoleBadHealthcheckDto;
import com.bentego.cdputils.dtos.healthcheck.ServiceBadHealthcheckDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelWriterService {

    Logger logger = LoggerFactory.getLogger(ExcelWriterService.class);

    public void writeAllHealthchecksToSingleExcel(ClusterWideBadHealthcheckDto clusterWideBadHealthcheckDto, String filePath) {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath)) {

            // 1. Host Healthcheck Sheet
            List<HostBadHealthcheckDto> hostList = clusterWideBadHealthcheckDto.getHostBadHealthcheckDtos();
            writeToSheet(workbook, "Bad Host Healthchecks", hostList, new String[]{
                    "hostId", "hostname", "healthcheckName", "healthcheckSummary", "healthcheckExplanation", "healthcheckSuppressed"
            });

            // 2. Role Healthcheck Sheet
            List<RoleBadHealthcheckDto> roleList = clusterWideBadHealthcheckDto.getRoleBadHealthcheckDtos();
            writeToSheet(workbook, "Bad Role Healthchecks", roleList, new String[]{
                    "roleName", "roleType", "roleConfigGroupName", "serviceName", "serviceDisplayName",
                    "hostId", "hostname", "healthcheckName", "healthcheckSummary", "healthcheckExplanation", "healthcheckSuppressed"
            });

            // 3. Service Healthcheck Sheet
            List<ServiceBadHealthcheckDto> serviceList = clusterWideBadHealthcheckDto.getServiceBadHealthcheckDtos();
            writeToSheet(workbook, "Bad Service Healthchecks", serviceList, new String[]{
                    "serviceName", "serviceDisplayName", "serviceType", "healthcheckName", "healthcheckSummary",
                    "healthcheckExplanation", "healthcheckSuppressed"
            });

            workbook.write(fileOut);
            logger.info("All healthcheck sheets successfully created in one Excel file: {}", filePath);

        } catch (IOException e) {
            logger.error("Failed to write Excel file", e);
        }
    }

    private <T> void writeToSheet(Workbook workbook, String sheetName, List<T> dataList, String[] headers) {
        if (dataList == null || dataList.isEmpty()) {
            logger.info("{} has no bad healthchecks, skipping sheet creation.", sheetName);
            return;
        }

        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        int rowNum = 1;
        for (T data : dataList) {
            Row row = sheet.createRow(rowNum++);

            if (data instanceof HostBadHealthcheckDto) {
                HostBadHealthcheckDto host = (HostBadHealthcheckDto) data;
                row.createCell(0).setCellValue(host.getHostId());
                row.createCell(1).setCellValue(host.getHostname());
                row.createCell(2).setCellValue(host.getHealthcheckDto().getHealthcheckName());
                row.createCell(3).setCellValue(host.getHealthcheckDto().getHealthcheckSummary());
                row.createCell(4).setCellValue(host.getHealthcheckDto().getHealthcheckExplanation());
                row.createCell(5).setCellValue(host.getHealthcheckDto().getHealthcheckSuppressed().toString());
            } else if (data instanceof RoleBadHealthcheckDto) {
                RoleBadHealthcheckDto role = (RoleBadHealthcheckDto) data;
                row.createCell(0).setCellValue(role.getRoleName());
                row.createCell(1).setCellValue(role.getRoleType());
                row.createCell(2).setCellValue(role.getRoleConfigGroupName());
                row.createCell(3).setCellValue(role.getServiceName());
                row.createCell(4).setCellValue(role.getServiceDisplayName());
                row.createCell(5).setCellValue(role.getHostId());
                row.createCell(6).setCellValue(role.getHostname());
                row.createCell(7).setCellValue(role.getHealthcheckDto().getHealthcheckName());
                row.createCell(8).setCellValue(role.getHealthcheckDto().getHealthcheckSummary());
                row.createCell(9).setCellValue(role.getHealthcheckDto().getHealthcheckExplanation());
                row.createCell(10).setCellValue(role.getHealthcheckDto().getHealthcheckSuppressed().toString());
            } else if (data instanceof ServiceBadHealthcheckDto) {
                ServiceBadHealthcheckDto service = (ServiceBadHealthcheckDto) data;
                row.createCell(0).setCellValue(service.getServiceName());
                row.createCell(1).setCellValue(service.getServiceDisplayName());
                row.createCell(2).setCellValue(service.getServiceType());
                row.createCell(3).setCellValue(service.getHealthcheckDto().getHealthcheckName());
                row.createCell(4).setCellValue(service.getHealthcheckDto().getHealthcheckSummary());
                row.createCell(5).setCellValue(service.getHealthcheckDto().getHealthcheckExplanation());
                row.createCell(6).setCellValue(service.getHealthcheckDto().getHealthcheckSuppressed().toString());
            }
        }
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }
}
