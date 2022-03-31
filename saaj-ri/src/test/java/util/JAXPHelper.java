/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

public class JAXPHelper {
    private static DocumentBuilder builder;
    private static Transformer transformer;

    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Create an instance of our own transformer factory impl
            TransformerFactory transFactory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());

            // create Transformer
            transformer = transFactory.newTransformer();

        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser     
            System.out.println("* Transformer Factory error");
            System.out.println("  " + tce.getMessage());

            // Use the contained exception, if any      
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            x.printStackTrace();

        }
    }

    public static Source readXMLFile(String xmlFileName) {

        Source domSource = null;

        try {
            Document domDoc = builder.parse(new File(xmlFileName));
            domSource = new javax.xml.transform.dom.DOMSource(domDoc);

        } catch (SAXParseException spe) {
            // Error generated by the parser    
            System.out.println(
                "\n** Parsing error"
                    + ", line "
                    + spe.getLineNumber()
                    + ", uri "
                    + spe.getSystemId());
            System.out.println("  " + spe.getMessage());

            // Use the contained exception, if any  
            Exception x = spe;
            if (spe.getException() != null)
                x = spe.getException();
            x.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return domSource;
    }
}
