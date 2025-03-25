package com.bentego.cdputils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Service
public class XmlReaderService {

    Logger logger = LoggerFactory.getLogger(XmlReaderService.class);

    private static final String HDFS_SITE_XML_PATH = "/etc/hadoop/conf/hdfs-site.xml";

    public XmlReaderService() {

    }

    public String getHdfsReplicationFactor() {
        try {
            File file = new File(HDFS_SITE_XML_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList propertyList = document.getElementsByTagName("property");

            for (int i = 0; i < propertyList.getLength(); i++) {
                Node propertyNode = propertyList.item(i);
                if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element propertyElement = (Element) propertyNode;
                    String name = propertyElement.getElementsByTagName("name").item(0).getTextContent();

                    if ("dfs.replication".equals(name)) {
                        return propertyElement.getElementsByTagName("value").item(0).getTextContent();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

