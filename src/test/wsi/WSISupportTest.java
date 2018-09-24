/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * 
 */

package wsi;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.xml.soap.*;
import javax.xml.transform.dom.DOMSource;

import junit.framework.TestCase;

/*
 * Unit test cases to test out the ws-i support.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */

public class WSISupportTest extends TestCase {

    public WSISupportTest(String name) {
        super(name);
    }

    private SOAPMessage createMessage() throws SOAPException {
        MessageFactory msgFactory = MessageFactory.newInstance();

        SOAPMessage msg = msgFactory.createMessage();

        return msg;
    }

    /*
     * Test to verify set char encoding.
     */
    public void testCharEncoding() {

        try {

            SOAPMessage message = createMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader hdr = envelope.getHeader();

            // create a header element
            SOAPHeaderElement transaction =
                hdr.addHeaderElement(
                    envelope.createName("Transaction", "t", "some-uri"));

            transaction.addTextNode("5");

            message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            // set char encoding to utf-16
            message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
            //message.writeTo(System.out);

            //System.out.println("\n\nContent type = " + message.getContentType());
        } catch (Exception e) {
            // e.printStackTrace();
            fail("No exception should be thrown: " + e);
        }
    }

    public void xtestContentType() {

        try {
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage();
            SOAPPart part = message.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader hdr = envelope.getHeader();
            SOAPBody body = envelope.getBody();
            SOAPFault fault = body.addFault();

            part.setMimeHeader("Content-Type", "text/html");
            fault.setFaultCode("100");
            fault.setFaultString("some reason for fault");

            fault.addChildElement("test");
            //message.writeTo(System.out);

            // System.out.println("\n\nContent type = " + message.getContentType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFaultDetail() throws Exception {

        String RETAIL_ORDER_NAMESPACE = "http://www.Something.com";

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart part = message.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        SOAPFault fault = body.addFault();
        //Detail detail = fault.addDetail();

        SOAPFactory soapFactory = SOAPFactory.newInstance();
        Detail detail = soapFactory.createDetail();
        Name name =
            soapFactory.createName(
                "InvalidProductCode",
                "ns1",
                RETAIL_ORDER_NAMESPACE);
        DetailEntry detailEntry = detail.addDetailEntry(name);
        SOAPElement soapElement =
            detailEntry.addChildElement(
                "Reason",
                "ns1",
                RETAIL_ORDER_NAMESPACE);
        soapElement.addTextNode("InvalidProductCode");
        soapElement =
            detailEntry.addChildElement(
                "ProductNumber",
                "ns1",
                RETAIL_ORDER_NAMESPACE);
        soapElement.addTextNode("600510");

        DOMSource source = new DOMSource(detail);

        fault.addChildElement(detail);

        String expected =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/"
                + "soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><SOAP-ENV:"
                + "Fault><faultcode>SOAP-ENV:Server</faultcode><faultstring>"
                + "Fault string, and possibly fault code, not set</faultstring>"
                + "<detail><ns1:InvalidProductCode xmlns:ns1=\"http://www."
                + "Something.com\"><ns1:Reason>InvalidProductCode</ns1:Reason><ns1:"
                + "ProductNumber>600510</ns1:ProductNumber></ns1:InvalidProductCode>"
                + "</detail></SOAP-ENV:Fault></SOAP-ENV:Body></SOAP-ENV:Envelope>";

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        message.writeTo(bytesOut);
        //message.writeTo(System.out);

        assertEquals(expected, bytesOut.toString());

    }

    public void testFaultLocale() throws Exception {

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart part = message.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        // add fault here
        SOAPFault fault = body.addFault();
        // Setting fault string to locale en_US
        fault.setFaultString("something", Locale.US);

        assertTrue(
            "Locale should be english",
            Locale.US.equals(fault.getFaultStringLocale()));

    }

    public static void main(String argv[]) {

        junit.textui.TestRunner.run(WSISupportTest.class);

    }

}
