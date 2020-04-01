/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * Tests for Bug Id: 4952738
 */
public class WriteXmlDeclarationTest extends TestCase {

    private static final String xmlDecl =
        "<?xml version=\"1.0\" encoding=\"utf-8\"";

    public WriteXmlDeclarationTest(String name) {
        super(name);
    }

    public void testXmlDeclWithInputStream1() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        byte[] utf8bytes = testString.getBytes("utf-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf8bytes);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(new StreamSource(bais));
        soapPart.getEnvelope();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }

    public void testXmlDeclWithInputStream2() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        byte[] utf8bytes = testString.getBytes("utf-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf8bytes);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(new StreamSource(bais));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }

    public void testXmlDeclWithReader1() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        StringReader reader = new StringReader(testString);
        StreamSource content = new StreamSource(reader);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(content);
        soapPart.getEnvelope();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }

    public void testXmlDeclWithReader2() throws Exception {
        String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        StringReader reader = new StringReader(testString);
        StreamSource content = new StreamSource(reader);
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(content);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        String msgString = baos.toString("utf-8");
        assertTrue(msgString.startsWith(xmlDecl));
    }
}
