package com.bentego.cdputils.enums;

import com.bentego.cdputils.contants.api.CdpWebInterfacePorts;
import com.bentego.cdputils.contants.roleconfig.*;

import java.util.Arrays;
import java.util.Optional;

public enum RoleConfigUIBinding {

    cloudera_mgmt_EVENTSERVER(MgmtRoleConfigGroupName.EVENTSERVER_0, CdpWebInterfacePorts.MGMT_EVENTSERVER_HTTP, null),
    cloudera_mgmt_ACTIVITYMONITOR(MgmtRoleConfigGroupName.ACTIVITYMONITOR_0, CdpWebInterfacePorts.MGMT_ACTIVITYMONITOR_HTTP, null),
    cloudera_mgmt_HOSTMONITOR(MgmtRoleConfigGroupName.HOSTMONITOR_0, CdpWebInterfacePorts.MGMT_HOSTMONITOR_HTTP, null),
    cloudera_mgmt_NAVIGATOR(MgmtRoleConfigGroupName.NAVIGATOR_0, CdpWebInterfacePorts.MGMT_NAVIGATOR_HTTP, null),
    cloudera_mgmt_NAVIGATORMETASERVER(MgmtRoleConfigGroupName.NAVIGATORMETASERVER_0, CdpWebInterfacePorts.MGMT_NAVIGATORMETASERVER_HTTP, null),
    cloudera_mgmt_REPORTSMANAGER(MgmtRoleConfigGroupName.REPORTSMANAGER_0, CdpWebInterfacePorts.MGMT_REPORTSMANAGER_HTTP, null),
    cloudera_mgmt_SERVICEMONITOR(MgmtRoleConfigGroupName.SERVICEMONITOR_0, CdpWebInterfacePorts.MGMT_SERVICEMONITOR_HTTP, null),

    atlas_ATLAS_SERVER(AtlasRoleConfigGroupName.ATLAS_SERVER_0, CdpWebInterfacePorts.ATLAS_HTTP, CdpWebInterfacePorts.ATLAS_HTTPS),

    cruise_control_CRUISE_CONTROL_SERVER(CruiseControlRoleConfigGroupName.CRUISE_CONTROL_SERVER_0, CdpWebInterfacePorts.CRUISE_CONTROL_SEVER_HTTP, null),

    das_DAS_EVENT_PROCESSOR(DasRoleConfigGroupName.DAS_EVENT_PROCESSOR_0, CdpWebInterfacePorts.DAS_EVENTPROCESSOR_HTTP, null),
    das_DAS_WEBAPP(DasRoleConfigGroupName.DAS_WEBAPP_0, CdpWebInterfacePorts.DAS_WEBAPP_HTTP, null),

    hbase_REGIONSERVER(HbaseRoleConfigGroupName.REGIONSERVER_0, CdpWebInterfacePorts.HBASE_REGION_SERVER_HTTP, null),
    hbase_HBASETHRIFTSERVER(HbaseRoleConfigGroupName.HBASETHRIFTSERVER_0, CdpWebInterfacePorts.HBASE_THRIFT_SERVER_HTTP, null),
    hbase_HBASERESTSERVER(HbaseRoleConfigGroupName.HBASERESTSERVER_0, CdpWebInterfacePorts.HBASE_RESTSERVER_HTTP, null),
    hbase_MASTER(HbaseRoleConfigGroupName.MASTER_0, CdpWebInterfacePorts.HBASE_MASTER_HTTP, null),

    hue_HUE_SERVER(HueRoleConfigGroupName.HUE_SERVER_0, CdpWebInterfacePorts.HUE_SERVER_HTTP, null),
    hue_HUE_LOAD_BALANCER(HueRoleConfigGroupName.HUE_LOAD_BALANCER_0, CdpWebInterfacePorts.HUE_LB_HTTP, null),

    hdfs_DATANODE(HdfsRoleConfigGroupName.DATANODE_0, CdpWebInterfacePorts.HDFS_DATANODE_HTTP, CdpWebInterfacePorts.HDFS_DATANODE_HTTPS),
    hdfs_JOURNALNODE(HdfsRoleConfigGroupName.JOURNALNODE_0, CdpWebInterfacePorts.HDFS_JOURNALNODE_HTTP, CdpWebInterfacePorts.HDFS_JOURNALNODE_HTTPS),
    hdfs_NAMENODE(HdfsRoleConfigGroupName.NAMENODE_0, CdpWebInterfacePorts.HDFS_NAMENODE_HTTP, CdpWebInterfacePorts.HDFS_NAMENODE_HTTPS),

    hive_on_tez_HIVESERVER2(HiveOnTezRoleConfigGroupName.HIVESERVER2_0, CdpWebInterfacePorts.HIVE_SERVER_HTTP, null),

    impala_CATALOGSERVER(ImpalaRoleConfigGroupName.CATALOGSERVER_0, CdpWebInterfacePorts.IMPALA_CATALOGSERVER_HTTP, null),
    impala_STATESTORE(ImpalaRoleConfigGroupName.STATESTORE_0, CdpWebInterfacePorts.IMPALA_STATESTORE_HTTP, null),
    impala_IMPALAD(ImpalaRoleConfigGroupName.IMPALAD_0, CdpWebInterfacePorts.IMPALA_IMPALAD_HTTP, null),

