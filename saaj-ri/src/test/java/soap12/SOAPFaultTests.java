/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap12;

import javax.xml.soap.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import java.io.*;

import java.util.Locale;
import java.util.Iterator;

import junit.framework.TestCase;


public class SOAPFaultTests extends TestCase {

    public SOAPFaultTests(String name) throws Exception {
        super(name);
    }

    public void testIncomingFault() throws Exception {

        MessageFactory mFactory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContent(
            new StreamSource(
                new FileInputStream("src/test/resources/soap12/data/fault.xml")));
        SOAPBody body = msg.getSOAPBody();
        assertTrue(body.hasFault());
        SOAPFault fault = body.getFault();

        // Check fault code
        QName faultcode = fault.getFaultCodeAsQName();
        assertEquals(faultcode, SOAPConstants.SOAP_SENDER_FAULT);

        // Check sub-code
        Iterator subcodes = fault.getFaultSubcodes();
        assertTrue("There's a subcode", subcodes.hasNext());
        assertEquals(
            (QName) subcodes.next(),
            new QName("http://www.w3.org/2003/05/soap-rpc", "BadArguments"));
        assertFalse("There's exactly one subcode", subcodes.hasNext());

        try {
            subcodes.remove();
            fail();
        } catch (Exception e) {
            //System.out.println("Subcodes.remove() " + e.getMessage());
        }
        // Check for Reason Texts
        String englishText = fault.getFaultReasonText(Locale.US);
        assertNotNull(englishText);
        assertEquals("The reason text is as expected", englishText, "Processing error");
        String czechText = fault.getFaultReasonText(Locale.forLanguageTag("cs"));
        assertNotNull(czechText);
        assertEquals("The reason text is as expected", czechText, "Chyba zpracování");
        Iterator locales = fault.getFaultReasonLocales();
        Iterator texts = fault.getFaultReasonTexts();
        assertTrue(locales.hasNext() && texts.hasNext());
        assertEquals(
            fault.getFaultReasonText((Locale) locales.next()),
            texts.next());
        assertTrue(locales.hasNext() && texts.hasNext());
        assertEquals(
            fault.getFaultReasonText((Locale) locales.next()),
            texts.next());
        assertFalse(locales.hasNext() || texts.hasNext());

        // Check for Detail
        assertTrue("Detail is present", fault.hasDetail());
        Detail detail = fault.getDetail();
        Iterator detailEntries = detail.getDetailEntries();
        assertTrue("There's a detail entry", detailEntries.hasNext());
        assertEquals(
            ((DetailEntry) detailEntries.next()).getElementQName(),
            new QName("http://travelcompany.example.org/faults", "myFaultDetails"));

        assertNull(fault.getFaultRole());
        assertNull(fault.getFaultNode());
    }

    public void testSetFaultReasonText() throws Exception {

        MessageFactory mFactory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFault fault = body.addFault();
        fault.setFaultCode(SOAPConstants.SOAP_RECEIVER_FAULT);
        fault.addFaultReasonText("Some Text", Locale.US);

        Iterator faultElements = fault.getChildElements();

        SOAPFaultElement code = (SOAPFaultElement) faultElements.next();
        assertEquals(
            "Code has the right name",
            code.getElementQName(),
            new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Code"));

        SOAPFaultElement reason = (SOAPFaultElement) faultElements.next();
        assertEquals(
            "Reason has the right name",
            reason.getElementQName(),
            new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Reason"));

        assertFalse(
            "There should be only two fault elements",
            faultElements.hasNext());
    }

    public void testAppendFaultSubcode() throws Exception {
        SOAPMessage msg =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFault fault = body.addFault();
        fault.appendFaultSubcode(new QName("another-uri", "name2", "q"));
        fault.appendFaultSubcode(new QName("yet-another-uri", "name3", "r"));

        Iterator faultElements = fault.getChildElements();
        SOAPFaultElement code = (SOAPFaultElement) faultElements.next();

        Iterator eachCodeChild = code.getChildElements();
        eachCodeChild.next();
        SOAPElement subcode = (SOAPElement) eachCodeChild.next();

        Iterator eachSubcodeChild = subcode.getChildElements();
        SOAPElement subcodeValue = (SOAPElement) eachSubcodeChild.next();
        assertEquals(subcodeValue.getValue(), "q:name2");
        SOAPElement subSubcode = (SOAPElement) eachSubcodeChild.next();

        Iterator eachSubSubcodeChild = subSubcode.getChildElements();
        SOAPElement subSubcodeValue = (SOAPElement) eachSubSubcodeChild.next();
        assertEquals(subSubcodeValue.getValue(), "r:name3");
        assertEquals(
            subSubcodeValue.createQName("name3", "r"),
            new QName("yet-another-uri", "name3"));
    }

    public void testCreateDetail() throws Exception {
        MessageFactory factory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPFactory sFactory =
            SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = factory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFault sf = body.addFault();
        Detail detail = sFactory.createDetail();
        detail.addDetailEntry(sFactory.createName("de2", "dePrefix", "deUri"));
        assertTrue(sf.addChildElement(detail) instanceof Detail);
    }

    public void testBug6235490() throws Exception {
        MessageFactory factory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = factory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFault sf = body.addFault();
        sf.addFaultReasonText("String for Locale UK", Locale.UK);
        if (!Locale.getDefault().equals(Locale.UK)) {
            assertNull(sf.getFaultReasonText(Locale.getDefault()));
        }
    }
    
    public void testAppendFaultSubcodeWithPrefixEmpty() throws Exception {
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPFault fault = body.addFault(SOAPConstants.SOAP_SENDER_FAULT, "Some string");
        fault.appendFaultSubcode(new QName("another-uri", "subcode"));
    }

    public void testOrderOfFaultElements() throws Exception {
        MessageFactory factory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = factory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFault sf = body.addFault();
        sf.addDetail();
        sf.setFaultRole("some-role");
        sf.setFaultNode("some-node");
        Iterator it = sf.getChildElements();
        assertTrue(((SOAPFaultElement) it.next()).getLocalName().equals("Code"));
        assertTrue(((SOAPFaultElement) it.next()).getLocalName().equals("Reason"));
        assertTrue(((SOAPFaultElement) it.next()).getLocalName().equals("Node"));
        assertTrue(((SOAPFaultElement) it.next()).getLocalName().equals("Role"));
        assertTrue(it.next() instanceof Detail);
        assertFalse(it.hasNext());
    }

    public void testFaultOnlyChildElementofBody() throws Exception {
        MessageFactory factory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = factory.createMessage();
        SOAPBody body = msg.getSOAPBody();
        body.addChildElement("foo");
        try {
            body.addFault();
        } catch (SOAPException se) {
            return;
        }
        fail("Fault shouldn't have got added when there was already a child element of soap body");
    }
}
