/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package namespace;

import java.util.Iterator;

import jakarta.xml.soap.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.w3c.dom.Document;

//import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;

import java.io.*;
import java.lang.reflect.Method;

/*
 * Tests to check for namespace rules being followed in SOAP message creation.
 */

public class NamespaceTest extends TestCase {

    private SOAPEnvelope envelope;
    private SOAPFactory sFactory;
    private MessageFactory msgFactory;

    public NamespaceTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() {

        try {
            msgFactory = MessageFactory.newInstance();
            sFactory = SOAPFactory.newInstance();
            SOAPMessage msg = msgFactory.createMessage();
            envelope = msg.getSOAPPart().getEnvelope();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SOAPMessage createMessage(Name name, String attrib)
        throws SOAPException {
        /*
         * Creating a message.
         */
        SOAPMessage msg = msgFactory.createMessage();

        envelope = msg.getSOAPPart().getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        SOAPElement elem = sFactory.createElement(name);

        if (attrib != null)
            elem.addAttribute(name, attrib);

        SOAPHeaderElement she =
            hdr.addHeaderElement(
                envelope.createName(
                    "HeaderElement1",
                    "he1",
                    "http://foo.xyz.com"));
        she.addChildElement(elem);

        return msg;
    }

    /*
     * xmlns prefix is reserved and cannot be declared
     */
    public void testXmlnsAsPrefix() {

        String exception = null;
        try {

            Name name1 =
                envelope.createName(
                    "foo",
                    "xmlns",
                    "http://www.w3.org/2000/xmlns/");

            SOAPMessage msg = createMessage(name1, "myNameSpaceURI");

            //msg.writeTo(System.out);

        } catch (Exception e) {
            exception = e.getMessage();
        }

        // exception should be thrown if someone tried to declare a namespace prefix with the
        // reserved 'xmlns' word.
        assertTrue("An exception should have been thrown", (exception != null));
    }

    /*
     * Qualified name cannot be xmlns.
     */
    public void testQNameAsPrefix() {

        String exception = null;
        try {

            Name name2 =
                envelope.createName(
                    "xmlns",
                    null,
                    "http://www.w3.org/2000/xmlns/");

            SOAPMessage msg = createMessage(name2, "");

            //msg.writeTo(System.out);

        } catch (Exception e) {
            exception = e.getMessage();
        }

        assertTrue("An exception should have been thrown", (exception != null));
    }

    /*
     * URI cannot be null.
     */
    public void testNullUriInName() {

        String exception = null;
        try {

            Name name3 = envelope.createName("test", "prefix", null);

            SOAPMessage msg = createMessage(name3, null);

            //msg.writeTo(System.out);

        } catch (Exception e) {
            exception = e.getMessage();
        }

        assertTrue("An exception should have been thrown", (exception != null));
    }

    /*
     * Test to verify SOAPFactory.createElement(localname);
     */
    public void testCreateElementString() {

        try {
            SOAPMessage msg = msgFactory.createMessage();
            envelope = msg.getSOAPPart().getEnvelope();

            SOAPHeader hdr = envelope.getHeader();
            SOAPBody bdy = envelope.getBody();

            SOAPElement elem = sFactory.createElement("localname");
            bdy.addChildElement(elem);

        } catch (Exception e) {
            // e.printStackTrace();
            fail("No exception should be thrown" + e.getMessage());
        }
    }

    public void testGetNamespacePrefixes() throws Exception {
        SOAPMessage message = msgFactory.createMessage();
        envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        body.addNamespaceDeclaration("prefix", "http://aUri");

        Iterator<String> eachPrefix = body.getNamespacePrefixes();

        String prefix;

        assertTrue(eachPrefix.hasNext());
        prefix = eachPrefix.next();
        assertTrue(
            "wrong first prefix: \"" + prefix + "\"",
            "prefix".equalsIgnoreCase(prefix));
        //        assertTrue(eachPrefix.hasNext());
        //        prefix = (String) eachPrefix.next();
        //        assertTrue("wrong second prefix: \""+ prefix+"\"", "SOAP-ENV".equalsIgnoreCase(prefix));

        if (eachPrefix.hasNext()) {
            String errorString = "";
            int count = 0;
            while (eachPrefix.hasNext() && count < 10) {
                prefix = eachPrefix.next();
                errorString =
                    errorString + "Unexpected prefix: " + prefix + "\n";
            }
            if (count == 10) {
                errorString = errorString + "more...";
            }

            fail(errorString);
        }

        eachPrefix = body.getNamespacePrefixes();
        eachPrefix.next();
        eachPrefix.remove();

        //        eachPrefix = body.getNamespacePrefixes();
        //        assertTrue(eachPrefix.hasNext());
        //        prefix = (String) eachPrefix.next();
        //        assertTrue("wrong prefix found after removal: \""+ prefix+"\"", "SOAP-ENV".equalsIgnoreCase(prefix));
        assertTrue(!eachPrefix.hasNext());
    }

    public void testBodyPrefix() throws Exception {
        // Create Envelope element
        SOAPElement env =
            SOAPFactory.newInstance().createElement(
                "Envelope",
                "env",
                "http://schemas.xmlsoap.org/soap/envelope/");
        env.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        env.addNamespaceDeclaration(
            "xsi",
            "http://www.w3.org/2001/XMLSchema-instance");
        env.addNamespaceDeclaration(
            "enc",
            "http://schemas.xmlsoap.org/soap/encoding/");

        // Insert Envelope element
        SOAPMessage soapMsg = msgFactory.createMessage();
        Document dom = soapMsg.getSOAPPart();
        SOAPElement elem = (SOAPElement) dom.importNode(env, true);
        dom.insertBefore(elem, null);

        //Insert Body element
        elem.addChildElement(
            "Body",
            "env",
            "http://schemas.xmlsoap.org/soap/envelope/");
        soapMsg.saveChanges();

        // Is namespace of Body "env" ?
        SOAPBody body = soapMsg.getSOAPBody();
        assertTrue(body.getPrefix().equals("env"));
    }

    public void testAttrPrefix() throws Exception {
        String msgStr =
            "<env:Envelope "
                + "xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" "
                + "xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "xmlns:ns0=\"http://echoservice.org/types4\" "
                + "xmlns:ns1=\"http://java.sun.com/jax-rpc-ri/internal\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<SOAP-ENV:Body "
                + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<ans1:echoMapEntryResponse "
                + "env:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" "
                + "xmlns:ans1=\"http://echoservice.org/wsdl\">"
                + "<result xsi:nil=\"1\" xsi:type=\"ns1:mapEntry\"/>"
                + "</ans1:echoMapEntryResponse>"
                + "</SOAP-ENV:Body>"
                + "</env:Envelope>";

        SOAPMessage soapMsg = msgFactory.createMessage();
        soapMsg.getSOAPPart().setContent(
            new StreamSource(new java.io.StringReader(msgStr)));
        soapMsg.saveChanges();
        SOAPBody body = soapMsg.getSOAPBody();
        Iterator i = body.getChildElements();
        SOAPElement elem = null;
        if (i.hasNext()) {
            elem = (SOAPElement) i.next(); // elem=ans1:echoMapEntryResponse
        }
        if (elem != null) {
            i = elem.getChildElements();
            elem = null;
            if (i.hasNext()) {
                elem = (SOAPElement) i.next(); // elem=result
                String got = elem.getNamespaceURI("ns1");
                String expected = "http://java.sun.com/jax-rpc-ri/internal";
                assertTrue(got.equals(expected));
            }
        }
    }

    public void testNamespaceDeclarationAsAttribute() throws Exception {
        SOAPElement element = SOAPFactory.newInstance().createElement(
                "Envelope",
                "env",
                "http://schemas.xmlsoap.org/soap/envelope/");
        element.addAttribute(SOAPFactory.newInstance().createName("fooName", "xmlns", ""), "http://foo");
        
        Iterator<String> eachDeclaration = element.getNamespacePrefixes();
        assertTrue(eachDeclaration.hasNext());
        assertEquals("fooName", eachDeclaration.next());
        if (eachDeclaration.hasNext()) {
            String extraPrefix = eachDeclaration.next();
            fail("An extra namespace declaration was added for: " + extraPrefix);
        }
    }
    
    public void testLookupNamespaceURIDOML3() throws Exception {
        String msg = "<?xml version='1.0' ?><S:Envelope xmlns:S='http://schemas.xmlsoap.org/soap/envelope/'><S:Body><ns2:Fault xmlns:ns2='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ns3='http://www.w3.org/2003/05/soap-envelope'><faultcode>ns3:VersionMismatch</faultcode><faultstring>Couldn't create SOAP message. Expecting Envelope in namespace http://schemas.xmlsoap.org/soap/envelope/, but got http://wrongname.org </faultstring></ns2:Fault></S:Body></S:Envelope>";
        
        
        MessageFactory messageFactory = MessageFactory.newInstance();
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        
        SOAPMessage soapMsg = messageFactory.createMessage(headers,new ByteArrayInputStream(msg.getBytes()));
        soapMsg.writeTo(System.out);
        SOAPBody body = soapMsg.getSOAPPart().getEnvelope().getBody();
        
        SOAPFault fault = body.getFault();
        String uri = fault.lookupNamespaceURI("ns3");
        assertTrue(uri.equals("http://www.w3.org/2003/05/soap-envelope"));
         
    }

    /*
     * Test of bug related to JDK-8159058.
     * Adding element with explicitly empty namespace URI shall put the element in global namespace.
     */
    public void testAddChildElementToGlobalNamespace() throws Exception {
        SOAPMessage msg = createSoapMessageWithBody();
        SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();

        String namespace = "http://example.org/test";
        SOAPElement parentInExplicitDefaultNamespace = body.addChildElement("content", "", namespace);
        parentInExplicitDefaultNamespace.addNamespaceDeclaration("", namespace);
        SOAPElement childInGlobalNamespace = parentInExplicitDefaultNamespace.addChildElement("global-child", "", "");
        childInGlobalNamespace.addNamespaceDeclaration("", "");
        SOAPElement grandChildInGlobalNamespace = childInGlobalNamespace.addChildElement("global-grand-child");
        SOAPElement childInDefaultNamespace = parentInExplicitDefaultNamespace.addChildElement("default-child");

        assertNull(childInGlobalNamespace.getNamespaceURI());
        assertNull(grandChildInGlobalNamespace.getNamespaceURI());
        assertEquals(namespace, childInDefaultNamespace.getNamespaceURI());
    }

    /*
     * Test of bug related to JDK-8159058.
     * Adding element with explicitly null namespace URI shall put the element in global namespace.
     */
    public void testAddChildElementToNullNamespace() throws Exception {
        SOAPMessage msg = createSoapMessageWithBody();
        SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();

        String namespace = "http://example.org/test";
        SOAPElement parentInExplicitDefaultNamespace = body.addChildElement("content", "", namespace);
        parentInExplicitDefaultNamespace.addNamespaceDeclaration("", namespace);
        SOAPElement childInGlobalNamespace = parentInExplicitDefaultNamespace.addChildElement("global-child", "", null);
        childInGlobalNamespace.addNamespaceDeclaration("", null);
        SOAPElement grandChildInGlobalNamespace = childInGlobalNamespace.addChildElement("global-grand-child");
        SOAPElement childInDefaultNamespace = parentInExplicitDefaultNamespace.addChildElement("default-child");

        assertNull(childInGlobalNamespace.getNamespaceURI());
        assertNull(grandChildInGlobalNamespace.getNamespaceURI());
        assertEquals(namespace, childInDefaultNamespace.getNamespaceURI());
    }

    /*
     * Test of bug related to JDK-8159058.
     * Adding element with explicitly empty namespace URI shall put the element in global namespace.
     */
    public void testAddChildElementToGlobalNamespaceQName() throws Exception {
        SOAPMessage msg = createSoapMessageWithBody();
        SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();

        String namespace = "http://example.org/test";
        SOAPElement parentInExplicitDefaultNamespace = body.addChildElement("content", "", namespace);
        parentInExplicitDefaultNamespace.addNamespaceDeclaration("", namespace);
        SOAPElement childInGlobalNamespace = parentInExplicitDefaultNamespace.addChildElement(new QName("", "global-child"));
        childInGlobalNamespace.addNamespaceDeclaration("", "");
        SOAPElement grandChildInGlobalNamespace = childInGlobalNamespace.addChildElement("global-grand-child");
        SOAPElement childInDefaultNamespace = parentInExplicitDefaultNamespace.addChildElement("default-child");

        assertNull(childInGlobalNamespace.getNamespaceURI());
        assertNull(grandChildInGlobalNamespace.getNamespaceURI());
        assertEquals(namespace, childInDefaultNamespace.getNamespaceURI());
    }

    /*
     * Test of bug related to JDK-8159058.
     * Adding element with explicitly empty namespace URI shall put the element in global namespace.
     */
    public void testAddChildElementToNullNamespaceQName() throws Exception {
        SOAPMessage msg = createSoapMessageWithBody();
        SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();

        String namespace = "http://example.org/test";
        SOAPElement parentInExplicitDefaultNamespace = body.addChildElement("content", "", namespace);
        parentInExplicitDefaultNamespace.addNamespaceDeclaration("", namespace);
        SOAPElement childInGlobalNamespace = parentInExplicitDefaultNamespace.addChildElement(new QName(null, "global-child"));
        childInGlobalNamespace.addNamespaceDeclaration("", "");
        SOAPElement grandChildInGlobalNamespace = childInGlobalNamespace.addChildElement("global-grand-child");
        SOAPElement childInDefaultNamespace = parentInExplicitDefaultNamespace.addChildElement("default-child");

        assertNull(childInGlobalNamespace.getNamespaceURI());
        assertNull(grandChildInGlobalNamespace.getNamespaceURI());
        assertEquals(namespace, childInDefaultNamespace.getNamespaceURI());
    }

    /*
     * Test of bug related to JDK-8159058.
     * Adding element with explicitly empty namespace URI shall put the element in global namespace.
     * This version of test does not explicitly add namespace declarations.
     */
    public void testAddChildElementToGlobalNamespaceNoDeclarations() throws Exception {
        SOAPMessage msg = createSoapMessageWithBody();
        SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();

        String namespace = "http://example.org/test";
        SOAPElement parentInExplicitDefaultNamespace = body.addChildElement("content", "", namespace);
        SOAPElement childInGlobalNamespace = parentInExplicitDefaultNamespace.addChildElement("global-child", "", "");
        SOAPElement childInDefaultNamespace = parentInExplicitDefaultNamespace.addChildElement("default-child");

        assertNull(childInGlobalNamespace.getNamespaceURI());
        assertEquals(namespace, childInDefaultNamespace.getNamespaceURI());
    }

    /*
     * Test of bug related to JDK-8159058.
     * Adding element with explicitly null namespace URI shall put the element in global namespace.
     * This version of test does not explicitly add namespace declarations.
     */
    public void testAddChildElementToNullNamespaceNoDeclarations() throws Exception {
        SOAPMessage msg = createSoapMessageWithBody();
        SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();

        String namespace = "http://example.org/test";
        SOAPElement parentInExplicitDefaultNamespace = body.addChildElement("content", "", namespace);
        SOAPElement childInGlobalNamespace = parentInExplicitDefaultNamespace.addChildElement("global-child", "", null);
        SOAPElement childInDefaultNamespace = parentInExplicitDefaultNamespace.addChildElement("default-child");

        assertNull(childInGlobalNamespace.getNamespaceURI());
        assertEquals(namespace, childInDefaultNamespace.getNamespaceURI());
    }

    public static void main(String argv[]) {

        junit.textui.TestRunner.run(NamespaceTest.class);

    }

    private static SOAPMessage createSoapMessageWithBody() throws SOAPException, UnsupportedEncodingException {
        String xml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body/></SOAP-ENV:Envelope>";
        MessageFactory mFactory = MessageFactory.newInstance();
        SOAPMessage msg = mFactory.createMessage();
        msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        return msg;
    }

    private static Name createFromTagName(String tagName) {
        Class<?> cls = null;
        try {
            cls = Thread.currentThread().getContextClassLoader().
                    loadClass("com.sun.xml.messaging.saaj.soap.name.NameImpl");
        } catch (Exception e) {
            try {
                cls = Thread.currentThread().getContextClassLoader().loadClass("com.sun.xml.internal.messaging.saaj.soap.name.NameImpl");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (cls != null) {
            Method meth = null;
            try {
                meth = cls.getMethod("create", String.class, String.class, String.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                int index = tagName.indexOf(':');
                if (index < 0) {

                    Name nm = (Name) meth.invoke(null, (Object[]) new String[]{tagName, "", ""});
                    return nm;

                } else {
                    Name nm = (Name) meth.invoke(null,(Object[]) new String[]{
                                tagName.substring(index + 1),
                                tagName.substring(0, index),
                                ""});
                    return nm;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
