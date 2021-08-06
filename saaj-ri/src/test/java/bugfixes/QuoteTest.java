/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
*
* @author SAAJ RI Development Team 
*/

package bugfixes;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import jakarta.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.TestCase;

public class QuoteTest extends TestCase {
    
    public QuoteTest(String name) {
        super(name);
    }    
    
    /*
     * getValue returned on element should not get truncated after 
     * encountering quot;
     */ 
    public void testQuoteGetValue() {
        String arg = "src/test/resources/bugfixes/data/quot.xml";
        try {
            doit1(arg);
        } catch (Throwable t) {
            fail("Get Quote test failed" + t.getMessage());
            // t.printStackTrace();
        }
    }
    
    static String  x509IssuerName = 
        "CN=VeriSign Class 3 Code Signing 2001 CA," +
        " OU=Terms of use at https://www.verisign.com/rpa (c)01," +
        " OU=VeriSign Trust Network, O=\"VeriSign, Inc.\"";

    public void doit1(String fileName) throws Exception {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        /*
         * This next statement will affect how the quote entities are
         * processed by the parser
         */
        
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        
        StreamSource source = new StreamSource(new FileInputStream(fileName));
        soapPart.setContent(source);
        message.saveChanges();
        
        SOAPHeader soapHeader = message.getSOAPPart().getEnvelope().getHeader();
        //System.out.println("\nnodeToString=" + nodeToString(soapHeader) + "\n\n");        
        
        Iterator iterator = null;
        SOAPElement keyInfoElement = null;  
        
        Name issuerSerial = SOAPFactory.newInstance()
            .createName("X509IssuerSerial", null, 
            "http://www.w3.org/2000/09/xmldsig#");
        iterator = soapHeader.getChildElements();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof SOAPElement) {
                SOAPElement soapElement = (SOAPElement)o;
                keyInfoElement = findElement((SOAPElement)o, issuerSerial);
                //System.out.println("\nnodeToString(keyInfoElement)=" + 
                //    nodeToString(keyInfoElement) + "\n\n");
                Iterator iterator2 = keyInfoElement.getChildElements();
                while (iterator2.hasNext()) {
                    o = iterator2.next();
                    if (o instanceof SOAPElement) {
                        soapElement = (SOAPElement)o;
                        if ("X509IssuerName".
                                equalsIgnoreCase(soapElement.getLocalName())) {
                            if (!x509IssuerName.equalsIgnoreCase(
                            soapElement.getValue()))
                                fail("Wrong IssuerName returned \n" + 
                                soapElement.getValue());
                        }
                    }
                }
                break;
            }
        }
        
    }
    
    /** Convert a node tree into a STRING representation */
    public String nodeToString(org.w3c.dom.Node node) throws Exception {
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
        Transformer transformer = tFactory.newTransformer();
        StringWriter stringWriter = new StringWriter();
        
        DOMSource source = new DOMSource(node);
        StreamResult result = new StreamResult(stringWriter);
        
        transformer.transform(source, result);
        return stringWriter.toString();
    }
    
    public SOAPElement findElement(SOAPElement soapElement, Name name) {
        Name n = soapElement.getElementName();
        if (n.equals(name)) {
            return soapElement;
        }
        Iterator iterator = soapElement.getChildElements();
        while (iterator.hasNext()) {
            Object o = (Object)iterator.next();
            if (o instanceof SOAPElement) {
                SOAPElement result = findElement((SOAPElement)o, name);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
    
}
