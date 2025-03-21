package com.bentego.cdputils.service;

import com.bentego.cdputils.builder.TimeseriesQueryBuilder;
import com.bentego.cdputils.dtos.HdfsCapacityDto;
import com.bentego.cdputils.enums.DataUnit;
import com.cloudera.api.swagger.TimeSeriesResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.ApiTimeSeries;
import com.cloudera.api.swagger.model.ApiTimeSeriesResponse;
import com.cloudera.api.swagger.model.ApiTimeSeriesResponseList;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TimeSeriesService {

    private final TimeSeriesResourceApi timeSeriesResourceApi;

    public TimeSeriesService(TimeSeriesResourceApi timeSeriesResourceApi) {
        this.timeSeriesResourceApi = timeSeriesResourceApi;
    }

    public HdfsCapacityDto getGeneralHdfsCapacity() throws ApiException {

        HdfsCapacityDto hdfsCapacityDto = new HdfsCapacityDto();
        String hdfsGeneralCapacityQuery = TimeseriesQueryBuilder.buildHdfsCapacityCheckQuery();
        ApiTimeSeriesResponseList hdfsGeneralCapacityTsResponse = timeSeriesResourceApi.queryTimeSeries("application/json",
                "RAW",
                null,
                false,
                hdfsGeneralCapacityQuery,
                "now");

        for (ApiTimeSeriesResponse apiTimeSeriesResponse: hdfsGeneralCapacityTsResponse.getItems()) {
            for (ApiTimeSeries apiTimeSeries: apiTimeSeriesResponse.getTimeSeries()) {

                if (apiTimeSeries.getMetadata().getMetricName().equals("dfs_capacity")) {
                    BigDecimal latestValue = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal latestValueAsGb = DataUnitConverterService.convertFromBigDecimal(latestValue, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsCapacityDto.setDfsCapacity(latestValueAsGb);
                }

                if (apiTimeSeries.getMetadata().getMetricName().equals("dfs_capacity_used")) {
                    BigDecimal latestValue = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal latestValueAsGb = DataUnitConverterService.convertFromBigDecimal(latestValue, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsCapacityDto.setDfsCapacityUsed(latestValueAsGb);
                }

                if (apiTimeSeries.getMetadata().getMetricName().equals("dfs_capacity_used_non_hdfs")) {
                    BigDecimal latestValue = apiTimeSeries.getData().get(apiTimeSeries.getData().size() -1).getValue();
                    BigDecimal latestValueAsGb = DataUnitConverterService.convertFromBigDecimal(latestValue, DataUnit.BYTE, DataUnit.GIGABYTE);
                    hdfsCapacityDto.setDfsCapacityUsedNonHdfs(latestValueAsGb);
                }
            }
        }
        return hdfsCapacityDto;
    }
}
