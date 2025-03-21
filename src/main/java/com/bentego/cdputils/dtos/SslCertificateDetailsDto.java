package com.bentego.cdputils.dtos;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SslCertificateDetailsDto {
    private String subject;
    private String issuer;
    private String validFrom;
    private String validUntil;
    private String encryptionAlgorithm;
}
