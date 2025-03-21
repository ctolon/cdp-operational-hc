package com.bentego.cdputils.service;

import com.bentego.cdputils.builder.TimeseriesQueryBuilder;
import com.bentego.cdputils.dtos.HdfsCapacityDto;
import com.bentego.cdputils.enums.DataUnit;
import com.cloudera.api.swagger.TimeSeriesResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.ApiTimeSeries;
import com.cloudera.api.swagger.model.ApiTimeSeriesData;
import com.cloudera.api.swagger.model.ApiTimeSeriesResponse;
import com.cloudera.api.swagger.model.ApiTimeSeriesResponseList;
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
}
