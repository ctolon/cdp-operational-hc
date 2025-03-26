package com.bentego.cdputils.service;

import com.bentego.cdputils.dtos.command.HdfsFsckOutputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegexParserService {

    Logger logger = LoggerFactory.getLogger(RegexParserService.class);

    public RegexParserService() {

    }

    public void parseFsckReport(String fsckOutput) {
        if (fsckOutput == null || fsckOutput.isEmpty()) {
            System.out.println("No fsck output received.");
            return;
        }

        // Tüm regex pattern'leri tanımla
        String[][] patterns = {
                {"HDFS Status", "Status:\\s+(\\w+)"},
                {"Number of DataNodes", "Number of data-nodes:\\s+(\\d+)"},
                {"Number of Racks", "Number of racks:\\s+(\\d+)"},
                {"Total Directories", "Total dirs:\\s+(\\d+)"},
                {"Total Symlinks", "Total symlinks:\\s+(\\d+)"},
                {"Total Files", "Total files:\\s+(\\d+)"},
                {"Total Blocks", "Total blocks \\(validated\\):\\s+(\\d+)"},
                {"Avg Block Size", "avg\\. block size (\\d+) B"},
                {"Minimally Replicated Blocks", "Minimally replicated blocks:\\s+(\\d+)"},
                {"Over-Replicated Blocks", "Over-replicated blocks:\\s+(\\d+)"},
                {"Under-Replicated Blocks", "Under-replicated blocks:\\s+(\\d+)"},
                {"Mis-Replicated Blocks", "Mis-replicated blocks:\\s+(\\d+)"},
                {"Default Replication Factor", "Default replication factor:\\s+(\\d+)"},
                {"Avg Block Replication", "Average block replication:\\s+([\\d.]+)"},
                {"Missing Blocks", "Missing blocks:\\s+(\\d+)"},
                {"Corrupt Blocks", "Corrupt blocks:\\s+(\\d+)"},
                {"Missing Replicas", "Missing replicas:\\s+(\\d+)"},
                {"Blocks Queued for Replication", "Blocks queued for replication:\\s+(\\d+)"},

                // Erasure Coded Block Groups
                {"EC Total Size", "Total size:\\s+(\\d+) B"},
                {"EC Total Files", "Total files:\\s+(\\d+)"},
                {"EC Total Block Groups", "Total block groups \\(validated\\):\\s+(\\d+)"},
                {"EC Minimally EC Block Groups", "Minimally erasure-coded block groups:\\s+(\\d+)"},
                {"EC Over-EC Block Groups", "Over-erasure-coded block groups:\\s+(\\d+)"},
                {"EC Under-EC Block Groups", "Under-erasure-coded block groups:\\s+(\\d+)"},
                {"EC Unsatisfactory Placement Block Groups", "Unsatisfactory placement block groups:\\s+(\\d+)"},
                {"EC Avg Block Group Size", "Average block group size:\\s+([\\d.]+)"},
                {"EC Missing Block Groups", "Missing block groups:\\s+(\\d+)"},
                {"EC Corrupt Block Groups", "Corrupt block groups:\\s+(\\d+)"},
                {"EC Missing Internal Blocks", "Missing internal blocks:\\s+(\\d+)"},
                {"EC Blocks Queued for Replication", "Blocks queued for replication:\\s+(\\d+)"}
        };

        for (String[] entry : patterns) {
            Pattern pattern = Pattern.compile(entry[1]);
            Matcher matcher = pattern.matcher(fsckOutput);
            if (matcher.find()) {
                System.out.println(entry[0] + ": " + matcher.group(1));
            }
        }
    }

    public HdfsFsckOutputDto parseFsckReport2(String fsckOutput) {
        if (fsckOutput == null || fsckOutput.isEmpty()) {
            System.out.println("No fsck output received.");
            return null;
        }

        HdfsFsckOutputDto report = new HdfsFsckOutputDto();
        String[][] patterns = {
                {"status", "Status:\\s+(\\w+)"},
                {"dataNodes", "Number of data-nodes:\\s+(\\d+)"},
                {"racks", "Number of racks:\\s+(\\d+)"},
                {"totalDirs", "Total dirs:\\s+(\\d+)"},
                {"totalSymlinks", "Total symlinks:\\s+(\\d+)"},
                {"totalFiles", "Total files:\\s+(\\d+)"},
                {"totalBlocks", "Total blocks \\(validated\\):\\s+(\\d+)"},
                {"avgBlockSize", "avg\\. block size (\\d+) B"},
                {"minimallyReplicatedBlocks", "Minimally replicated blocks:\\s+(\\d+)"},
                {"overReplicatedBlocks", "Over-replicated blocks:\\s+(\\d+)"},
                {"underReplicatedBlocks", "Under-replicated blocks:\\s+(\\d+)"},
                {"misReplicatedBlocks", "Mis-replicated blocks:\\s+(\\d+)"},
                {"defaultReplicationFactor", "Default replication factor:\\s+(\\d+)"},
                {"avgBlockReplication", "Average block replication:\\s+([\\d.]+)"},
                {"missingBlocks", "Missing blocks:\\s+(\\d+)"},
                {"corruptBlocks", "Corrupt blocks:\\s+(\\d+)"},
                {"missingReplicas", "Missing replicas:\\s+(\\d+)"},
                {"blocksQueuedForReplication", "Blocks queued for replication:\\s+(\\d+)"},

                {"ecTotalSize", "Total size:\\s+(\\d+) B"},
                {"ecTotalFiles", "Total files:\\s+(\\d+)"},
                {"ecTotalBlockGroups", "Total block groups \\(validated\\):\\s+(\\d+)"},
                {"ecMinimallyEcBlockGroups", "Minimally erasure-coded block groups:\\s+(\\d+)"},
                {"ecOverEcBlockGroups", "Over-erasure-coded block groups:\\s+(\\d+)"},
                {"ecUnderEcBlockGroups", "Under-erasure-coded block groups:\\s+(\\d+)"},
                {"ecUnsatisfactoryPlacementBlockGroups", "Unsatisfactory placement block groups:\\s+(\\d+)"},
                {"ecAvgBlockGroupSize", "Average block group size:\\s+([\\d.]+)"},
                {"ecMissingBlockGroups", "Missing block groups:\\s+(\\d+)"},
                {"ecCorruptBlockGroups", "Corrupt block groups:\\s+(\\d+)"},
                {"ecMissingInternalBlocks", "Missing internal blocks:\\s+(\\d+)"},
                {"ecBlocksQueuedForReplication", "Blocks queued for replication:\\s+(\\d+)"}
        };

        for (String[] entry : patterns) {
            Pattern pattern = Pattern.compile(entry[1]);
            Matcher matcher = pattern.matcher(fsckOutput);
            if (matcher.find()) {
                String value = matcher.group(1);
                try {
                    switch (entry[0]) {
                        case "status":
                            report.setStatus(value);
                            break;
                        case "avgBlockReplication":
                        case "ecAvgBlockGroupSize":
                            report.getClass().getMethod("set" + capitalize(entry[0]), double.class)
                                    .invoke(report, Double.parseDouble(value));
                            break;
                        case "avgBlockSize":
                        case "ecTotalSize":
                            report.getClass().getMethod("set" + capitalize(entry[0]), long.class)
                                    .invoke(report, Long.parseLong(value));
                            break;
                        default:
                            report.getClass().getMethod("set" + capitalize(entry[0]), int.class)
                                    .invoke(report, Integer.parseInt(value));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return report;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
