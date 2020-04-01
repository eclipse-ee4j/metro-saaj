/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package mime;

import jakarta.xml.soap.*;
import jakarta.activation.DataHandler;

import javax.xml.transform.stream.StreamSource;

import java.io.StringReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

/**
 * Tests for Bug ID: 4991879
 */
public class XmlAttachmentTest extends TestCase {

    public XmlAttachmentTest(String name) {
        super(name);
    }

    public void testXmlAttachmentWithXmlDecl() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>    <START><A>Hello</A><B>World</B></START>";
        StringReader reader = new StringReader(xml);
        StreamSource content = new StreamSource(reader);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        handler.writeTo(baos);
        String reconstructedXml = baos.toString("utf-8");
        assertEquals(
            "Attachment content is preserved", xml, reconstructedXml);
    }

    public void testXmlAttachmentWithoutXmlDecl() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<START><A>Hello</A><B>World</B></START>";
        StringReader reader = new StringReader(xml);
        StreamSource content = new StreamSource(reader);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        handler.writeTo(baos);
        String reconstructedXml = baos.toString();
        assertEquals(
            "Attachment content is preserved", xml, reconstructedXml);
    }

    public void testXmlAttachmentReWriteWithReader() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>    <START><A>Hello</A><B>World</B></START>";
        StringReader reader = new StringReader(xml);
        StreamSource content = new StreamSource(reader);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        handler.writeTo(baos1);
        handler.writeTo(baos2);
        String reconstructedXml1 = baos1.toString("utf-8");
        String reconstructedXml2 = baos2.toString("utf-8");
        assertEquals(
            "writeTo for the attachment should succeed the second time too!",
            reconstructedXml1,
            reconstructedXml2);
    }

    public void testXmlAttachmentReWriteWithIS() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>    <START><A>Hello</A><B>World</B></START>";
        ByteArrayInputStream bais =
            new ByteArrayInputStream(xml.getBytes("utf-8"));
        StreamSource content = new StreamSource(bais);
        AttachmentPart attachment =
            msg.createAttachmentPart(content, "text/xml");

        DataHandler handler = attachment.getDataHandler();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        handler.writeTo(baos1);
        handler.writeTo(baos2);
        String reconstructedXml1 = baos1.toString("utf-8");
        String reconstructedXml2 = baos2.toString("utf-8");
        assertEquals(
            "writeTo for the attachment should succeed the second time too!",
            reconstructedXml1,
            reconstructedXml2);
    }
}
