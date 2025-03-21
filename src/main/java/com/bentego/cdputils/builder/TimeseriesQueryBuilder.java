package com.bentego.cdputils.builder;


public final class TimeseriesQueryBuilder {

    public static String buildDirectoryCapacityCheckQuery(String hostId, String path, String category) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT capacity_used, capacity ")
                .append("WHERE hostId = '").append(hostId).append("' ")
                .append("AND path = '").append(path).append("' ")
                .append("AND category = '").append(category).append("'");

        return query.toString();

    }

    public static String buildHdfsCapacityCheckQuery() {
        return "SELECT dfs_capacity, dfs_capacity_used, dfs_capacity_used_non_hdfs WHERE entityName = 'hdfs'";
    }
}
