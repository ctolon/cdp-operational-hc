package com.bentego.cdputils.dtos.command;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HdfsFsckOutputDto {
    private String status;
    private int dataNodes;
    private int racks;
    private int totalDirs;
    private int totalSymlinks;
    private int totalFiles;
    private int totalBlocks;
    private long avgBlockSize;
    private int minimallyReplicatedBlocks;
    private int overReplicatedBlocks;
    private int underReplicatedBlocks;
    private int misReplicatedBlocks;
    private int defaultReplicationFactor;
    private double avgBlockReplication;
    private int missingBlocks;
    private int corruptBlocks;
    private int missingReplicas;
    private int blocksQueuedForReplication;

    private long ecTotalSize;
    private int ecTotalFiles;
    private int ecTotalBlockGroups;
    private int ecMinimallyEcBlockGroups;
    private int ecOverEcBlockGroups;
    private int ecUnderEcBlockGroups;
    private int ecUnsatisfactoryPlacementBlockGroups;
    private double ecAvgBlockGroupSize;
    private int ecMissingBlockGroups;
    private int ecCorruptBlockGroups;
    private int ecMissingInternalBlocks;
    private int ecBlocksQueuedForReplication;
}
