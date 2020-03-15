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

import junit.framework.TestCase;

import java.io.*;

import java.util.Iterator;

import javax.xml.soap.*;
import javax.xml.transform.stream.*;

public class GetChildElementsTest extends TestCase {

    public GetChildElementsTest(String name) {
        super(name);
    }

    public void testGetChildElements() throws Exception {

        String xml =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header><wsse:Security xmlns='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd' xmlns:wsse='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd'/></SOAP-ENV:Header><SOAP-ENV:Body/></SOAP-ENV:Envelope>";

        byte[] testDocBytes = xml.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();
        soapPart.setContent(strSource);
        message.saveChanges();

        Iterator headerElements =
            message.getSOAPHeader().getChildElements(
                SOAPFactory.newInstance().createName(
                    "Security",
                    "wsse",
                    "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"));

        assertTrue(headerElements.hasNext());
    }
}
