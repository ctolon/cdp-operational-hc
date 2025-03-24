package com.bentego.cdputils.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cdp.web-interface")
public class CdpWebInterfaceConfig {
    private String atlasHttp = "31000";
    private String atlasHttps = "31443";
    private String cruiseControlServerHttp = "8899";
    private String dasWebappHttp = "30800";
    private String dasEventprocessorHttp = "30900";
    private String hbaseMasterHttp = "16010";
    private String hbaseRegionServerHttp = "16030";
    private String hbaseThriftServerHttp = "9095";
    private String hbaseRestServerHttp = "8085";
    private String hdfsNamenodeHttp = "9870";
    private String hdfsNamenodeHttps = "9871";
    private String hdfsSecondaryNamenodeHttp = "9868";
    private String hdfsSecondaryNamenodeHttps = "9869";
    private String hdfsJournalnodeHttp = "8480";
    private String hdfsJournalnodeHttps = "8481";
    private String hdfsDatanodeHttp = "9864";
    private String hdfsDatanodeHttps = "9865";
    private String hiveServerHttp = "10002";
    private String hueLbHttp = "8888";
    private String hueServerHttp = "8889";
    private String impalaCatalogServerHttp = "25020";
    private String impalaStatestoreHttp = "25010";
    private String impalaImpaladHttp = "25000";
    private String kuduMasterHttp = "8051";
    private String kuduTserverHttp = "8050";
    private String knoxGatewayHttp = "8443";
    private String knoxGatewayHttps = "8444";
    private String livyServerHttp = "8998";
    private String oozieServerHttp = "11000";
    private String oozieServerHttps = "11443";
    private String rangerAdminHttp = "6080";
    private String rangerAdminHttps = "6182";
    private String sparkHistoryServerHttp = "18088";
    private String sparkHistoryServerHttps = "18488";
    private String spark3HistoryServerHttp = "18089";
    private String spark3HistoryServerHttps = "18489";
    private String schemaRegistryWebserverHttp = "7788";
    private String schemaRegistryWebserverHttps = "7790";
    private String schemaRegistryAdminHttp = "7789";
    private String schemaRegistryAdminHttps = "7791";
    private String smmUiBoth = "9991";
    private String yarnJobHistoryServerHttp = "19888";
    private String yarnJobHistoryServerHttps = "19890";
    private String yarnResourceManagerHttp = "8088";
    private String yarnResourceManagerHttps = "8090";
    private String yarnNodeManagerHttp = "8042";
    private String yarnNodeManagerHttps = "8044";
    private String zeppelinServerHttp = "8885";
    private String zeppelinServerHttps = "8886";
    private String solrServerHttp = "8983";
    private String solrServerHttps = "8985";
    private String mgmtEventServerHttp = "8084";
    private String mgmtActivityMonitorHttp = "8087";
    private String mgmtHostMonitorHttp = "8091";
    private String mgmtNavigatorHttp = "8089";
    private String mgmtNavigatorMetaServerHttp = "7779";
    private String mgmtReportsManagerHttp = "8083";
    private String mgmtServiceMonitorHttp = "8086";

    // Getters ve Setters
}
