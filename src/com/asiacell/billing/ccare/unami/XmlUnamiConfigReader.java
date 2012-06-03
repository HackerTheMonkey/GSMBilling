package com.asiacell.billing.ccare.unami;

import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
public class XmlUnamiConfigReader
{
    public String getParameter(String parameterName)
            throws Exception
    {
        File xmlFile = new File("UNAMI_CFG.xml");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList docMainNodeList = doc.getElementsByTagName(parameterName);
        Node unamiNode = docMainNodeList.item(0);
        Element unamiElement = (Element)unamiNode;
        NodeList unamiNodeList = unamiElement.getChildNodes();
        Node unamiN = unamiNodeList.item(0);
        return unamiN.getNodeValue();
    }
}