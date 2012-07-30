/*
 * XMLPropertiesHandler.java
 *
 * Created on 27 January 2005, 00:38
 *
 * AsItHappens - real-time network monitor
 * Copyright (C) 2006  Mark Abrahams
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package nz.co.abrahams.asithappens.core;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

/**
 *
 * @author mark
 */
public class XMLPropertiesHandler extends DefaultHandler {
    
    /** Creates a new instance of PropertiesParser */
    public XMLPropertiesHandler() {
    }
    
    public static void parseFile(File file) throws ParserConfigurationException, SAXException, IOException {
        XMLPropertiesHandler handler;
        SAXParserFactory factory;
        SAXParser parser;
        
        handler = new XMLPropertiesHandler();
        factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
        parser.parse(file, handler);
    }
    
    public void startDocument()
    throws SAXException {
        System.out.println("<?xml version='1.0' encoding='UTF-8'?>");
    }
    
    public void endDocument()
    throws SAXException {
        System.out.println("End");
    }   
    
    public void startElement(String namespaceURI,
            String lName, // local name
            String qName, // qualified name
            Attributes attrs)
            throws SAXException {
        String eName = lName; // element name
        if ("".equals(eName)) eName = qName; // namespaceAware = false
        System.out.print("<"+eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name
                if ("".equals(aName)) aName = attrs.getQName(i);
                System.out.print(" ");
                System.out.print(aName+"=\""+attrs.getValue(i)+"\"");
            }
        }
        System.out.print(">");
    }
    
    public void endElement(String namespaceURI,
            String sName, // simple name
            String qName  // qualified name
            )
            throws SAXException {
        System.out.print("</"+sName+">");
    }
    
    public void characters(char buf[], int offset, int len)
    throws SAXException {
        String s = new String(buf, offset, len);
        System.out.print(s);
    }
    
    public static void main(String argv[]) {
        try {
            XMLPropertiesHandler.parseFile(new File(argv[0]));
        } catch (Exception e) {
            //ErrorHandler.logMessage(e.getMessage(), e);
        }
    }   
}
