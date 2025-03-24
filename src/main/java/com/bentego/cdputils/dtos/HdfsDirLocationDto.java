package com.bentego.cdputils.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HdfsDirLocationDto {
    private String nameNodeDir;
    private String dataNodeDir;
    private String journalNodeDir;
}
