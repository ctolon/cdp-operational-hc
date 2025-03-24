package com.bentego.cdputils.service;

import com.bentego.cdputils.contants.roleconfig.HdfsRoleConfigGroupName;
import com.bentego.cdputils.dtos.HdfsCapacityDto;
import com.bentego.cdputils.dtos.HdfsDirLocationDto;
import com.cloudera.api.swagger.RoleConfigGroupsResourceApi;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.model.ApiConfig;
import com.cloudera.api.swagger.model.ApiRoleConfigGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DirLocatorService {

    Logger logger = LoggerFactory.getLogger(DirLocatorService.class);

    private final RoleConfigGroupsResourceApi roleConfigGroupsResourceApi;


    public DirLocatorService(RoleConfigGroupsResourceApi roleConfigGroupsResourceApi) {

        this.roleConfigGroupsResourceApi = roleConfigGroupsResourceApi;

    }

    public void setHdfsDirLocations(HdfsDirLocationDto hdfsDirLocationDto, String clusterName) throws ApiException {

        // Namenode
        ApiRoleConfigGroup hdfsNamenodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup(clusterName, HdfsRoleConfigGroupName.NAMENODE, "hdfs");
        for (ApiConfig apiConfig: hdfsNamenodeRoleConfigGroup.getConfig().getItems()) {
            if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                hdfsDirLocationDto.setNameNodeDir(apiConfig.getValue());
                logger.info("hdfs namenode directory path for cluster: {}", hdfsDirLocationDto.getNameNodeDir());
            }
        }

        // Datanode
        ApiRoleConfigGroup hdfsDatanodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup(clusterName, HdfsRoleConfigGroupName.DATANODE, "hdfs");
        for (ApiConfig apiConfig: hdfsDatanodeRoleConfigGroup.getConfig().getItems()) {
            if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                hdfsDirLocationDto.setDataNodeDir(apiConfig.getValue());
                logger.info("hdfs datanode directory path for cluster: {}", hdfsDirLocationDto.getDataNodeDir());
            }
        }

        // Journalnode
        ApiRoleConfigGroup hdfsJournalnodeRoleConfigGroup = roleConfigGroupsResourceApi.readRoleConfigGroup(clusterName, HdfsRoleConfigGroupName.JOURNALNODE, "hdfs");
        for (ApiConfig apiConfig: hdfsJournalnodeRoleConfigGroup.getConfig().getItems()) {
            if (apiConfig.getName().contains("dir_list") && apiConfig.getValue().startsWith("/")) {
                hdfsDirLocationDto.setJournalNodeDir(apiConfig.getValue());
                logger.info("hdfs journalnode directory path for cluster: {}", hdfsDirLocationDto.getJournalNodeDir());
            }
        }
    }

}
