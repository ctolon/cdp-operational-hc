package com.bentego.cdputils.service;

import com.bentego.cdputils.builder.TimeseriesQueryBuilder;
import com.bentego.cdputils.contants.roleconfig.HdfsRoleConfigGroupName;
import com.bentego.cdputils.dtos.DirCapacityDto;
import com.bentego.cdputils.dtos.HdfsCapacityDto;
import com.bentego.cdputils.enums.DataUnit;
import com.cloudera.api.swagger.TimeSeriesResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class TimeSeriesService {

    private final TimeSeriesResourceApi timeSeriesResourceApi;

    public TimeSeriesService(TimeSeriesResourceApi timeSeriesResourceApi) {
        this.timeSeriesResourceApi = timeSeriesResourceApi;
    }

    private ApiTimeSeriesResponseList easyQueryTimeSeries(String query) throws ApiException {
        return timeSeriesResourceApi.queryTimeSeries("application/json", "RAW", null, false, query, "now");
    }

    public HdfsCapacityDto getGeneralHdfsCapacity() throws ApiException {

        HdfsCapacityDto hdfsCapacityDto = new HdfsCapacityDto();
        String hdfsGeneralCapacityQuery = TimeseriesQueryBuilder.buildHdfsCapacityCheckQuery();

        ApiTimeSeriesResponseList hdfsGeneralCapacityTsResponse = easyQueryTimeSeries(hdfsGeneralCapacityQuery);

        Map<String, Consumer<BigDecimal>> metricMap = new HashMap<>();
        metricMap.put("dfs_capacity", hdfsCapacityDto::setDfsCapacity);
        metricMap.put("dfs_capacity_used", hdfsCapacityDto::setDfsCapacityUsed);
        metricMap.put("dfs_capacity_used_non_hdfs", hdfsCapacityDto::setDfsCapacityUsedNonHdfs);

        for (ApiTimeSeriesResponse apiTimeSeriesResponse : hdfsGeneralCapacityTsResponse.getItems()) {
            for (ApiTimeSeries apiTimeSeries : apiTimeSeriesResponse.getTimeSeries()) {
                String metricName = apiTimeSeries.getMetadata().getMetricName();

                if (metricMap.containsKey(metricName)) {
                    List<ApiTimeSeriesData> dataList = apiTimeSeries.getData();
                    if (!dataList.isEmpty()) {
                        BigDecimal latestValue = dataList.get(dataList.size() - 1).getValue();
                        BigDecimal latestValueAsGb = DataUnitConverterService.convertFromBigDecimal(latestValue, DataUnit.BYTE, DataUnit.GIGABYTE);
                        metricMap.get(metricName).accept(latestValueAsGb);
                    }
                }
            }
        }
        return hdfsCapacityDto;
    }

    public DirCapacityDto getDataNodeHdfsCapacity(String hdfsDatanodeDir, ApiHost apiHost) throws ApiException {

        DirCapacityDto hdfsDataNodeDirCapacity = new DirCapacityDto();
        hdfsDataNodeDirCapacity.setDirLocation(hdfsDatanodeDir);
        hdfsDataNodeDirCapacity.setRoleType(HdfsRoleConfigGroupName.DATANODE_0);
        hdfsDataNodeDirCapacity.setHostId(apiHost.getHostId());
        hdfsDataNodeDirCapacity.setHostname(apiHost.getHostname());

        String dataNodeTsQuery = TimeseriesQueryBuilder.buildDirectoryCapacityCheckQuery(
                apiHost.getHostId(),
                hdfsDatanodeDir,
                "DIRECTORY"
        );

        ApiTimeSeriesResponseList dataNodeTsResponse = easyQueryTimeSeries(dataNodeTsQuery);


        for (ApiTimeSeriesResponse apiTimeSeriesResponse: dataNodeTsResponse.getItems()) {
            for (ApiTimeSeries apiTimeSeries: apiTimeSeriesResponse.getTimeSeries()) {

                if (apiTimeSeries.getMetadata().getMetricName().equals("capacity_used")) {
                    BigDecimal dataNodeDirCapacityUsed = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal dataNodeDirCapacityUsedAsGb = DataUnitConverterService.convertFromBigDecimal(dataNodeDirCapacityUsed, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsDataNodeDirCapacity.setCapacityUsed(dataNodeDirCapacityUsedAsGb);
                }

                if (apiTimeSeries.getMetadata().getMetricName().equals("capacity")) {
                    BigDecimal dataNodeDirCapacity = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal dataNodeDirCapacityAsGb = DataUnitConverterService.convertFromBigDecimal(dataNodeDirCapacity, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsDataNodeDirCapacity.setCapacity(dataNodeDirCapacityAsGb);
                }
            }
        }
        // dirCapacityDtos.add(hdfsDataNodeDirCapacity);
        return hdfsDataNodeDirCapacity;
    }

    public DirCapacityDto getJournalNodeHdfsCapacity(String hdfsJournalnodeDir, ApiHost apiHost) throws ApiException {
        DirCapacityDto hdfsJournalNodeDirCapacity = new DirCapacityDto();
        hdfsJournalNodeDirCapacity.setDirLocation(hdfsJournalnodeDir);
        hdfsJournalNodeDirCapacity.setRoleType(HdfsRoleConfigGroupName.JOURNALNODE_0);
        hdfsJournalNodeDirCapacity.setHostId(apiHost.getHostId());
        hdfsJournalNodeDirCapacity.setHostname(apiHost.getHostname());

        String journalNodeTsQuery = TimeseriesQueryBuilder.buildDirectoryCapacityCheckQuery(
                apiHost.getHostId(),
                hdfsJournalnodeDir,
                "DIRECTORY"
        );

        ApiTimeSeriesResponseList journalNodeTsResponse = easyQueryTimeSeries(journalNodeTsQuery);


        for (ApiTimeSeriesResponse apiTimeSeriesResponse: journalNodeTsResponse.getItems()) {
            for (ApiTimeSeries apiTimeSeries: apiTimeSeriesResponse.getTimeSeries()) {

                if (apiTimeSeries.getMetadata().getMetricName().equals("capacity_used")) {
                    BigDecimal journalNodeDirCapacityUsed = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal journalNodeDirCapacityUsedAsGb = DataUnitConverterService.convertFromBigDecimal(journalNodeDirCapacityUsed, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsJournalNodeDirCapacity.setCapacityUsed(journalNodeDirCapacityUsedAsGb);
                }

                if (apiTimeSeries.getMetadata().getMetricName().equals("capacity")) {
                    BigDecimal journalNodeDirCapacity = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal journalNodeDirCapacityAsGb = DataUnitConverterService.convertFromBigDecimal(journalNodeDirCapacity, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsJournalNodeDirCapacity.setCapacity(journalNodeDirCapacityAsGb);
                }
            }
        }
        // dirCapacityDtos.add(hdfsJournalNodeDirCapacity);
        return hdfsJournalNodeDirCapacity;
    }

    public DirCapacityDto getNameNodeHdfsCapacity(String hdfsNamenodeDir, ApiHost apiHost) throws ApiException {
        DirCapacityDto hdfsNameNodeDirCapacity = new DirCapacityDto();
        hdfsNameNodeDirCapacity.setDirLocation(hdfsNamenodeDir);
        hdfsNameNodeDirCapacity.setRoleType(HdfsRoleConfigGroupName.NAMENODE_0);
        hdfsNameNodeDirCapacity.setHostId(apiHost.getHostId());
        hdfsNameNodeDirCapacity.setHostname(apiHost.getHostname());

        String nameNodeTsQuery = TimeseriesQueryBuilder.buildDirectoryCapacityCheckQuery(
                apiHost.getHostId(),
                hdfsNamenodeDir,
                "DIRECTORY"
        );

        ApiTimeSeriesResponseList nameNodeTsResponse = easyQueryTimeSeries(nameNodeTsQuery);


        for (ApiTimeSeriesResponse apiTimeSeriesResponse: nameNodeTsResponse.getItems()) {
            for (ApiTimeSeries apiTimeSeries: apiTimeSeriesResponse.getTimeSeries()) {

                if (apiTimeSeries.getMetadata().getMetricName().equals("capacity_used")) {
                    BigDecimal nameNodeDirCapacityUsed = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal nameNodeDirCapacityUsedAsGb = DataUnitConverterService.convertFromBigDecimal(nameNodeDirCapacityUsed, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsNameNodeDirCapacity.setCapacityUsed(nameNodeDirCapacityUsedAsGb);
                }

                if (apiTimeSeries.getMetadata().getMetricName().equals("capacity")) {
                    BigDecimal nameNodeDirCapacity = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal nameNodeDirCapacityAsGb = DataUnitConverterService.convertFromBigDecimal(nameNodeDirCapacity, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsNameNodeDirCapacity.setCapacity(nameNodeDirCapacityAsGb);
                }
            }
        }
        // dirCapacityDtos.add(hdfsNameNodeDirCapacity);
        return hdfsNameNodeDirCapacity;
    }

}
