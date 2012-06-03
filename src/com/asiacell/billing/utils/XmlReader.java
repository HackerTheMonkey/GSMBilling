package com.asiacell.billing.utils;
/**
 * This XML reader can read any simple XML document
 * the only thing we need to do is to pass the wanted XML parameter
 * as well as the path to the XML data file.
 */
import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class XmlReader 
{

    public String getParameter(String parameterName, String filePath)
            throws Exception {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList docMainNodeList = doc.getElementsByTagName(parameterName);
        Node subNode = docMainNodeList.item(0);
        Element subElement = (Element) subNode;
        NodeList subNodeList = subElement.getChildNodes();
        Node dataNode = subNodeList.item(0);
        return dataNode.getNodeValue();
    }
}