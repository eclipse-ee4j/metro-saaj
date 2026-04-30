/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package stax;

import com.sun.xml.messaging.saaj.soap.impl.ElementImpl;
import com.sun.xml.messaging.saaj.util.stax.SaajStaxWriter;
import junit.framework.TestCase;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Tests {@link SaajStaxWriter} with respect to namespace assignments.
 *
 * <p>
 * This test class was created to check a fix of JDK-8159058.
 * </p>
 */
public class SaajStaxWriterNamespaceTest extends TestCase {

    private static final String ENV_URI = "http://schemas.xmlsoap.org/soap/envelope";
    private static final String WRAP_URI = "http://test.org/wrapper";
    private static final String PREFIX =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<SOAP-ENV:Header/>" +
            "<SOAP-ENV:Body>" +
            "<wrapper xmlns=\"http://test.org/wrapper\">";
    private static final String SUFFIX = "</wrapper></SOAP-ENV:Body></SOAP-ENV:Envelope>";

    private static final String EXAMPLE_URI_1 = "http://example.org/schema1";
    private static final String EXAMPLE_URI_2 = "http://example.org/schema2";
    private SOAPMessage message;
    private SaajStaxWriter writer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        message = MessageFactory.newInstance().createMessage();
        writer = new SaajStaxWriter(message, ENV_URI);
    }

    @Override
    protected  void tearDown() throws Exception {
        super.tearDown();
        this.message = null;
        this.writer = null;
    }

    private void openLayout() throws XMLStreamException {
        writer.writeStartDocument();
        writer.writeStartElement(ENV_URI, "Envelope");
        writer.writeStartElement(ENV_URI, "Body");
        writer.writeEmptyElement(WRAP_URI, "wrapper");
    }

    private void closeLayout() throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    private void closeWriter() throws XMLStreamException {
        writer.flush();
        writer.close();
    }

    public void testDifferentDefaultNamespacesManual() throws Exception {
        openLayout();

        writer.writeStartElement("container");
        writer.writeAttribute("valid", "true"); // intentionally out of order but shall be handled correctly
        writer.writeNamespace("", EXAMPLE_URI_1);
        //writer.setPrefix("", EXAMPLE_URI_1);

        writer.writeStartElement("content");
        writer.writeNamespace("", EXAMPLE_URI_2);

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_2, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\" valid=\"true\">"
                        + "<content xmlns=\"http://example.org/schema2\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testDifferentDefaultNamespacesAuto() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1, "container");
        writer.writeStartElement(EXAMPLE_URI_2, "content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_2, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"http://example.org/schema2\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testGlobalNamespaceInDefaultNamespacesManual() throws Exception {
        openLayout();

        writer.writeStartElement("container");
        writer.writeNamespace("", EXAMPLE_URI_1);
        writer.setPrefix("", EXAMPLE_URI_1);

        writer.writeStartElement("content");
        writer.writeNamespace("", "");
        writer.setPrefix("", "");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertNull(content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testGlobalNamespaceInDefaultNamespacesAuto() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1,"container");
        writer.writeStartElement("","content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertNull(content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testGlobalNamespaceInDefaultNamespacesMixed() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1,"container");

        writer.writeStartElement("content");
        writer.writeNamespace("", "");
        writer.setPrefix("", "");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertNull(content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content xmlns=\"\"/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }


    public void testInheritNamespaceInDefaultNamespacesManual() throws Exception {
        openLayout();

        writer.writeStartElement("container");
        writer.writeNamespace("", EXAMPLE_URI_1);
        writer.setPrefix("", EXAMPLE_URI_1);

        writer.writeStartElement("content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        ElementImpl element = (ElementImpl) getWrapper();
        SOAPElement container = (SOAPElement)element.getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_1, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    public void testInheritNamespaceInDefaultNamespacesAuto() throws Exception {
        openLayout();

        writer.writeStartElement(EXAMPLE_URI_1,"container");
        writer.writeStartElement("content");

        writer.writeEndElement();

        writer.writeEndElement();

        closeLayout();
        closeWriter();

        SOAPElement container = (SOAPElement)getWrapper().getFirstChild();
        assertEquals(EXAMPLE_URI_1, container.getNamespaceURI());
        SOAPElement content = (SOAPElement) container.getFirstChild();
        assertEquals(EXAMPLE_URI_1, content.getNamespaceURI());

        assertEquals("<container xmlns=\"http://example.org/schema1\">"
                        + "<content/>"
                        + "</container>",
                getSoapMessageContentsAsString());
    }

    private String getSoapMessageContentsAsString() throws IOException, SOAPException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        String txtMessage = os.toString(StandardCharsets.UTF_8);
        if (txtMessage.startsWith(PREFIX)) {
            txtMessage = txtMessage.substring(PREFIX.length());
        } else {
            fail("SOAP message does not start with " + PREFIX);
        }
        if (txtMessage.endsWith(SUFFIX)) {
            txtMessage = txtMessage.substring(0, txtMessage.length() - SUFFIX.length());
        } else {
            fail("SOAP message does not end with " + SUFFIX);
        }
        return txtMessage;
    }

    private SOAPElement getWrapper() throws SOAPException {
        return (SOAPElement) message.getSOAPBody().getFirstChild();
    }
}
