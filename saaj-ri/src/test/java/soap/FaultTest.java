/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import jakarta.xml.soap.*;
import javax.xml.transform.stream.*;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

public class FaultTest extends TestCase {

    public FaultTest(String name) {
        super(name);
    }

    public void testGetDetail() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPFault fault = body.addFault();
        Detail detail = fault.addDetail();
        String detailEntryLocalName = "name";
        Name detailEntryName = envelope.createName(detailEntryLocalName, "prefix", "uri");
        detail.addDetailEntry(detailEntryName);
        
        SOAPFault extractedFault = body.getFault();
        assertTrue(extractedFault != null);
        Detail extractedDetail = extractedFault.getDetail();
        assertTrue(extractedDetail != null);
        Iterator<DetailEntry> eachDetailEntry = extractedDetail.getDetailEntries();
        assertTrue(eachDetailEntry.hasNext());
        DetailEntry extractedEntry = eachDetailEntry.next();
        assertEquals(detailEntryLocalName, extractedEntry.getLocalName());
        assertFalse(eachDetailEntry.hasNext());

        extractedFault.setFaultActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
        Detail d = extractedFault.getDetail();
        d.detachNode();
        d = extractedFault.getDetail();
        assertTrue(d == null);
    }

    public void testFaultWithAmp() throws Exception {
        String testDoc = 
          "<?xml version='1.0' encoding='UTF-8'?>"
        + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
        +     "<soap:Body>"
        +         "<soap:Fault>"
        +             "<soap:faultcode>"
        +                 "code&gt;code"
        +             "</soap:faultcode>"
        +             "<soap:faultstring>"
        +                 "string&gt;string"
        +             "</soap:faultstring>"
        +             "<soap:faultactor>"
        +                 "actor&gt;actor"
        +             "</soap:faultactor>"
        +             "<soap:detail />"
        +         "</soap:Fault>"
        +     "</soap:Body>"
        + "</soap:Envelope>";

        byte[] testDocBytes = testDoc.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);

        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();

        soapPart.setContent(strSource);
        message.saveChanges();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPFault fault = body.getFault();
        assertTrue(fault.getFaultCode().equals("code>code"));
        assertTrue(fault.getFaultString().equals("string>string"));
        assertTrue(fault.getFaultActor().equals("actor>actor"));
    }

    /**
     * Test case for CR ID 6212709
     */
    public void testSetFaultCodeWithNameArg() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPFault fault = body.addFault();
        Name faultCodeName = SOAPFactory.newInstance().createName("Client", "env", null);
        try {
            fault.setFaultCode(faultCodeName);
        } catch (SOAPException se) {
            return;
        }
        fail("Invalid fault code allowed to be set");
    }
    
    public void testSetFaultCodeWithPrefixEmpty() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        body.addFault(new QName("some-uri", "code"), "Some string");
    }

    public void testSetFaultStringAndLocale() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        body.addFault(new QName("some-uri", "code"), "Some string");
        SOAPFault fault = body.getFault();
        fault.setFaultString("EN faultString" , java.util.Locale.ENGLISH);
        //System.out.println(fault.getFaultString() + " : " + fault.getFaultStringLocale());
        fault.setFaultString("No Locale faultString");
        //System.out.println(fault.getFaultString() + " : " + fault.getFaultStringLocale());
        assertTrue(fault.getFaultStringLocale() == null);
    }
}
