/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap12;

import jakarta.xml.soap.*;
import javax.xml.namespace.QName;

import java.util.Iterator;

import junit.framework.TestCase;


public class SOAPHeaderTests extends TestCase {

    public SOAPHeaderTests(String name) throws Exception {
        super(name);
    }

    public void testAddNotUnderstoodHeaderElement() throws Exception {

        MessageFactory mFactory =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mFactory.createMessage();
        SOAPHeader header = msg.getSOAPHeader();
        QName nameOfHeaderNotUnderstood = new QName("some-uri", "name");
        header.addNotUnderstoodHeaderElement(nameOfHeaderNotUnderstood);
        Iterator<SOAPHeaderElement> eachHeaderElement = header.examineAllHeaderElements();
        assertTrue("There's a header element", eachHeaderElement.hasNext());
        SOAPHeaderElement notUnderstoodElement =
                eachHeaderElement.next();
        assertEquals(
            notUnderstoodElement.getElementQName(),
            new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "NotUnderstood"));
        String qname =
            notUnderstoodElement.getAttributeValue(new QName("qname"));
        String prefix = getPrefixFromExpandedName(qname);
        String localName = getLocalnameFromExpandedName(qname);
        assertEquals(
            nameOfHeaderNotUnderstood,
            notUnderstoodElement.createQName(localName, prefix));
    }

    public void testAddUpgradeHeaderElement() throws Exception {
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage msg = msgFactory.createMessage();
        SOAPHeader header = msg.getSOAPHeader();
        header.addUpgradeHeaderElement(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        Iterator<SOAPHeaderElement> eachHeaderElement = header.examineAllHeaderElements();
        assertTrue("There's a header element", eachHeaderElement.hasNext());
        SOAPHeaderElement upgradeElement =
                eachHeaderElement.next();
        assertEquals(
            upgradeElement.getElementQName(),
            new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Upgrade"));
        Iterator eachSupportedEnvElement =
            upgradeElement.getChildElements(
                new QName(
                    SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE,
                    "SupportedEnvelope"));
        assertTrue(
            "There's a SupportedEnvelope element",
            eachSupportedEnvElement.hasNext());
        SOAPElement supportedEnvElement =
            (SOAPElement) eachSupportedEnvElement.next();
        String qname =
            supportedEnvElement.getAttributeValue(new QName("qname"));
        String prefix = getPrefixFromExpandedName(qname);
        String localName = getLocalnameFromExpandedName(qname);
        assertEquals(localName, "Envelope");
        assertEquals(
            SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE,
            supportedEnvElement.getNamespaceURI(prefix));
    }

    private static String getPrefixFromExpandedName(String expandedName) {
        int index = expandedName.indexOf(':');
        if (index < 0)
            return "";
        else
            return expandedName.substring(0, index);
    }

    private static String getLocalnameFromExpandedName(String expandedName) {
        int index = expandedName.indexOf(':');
        if (index < 0)
            return expandedName;
        else
            return expandedName.substring(index + 1);
    }
}
