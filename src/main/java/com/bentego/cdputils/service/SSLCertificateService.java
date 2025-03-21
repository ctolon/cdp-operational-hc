package com.bentego.cdputils.service;

import com.bentego.cdputils.dtos.SslCertificateDetailsDto;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


@Service
public class SSLCertificateService {

    public SslCertificateDetailsDto getSSLCertificateDetails(String urlString, Integer customPort) {
        // Map<String, String> certDetails = new HashMap<>();
        SslCertificateDetailsDto sslCertificateDetailsDto = new SslCertificateDetailsDto();
        int port = 443;
        String host = "";

        try {
            if (urlString.startsWith("https://")) {
                host = urlString.replaceFirst("https://", "").split("/")[0];
            } else if (urlString.startsWith("http://")) {
                host = urlString.replaceFirst("http://", "").split("/")[0];
            }
            //else {
            //    return null;
            //}


            if (customPort != null) {
                port = customPort;
            }

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
                socket.startHandshake();

                X509Certificate[] certificates = (X509Certificate[]) socket.getSession().getPeerCertificates();

                if (certificates.length > 0) {
                    X509Certificate certificate = certificates[0];

                    sslCertificateDetailsDto.setSubject(certificate.getSubjectDN().toString());
                    sslCertificateDetailsDto.setIssuer(certificate.getIssuerDN().toString());
                    sslCertificateDetailsDto.setValidFrom(certificate.getNotBefore().toString());
                    sslCertificateDetailsDto.setValidUntil(certificate.getNotAfter().toString());
                    sslCertificateDetailsDto.setEncryptionAlgorithm(certificate.getSigAlgName());

                    //certDetails.put("subject", certificate.getSubjectDN().toString());
                    //certDetails.put("issuer", certificate.getIssuerDN().toString());
                    //certDetails.put("validFrom", certificate.getNotBefore().toString());
                    //certDetails.put("validUntil", certificate.getNotAfter().toString());
                    //certDetails.put("encryptionAlgorithm", certificate.getSigAlgName());
                }
            }
        } catch (Exception e) {
            //certDetails.put("error", "error when fetching ssl certificate: " + e.getMessage());
        }
        //return certDetails;
        return sslCertificateDetailsDto;
    }
}
