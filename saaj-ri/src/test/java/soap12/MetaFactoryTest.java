/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
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

package soap12;


import jakarta.xml.soap.*;
import java.io.*;
import javax.xml.namespace.QName;

import junit.framework.TestCase;


public class MetaFactoryTest extends TestCase {

    public MetaFactoryTest(String name) {
        super(name);
    }

    public void testSOAPFactory11() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        //assertTrue(factory instanceof com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl);
        assertTrue(factory.getClass().getName().contains("soap.ver1_1.SOAPFactory1_1Impl"));
    }

    public void testMessageFactory11()  throws Exception {
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        //assertTrue(factory instanceof com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl);
        assertTrue(factory.getClass().getName().contains("soap.ver1_1.SOAPMessageFactory1_1Impl"));
        // SOAP 11 Msg factory need not look for a Content-Type header
        SOAPMessage msg = factory.createMessage(
                            new MimeHeaders(), 
                            new FileInputStream("src/test/resources/dynamic11.xml"));
        SOAPBody body = msg.getSOAPBody();
        //assertTrue(body instanceof com.sun.xml.messaging.saaj.soap.ver1_1.Body1_1Impl);
        assertTrue(body.getClass().getName().contains("soap.ver1_1.Body1_1Impl"));
    }

    public void testSOAPFactory12()  throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        //assertTrue(factory instanceof com.sun.xml.messaging.saaj.soap.ver1_2.SOAPFactory1_2Impl);
        assertTrue(factory.getClass().getName().contains("soap.ver1_2.SOAPFactory1_2Impl"));
        QName name = new QName("http://www.w3.org/2003/05/soap-envelope", "Body");
        SOAPElement element = factory.createElement(name);
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Body1_2Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_2.Body1_2Impl"));
        Detail detail = factory.createDetail();
        //assertTrue(detail instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Detail1_2Impl);
        assertTrue(detail.getClass().getName().contains("soap.ver1_2.Detail1_2Impl"));
    }

    public void testMessageFactory12()  throws Exception {
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        //assertTrue(factory instanceof com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl);
        assertTrue(factory.getClass().getName().contains("ver1_2.SOAPMessageFactory1_2Impl"));
        // SOAP 12 Msg factory need not look for a Content-Type header
        SOAPMessage msg =
            factory.createMessage(
                null,
                new FileInputStream("./src/test/resources/dynamic12.xml"));
        SOAPBody body = msg.getSOAPBody();
        //assertTrue(body instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Body1_2Impl);
        assertTrue(body.getClass().getName().contains("soap.ver1_2.Body1_2Impl"));
        SOAPHeader sh = msg.getSOAPHeader();
        SOAPHeaderElement she =
            sh.addHeaderElement(
                msg.getSOAPPart().getEnvelope().createName(
                    "HeaderElement1",
                    "he1",
                    "http://foo.xyz.com"));
    }

    public void testSOAPFactoryDynamic()  throws Exception {
     // try to create elements with standard names and ensure the right SOAPDocument is getting
     // created
        SOAPFactory factory = SOAPFactory.newInstance(SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
        assertTrue(factory.getClass().getName().contains("SOAPFactoryDynamicImpl"));

        // test createName methods
        Name name1 = factory.createName("Envelope","SOAP-ENV","http://schemas.xmlsoap.org/soap/envelope/");
        Name name2 = factory.createName("Envelope","SOAP-ENV","http://www.w3.org/2003/05/soap-envelope");

        Name name3 = factory.createName("test-name");

        QName name4 = new QName("http://www.w3.org/2003/05/soap-envelope", "Body");


        SOAPElement element = factory.createElement(name1);
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_1.Envelope1_1Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_1.Envelope1_1Impl"));

        element = factory.createElement(name2);
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Envelope1_2Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_2.Envelope1_2Impl"));
        element = factory.createElement(name3);
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.impl.ElementImpl);
        assertTrue(element.getClass().getName().contains("soap.impl.ElementImpl"));
        element = factory.createElement(name4);
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Body1_2Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_2.Body1_2Impl"));
        element = factory.createElement(
                    "Header", "SOAP-ENV", "http://www.w3.org/2003/05/soap-envelope");
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Header1_2Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_2.Header1_2Impl"));
        element = element.addChildElement(
                    factory.createElement("HeaderChild", null, "http://chemas.xmlsoap.org/soap/envelope/"));
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_2.HeaderElement1_2Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_2.HeaderElement1_2Impl"));
        element = factory.createElement("Header");
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.impl.ElementImpl);
        assertTrue(element.getClass().getName().contains("soap.impl.ElementImpl"));
        element = factory.createElement("Header", null, "http://www.w3.org/2003/05/soap-envelope");
        //assertTrue(element instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Header1_2Impl);
        assertTrue(element.getClass().getName().contains("soap.ver1_2.Header1_2Impl"));

        try {
            Detail detail = factory.createDetail();
            fail("Dynamic Protocol does not support createDetail");
        } catch (UnsupportedOperationException ex) {
        }

    }

    public void testMessageFactoryDynamic()  throws Exception {
        MessageFactory factory =  MessageFactory.newInstance(SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
        assertTrue(factory.getClass().getName().contains("SOAPMessageFactoryDynamicImpl"));

        try {
            factory.createMessage();
            assertTrue(false);
        } catch (UnsupportedOperationException ex) {
        }

        // create message with Content-Type header SOAP 1.1
        MimeHeaders headers = new MimeHeaders();
        headers.setHeader("Content-Type","text/xml");  
        SOAPMessage msg = factory.createMessage(
                            headers, new FileInputStream("src/test/resources/dynamic11.xml"));
        SOAPBody body = msg.getSOAPBody();
        //assertTrue(body instanceof com.sun.xml.messaging.saaj.soap.ver1_1.Body1_1Impl);
        assertTrue(body.getClass().getName().contains("soap.ver1_1.Body1_1Impl"));

        // create message with Content-Type header SOAP 1.2
        headers = new MimeHeaders();
        headers.setHeader("Content-Type","application/soap+xml");  
        msg = factory.createMessage(
                            headers, new FileInputStream("src/test/resources/dynamic12.xml"));
        body = msg.getSOAPBody();
        //assertTrue(body instanceof com.sun.xml.messaging.saaj.soap.ver1_2.Body1_2Impl);
        assertTrue(body.getClass().getName().contains("soap.ver1_2.Body1_2Impl"));
        // create message with no Content-Type header 
        try {
            headers = new MimeHeaders();
            msg = factory.createMessage(
                                headers, new FileInputStream("src/test/resources/dynamic12.xml"));
            assertTrue(false);
        } catch (SOAPException e) {
        }

        // create message with Content-Type header SOAP 1.1, but message type SOAP 1.2
        try {
            headers = new MimeHeaders();
            headers.setHeader("Content-Type","text/xml");  
            msg = factory.createMessage(
                                headers, new FileInputStream("src/test/resources/dynamic12.xml"));
            body = msg.getSOAPBody();
            fail("expected SOAPException here");
        } catch (Exception e) {
        }

        // create message with Content-Type header SOAP 1.2, but message type SOAP 1.1
        try {
            headers = new MimeHeaders();
            headers.setHeader("Content-Type","application/soap+xml");  
            msg = factory.createMessage(
                                headers, new FileInputStream("src/test/resources/dynamic11.xml"));
            body = msg.getSOAPBody();
            fail("expected SOAPException here");
        } catch (Exception e) {
        }
    }

    public void testSOAPFactoryJunk()  throws Exception {
        try {
            SOAPFactory factory = SOAPFactory.newInstance("junk");
            assertTrue(false);
        } catch (SOAPException ex) {
        }
    }

    public void testMessageFactoryJunk()  throws Exception {
        try {
            MessageFactory factory = MessageFactory.newInstance("junk");
            assertTrue(false);
        } catch (SOAPException ex) {
        }
    }
}
