/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
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

package soap;

import java.util.Iterator;

import jakarta.xml.soap.*;

import junit.framework.TestCase;

public class SOAPStructureTest extends TestCase {
    public SOAPStructureTest(String name) {
        super(name);
    }

    public void testAddHeaderElement() throws Exception {
        SOAPMessage message = MessageFactory.newInstance().createMessage();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        
        Name name1 = envelope.createName("firstElement", null, "foo");
        Name name2 = envelope.createName("secondElement", null, "foo");
        
        SOAPHeaderElement element1 = header.addHeaderElement(name1);
        element1.setActor("theActor");

        SOAPHeaderElement element2 = header.addHeaderElement(name2);
        element2.setActor("theActor");
        
        Iterator eachElement = header.extractHeaderElements("theActor");
        
        assertTrue("First element is there", eachElement.hasNext());
        SOAPHeaderElement outElement1 = (SOAPHeaderElement) eachElement.next();
        assertTrue("Second element is there", eachElement.hasNext());
        SOAPHeaderElement outElement2 = (SOAPHeaderElement) eachElement.next();
        assertFalse("No more elements", eachElement.hasNext());

        assertEquals("First element is correct", element1, outElement1);
        assertEquals("Second element is correct", element2, outElement2);
    }

    public void testAddHeader() throws Exception {
        SOAPMessage message = MessageFactory.newInstance().createMessage();
        message.getSOAPHeader().detachNode();
        
        SOAPHeader header = message.getSOAPHeader();
        assertTrue(header == null);
        
        message.getSOAPPart().getEnvelope().addHeader();
        header = message.getSOAPHeader();
        assertTrue(header != null);
        
        String headerPrefix = header.getPrefix();
        String headerUri = header.getNamespaceURI(headerPrefix);
        assertTrue(SOAPConstants.URI_NS_SOAP_ENVELOPE.equals(headerUri));
    }
}
