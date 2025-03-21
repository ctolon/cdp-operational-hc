package com.bentego.cdputils.contants.api;

public final class CdpWebInterfacePorts {
    public static final String ATLAS_HTTP = "31000";
    public static final String ATLAS_HTTPS = "31443";

    public static final String CRUISE_CONTROL_SEVER_HTTP = "8899";

    public static final String DAS_WEBAPP_HTTP = "30800";
    public static final String DAS_EVENTPROCESSOR_HTTP = "30900";

    public static final String HBASE_MASTER_HTTP = "16010";
    public static final String HBASE_REGION_SERVER_HTTP = "16030";
    public static final String HBASE_THRIFT_SERVER_HTTP = "9095";
    public static final String HBASE_RESTSERVER_HTTP = "8085";

    public static final String HDFS_NAMENODE_HTTP = "9870";
    public static final String HDFS_NAMENODE_HTTPS = "9871";
    public static final String HDFS_SECONDARY_NAMENODE_HTTP = "9868";
    public static final String HDFS_SECONDARY_NAMENODE_HTTPS = "9869";
    public static final String HDFS_JOURNALNODE_HTTP = "8480";
    public static final String HDFS_JOURNALNODE_HTTPS = "8481";
    public static final String HDFS_DATANODE_HTTP = "9864"; // TODO Check it
    public static final String HDFS_DATANODE_HTTPS = "9865";

    public static final String HIVE_SERVER_HTTP = "10002";

    public static final String HUE_LB_HTTP = "8888";
    public static final String HUE_SERVER_HTTP = "8889";

    public static final String IMPALA_CATALOGSERVER_HTTP = "25020";
    public static final String IMPALA_STATESTORE_HTTP = "25010";
    public static final String IMPALA_IMPALAD_HTTP = "25000";

    public static final String KUDU_MASTER_HTTP = "8051";
    public static final String KUDU_TSERVER_HTTP = "8050";

    public static final String KNOX_GATEWAY_HTTP = "8443";
    public static final String KNOX_GATEWAY_HTTPS = "8444";

    public static final String LIVY_SERVER_HTTP = "8998";

    public static final String OOZIE_SERVER_HTTP = "11000";
    public static final String OOZIE_SERVER_HTTPS = "11443";

    public static final String RANGER_ADMIN_HTTP = "6080";
    public static final String RANGER_ADMIN_HTTPS = "6182";

    // TODO eğer sadece spark3 kuruluysa ilk spark alınmalı
    public static final String SPARK_HISTORY_SERVER_HTTP = "18088";
    public static final String SPARK_HISTORY_SERVER_HTTPS = "18488";
    public static final String SPARK3_HISTORY_SERVER_HTTP = "18089";
    public static final String SPARK3_HISTORY_SERVER_HTTPS = "18489";

    public static final String SCHEMAREGISTRY_WEBSERVER_HTTP = "7788";
    public static final String SCHEMAREGISTRY_WEBSERVER_HTTPS = "7790";
    public static final String SCHEMAREGISTRY_ADMIN_HTTP = "7789";
    public static final String SCHEMAREGISTRY_ADMIN_HTTPS = "7791";

    public static final String SMM_UI_BOTH = "9991";

    public static final String YARN_JOB_HISTORY_SERVER_HTTP = "19888";
    public static final String YARN_JOB_HISTORY_SERVER_HTTPS = "19890";
    public static final String YARN_RESOURCE_MANAGER_HTTP = "8088";
    public static final String YARN_RESOURCE_MANAGER_HTTPS = "8090";
    public static final String YARN_NODE_MANAGER_HTTP = "8042";
    public static final String YARN_NODE_MANAGER_HTTPS = "8044";

    public static final String ZEPPELIN_SERVER_HTTP = "8885";
    public static final String ZEPPELIN_SERVER_HTTPS = "8886";

    public static final String SOLR_SERVER_HTTP = "8983";
    public static final String SOLR_SERVER_HTTPS = "8985";

    public static final String MGMT_EVENTSERVER_HTTP = "8084";
    public static final String MGMT_ACTIVITYMONITOR_HTTP = "8087";
    public static final String MGMT_HOSTMONITOR_HTTP = "8091";
    public static final String MGMT_NAVIGATOR_HTTP = "8089";
    public static final String MGMT_NAVIGATORMETASERVER_HTTP = "7779";
    public static final String MGMT_REPORTSMANAGER_HTTP = "8083";
    public static final String MGMT_SERVICEMONITOR_HTTP = "8086";
}
