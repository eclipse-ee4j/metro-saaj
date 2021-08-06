/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap;

import jakarta.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * These unit tests are based on one of the TCK tests
 */
public class XmlDeclarationUtf16Test extends TestCase {

    private static final String GoodSoapMessage =
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://helloservice.org/wsdl\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><tns:hello><String_1 xsi:type=\"xsd:string\">&lt;Bozo&gt;</String_1></tns:hello></soap:Body></soap:Envelope>";

    public XmlDeclarationUtf16Test(String name) {
        super(name);
    }
    
    public void test1VerifyXmlDeclarationUtf16() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        ByteArrayInputStream bais =
            new ByteArrayInputStream(GoodSoapMessage.getBytes());
        StreamSource ssrc=new StreamSource(bais);
        sp.setContent(ssrc);
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String decoded = baos.toString("utf-16");
        //String decoded = baos.toString();
        assertTrue(
            decoded.indexOf("<?xml") != -1 && decoded.indexOf("utf-16") != -1);
    }

    public void test2VerifyXmlDeclarationUtf16() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        StringReader reader = new StringReader(GoodSoapMessage);
        StreamSource ssrc=new StreamSource(reader);
        sp.setContent(ssrc);
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String decoded = baos.toString("utf-16");
        //String decoded = baos.toString();
        assertTrue(
            decoded.indexOf("<?xml") != -1 && decoded.indexOf("utf-16") != -1);
    }

    public void testVerifyNoXmlDeclarationOutput() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        ByteArrayInputStream bais =
            new ByteArrayInputStream(GoodSoapMessage.getBytes());
        StreamSource ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "false");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String soapmessage = new String(baos.toByteArray());
        assertFalse(soapmessage.indexOf("<?xml") != -1);
    }

    //CR:4952752
    public void testVerifyXmlDeclUtf16() throws Exception {
         MessageFactory factory = MessageFactory.newInstance();
         String xml = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><p:content SOAP-ENV:encodingStyle=\"http://example.com/encoding\" xmlns:p=\"some-uri\">Jeu universel de caract�res cod�s � plusieurs octets</p:content></SOAP-ENV:Body></SOAP-ENV:Envelope>";
           SOAPMessage msg = factory.createMessage();
           msg.getMimeHeaders().setHeader("Content-Type","text/xml; charset=utf-16");
           msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-16"))));
           msg.saveChanges();
           String[] contentType = msg.getMimeHeaders().getHeader("Content-Type");
           if((contentType != null)&&(contentType.length > 0) && 
               (!contentType[0].equals("text/xml; charset=utf-16"))){
               fail();
           } 
    }
}
