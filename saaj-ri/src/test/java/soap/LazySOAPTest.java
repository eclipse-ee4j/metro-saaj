/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.messaging.saaj.soap.MessageImpl;

import junit.framework.TestCase;

public class LazySOAPTest extends TestCase {
    public void testStaxCorrectness() throws Exception {
        //make a small message
        String soapMsg = makeSoapMessageString(3);
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        InputStream in = new ByteArrayInputStream(soapMsg.getBytes("UTF-8")); 
//      System.out.println("soap msg: " + soapMsg);
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, in);
        msg.setProperty(MessageImpl.LAZY_SOAP_BODY_PARSING, "true");
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        traverseStaxSoapMessageForCorrectness(env);
        
        //write and re-read, make sure it's still correct
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        msg.writeTo(bos);
        msg = MessageFactory.newInstance().createMessage(headers, new ByteArrayInputStream(bos.toByteArray()));
        env = msg.getSOAPPart().getEnvelope();
        traverseStaxSoapMessageForCorrectness(env);
        
    }
    
    public void testLazyEnvelopeWrite() throws Exception {
      //make a small message
        String soapMsg = makeSoapMessageString(3);
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        InputStream in = new ByteArrayInputStream(soapMsg.getBytes("UTF-8")); 
//      System.out.println("soap msg: " + soapMsg);
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, in);
        msg.setProperty(MessageImpl.LAZY_SOAP_BODY_PARSING, "true");
        //force envelope to be parsed
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        
        //now write lazy
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(bos);
//        ((LazyEnvelope) env).writeTo(w);
        Method method = env.getClass().getMethod("writeTo", new Class[]{XMLStreamWriter.class});
        method.invoke(env, new Object[]{w});
        w.flush();
        //TODO desagar Body is EMPTY!! Why? Fix it
        String writtenSoap = bos.toString();
        System.out.println(writtenSoap);
        assertTrue(writtenSoap.indexOf("Hello") > 0);
        

    }
    
    public void testLazySoapFault() throws Exception {
        String soapMsg = makeSoapFaultMessageString();
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        InputStream in = new ByteArrayInputStream(soapMsg.getBytes("UTF-8")); 
//      System.out.println("soap msg: " + soapMsg);
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, in);
        msg.setProperty(MessageImpl.LAZY_SOAP_BODY_PARSING, "true");
        //force envelope to be parsed
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPFault fault = env.getBody().getFault();
        assertNotNull(fault);
    }
    /**
     * Use DOM APIs to traverse to make sure message is materialized correctly
     * @param elem
     */
    private void traverseStaxSoapMessageForCorrectness(SOAPElement elem) {
        assertEquals("Envelope", elem.getLocalName());
        Node child;
        child = getElementChild(elem);
        assertTrue(child instanceof javax.xml.soap.SOAPHeader);
        assertEquals("Header", child.getLocalName());
        
        child = getElementSibling(child);
        assertTrue(child instanceof javax.xml.soap.SOAPBody);
        assertEquals("Body", child.getLocalName());
        
        child = getElementChild(child);
        assertEquals("Hello", child.getLocalName());
        
        NodeList children = child.getChildNodes();
        int elemChildren = 0;
        for (int i=0;i<children.getLength();i++) {
            Node nextChild = children.item(i);
            if (nextChild.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            elemChildren++;
            assertEquals("String_1", nextChild.getLocalName());
            
        }
        assertEquals(3, elemChildren);
    }
    
    private Node getElementSibling(Node elem) {
        Node sib;
        do {
            sib = elem.getNextSibling();
        } while (sib != null && sib.getNodeType() != Node.ELEMENT_NODE);
        return sib;
    }

    private Node getElementChild(Node elem) {
        Node child;
        for (child = elem.getFirstChild();child != null && child.getNodeType() != Node.ELEMENT_NODE;child = child.getNextSibling());
        return child;
    }
    
    private String makeSoapMessageString(int numPayloadElems) {
        String soapPrefix = "<env:Envelope"
                        + " xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'"
                        + " xmlns:ns1='http://example.com/wsdl'>"
                        +    "<env:Header/>"
                        +    "<env:Body>"
                        +      "<ns1:Hello>";

        String soapSuffix =
        "</ns1:Hello>"
                +    "</env:Body>"
                + "</env:Envelope>";
        
        StringBuilder soap = new StringBuilder(soapPrefix);
        for (int i=0;i<numPayloadElems;i++) {
            soap.append("<String_1>Duke " + i + "</String_1>");
        }
        soap.append(soapSuffix);
        return soap.toString();
    }
    
    private String makeSoapFaultMessageString() {
        String soapPrefix = "<env:Envelope"
                        + " xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'"
                        + " xmlns:ns1='http://example.com/wsdl'>"
                        +    "<env:Header/>"
                        +    "<env:Body>"
                        +      "<env:Fault>";

        String soapSuffix =
        "</env:Fault>"
                +    "</env:Body>"
                + "</env:Envelope>";
        
        StringBuilder soap = new StringBuilder(soapPrefix);
        soap.append("<env:faultcode>myfaultcode</env:faultcode>");
        soap.append("<env:faultstring>myfaultcode</env:faultstring>");
        soap.append("<env:faultactor>myfaultactor</env:faultactor>");
        soap.append("<env:detail><mydetailelem/></env:detail>");

        soap.append(soapSuffix);
        return soap.toString();
    }
}