    kudu_KUDU_MASTER(KuduRoleConfigGroupName.KUDU_MASTER_0, CdpWebInterfacePorts.KUDU_MASTER_HTTP, null),
    kudu_KUDU_TSERVER(KuduRoleConfigGroupName.KUDU_TSERVER_0, CdpWebInterfacePorts.KUDU_TSERVER_HTTP, null),

    knox_KNOX_GATEWAY(KnoxRoleConfigGroupName.KNOX_GATEWAY_0, CdpWebInterfacePorts.KNOX_GATEWAY_HTTP, CdpWebInterfacePorts.KNOX_GATEWAY_HTTPS),

    ranger_RANGER_ADMIN(RangerRoleConfigGroupName.RANGER_ADMIN_0, CdpWebInterfacePorts.RANGER_ADMIN_HTTP, CdpWebInterfacePorts.RANGER_ADMIN_HTTPS),

    livy_LIVY_SERVER(LivyRoleConfigGroupName.LIVY_SERVER_0, CdpWebInterfacePorts.LIVY_SERVER_HTTP, null),

    oozie_OOZIE_SERVER(OozieRoleConfigGroupName.OOZIE_SERVER_0, CdpWebInterfacePorts.OOZIE_SERVER_HTTP, CdpWebInterfacePorts.OOZIE_SERVER_HTTPS),

    solr_SOLR_SERVER(SolrRoleConfigGroupName.SOLR_SERVER_0, CdpWebInterfacePorts.SOLR_SERVER_HTTP, CdpWebInterfacePorts.SOLR_SERVER_HTTPS),

    // TODO streamste findfirst yapılıyor handle et
    schemaregistry_SCHEMA_REGISTRY_SERVER(SchemaRegistryRoleConfigGroupName.SCHEMA_REGISTRY_SERVER_0, CdpWebInterfacePorts.SCHEMAREGISTRY_WEBSERVER_HTTP, CdpWebInterfacePorts.SCHEMAREGISTRY_WEBSERVER_HTTPS),
    schemaregistry_SCHEMA_REGISTRY_SERVER2(SchemaRegistryRoleConfigGroupName.SCHEMA_REGISTRY_SERVER_0, CdpWebInterfacePorts.SCHEMAREGISTRY_ADMIN_HTTP, CdpWebInterfacePorts.SCHEMAREGISTRY_ADMIN_HTTPS),

    // TODO rahdpmp01 sparkı kontrol et 2 mi 3 mü
    spark_on_yarn_SPARK_YARN_HISTORY_SERVER(SparkRoleConfigGroupName.SPARK_YARN_HISTORY_SERVER_0, CdpWebInterfacePorts.SPARK3_HISTORY_SERVER_HTTP, CdpWebInterfacePorts.SPARK3_HISTORY_SERVER_HTTPS),

    streams_messaging_manager_STREAMS_MESSAGING_MANAGER_SERVER(StreamsMessagingManagerRoleConfigGroupName.STREAMS_MESSAGING_MANAGER_SERVER_0, CdpWebInterfacePorts.SMM_UI_BOTH, null),
    streams_messaging_manager_STREAMS_MESSAGING_MANAGER_UI(StreamsMessagingManagerRoleConfigGroupName.STREAMS_MESSAGING_MANAGER_UI_0, "8587", null),

    yarn_NODEMANAGER(YarnRoleConfigGroupName.NODEMANAGER_0, CdpWebInterfacePorts.YARN_NODE_MANAGER_HTTP, CdpWebInterfacePorts.YARN_NODE_MANAGER_HTTPS),
    yarn_JOBHISTORY(YarnRoleConfigGroupName.JOBHISTORY_0, CdpWebInterfacePorts.YARN_JOB_HISTORY_SERVER_HTTP, CdpWebInterfacePorts.YARN_JOB_HISTORY_SERVER_HTTPS),
    yarn_RESOURCEMANAGER(YarnRoleConfigGroupName.RESOURCEMANAGER_0, CdpWebInterfacePorts.YARN_RESOURCE_MANAGER_HTTP, CdpWebInterfacePorts.YARN_RESOURCE_MANAGER_HTTPS),

    zeppelin_ZEPPELIN_SERVER(ZeppelinRoleConfigGroupName.ZEPPELIN_SERVER_0, CdpWebInterfacePorts.ZEPPELIN_SERVER_HTTP, CdpWebInterfacePorts.ZEPPELIN_SERVER_HTTPS)
    ;

    private final String roleConfigName;
    private final String port;
    private final String sslPort;

    RoleConfigUIBinding(String roleConfigName, String port, String sslPort) {
        this.roleConfigName = roleConfigName;
        this.port = port;
        this.sslPort = sslPort;
    }

    public String getRoleConfigName() {
        return roleConfigName;
    }

    public String getPort() {
        return port;
    }

    public String getSslPort() {
        return sslPort;
    }

    public static Optional<RoleConfigUIBinding> findByRoleConfigName(String roleConfigName) {
        return Arrays.stream(RoleConfigUIBinding.values())
                .filter(binding -> binding.getRoleConfigName().equals(roleConfigName))
                .findFirst();
    }
}
