/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package namespace;

import jakarta.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import java.io.*;

import junit.framework.TestCase;


public class DefaultNamespaceTest extends TestCase {

    /*
     * Testcase for Bug Id 5034339 (Synopsis - Non-namespace-qualified
     * element is assigned a namespace declaration)
     */
    public void testBugId5034339() throws Exception {

        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPBody body = msg.getSOAPBody();
        Name name = SOAPFactory.newInstance().createName("Content", "", "some-uri");
        body.addBodyElement(name).addChildElement("Value").addTextNode("SUNW");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        StreamSource source = new StreamSource(bais);

        SOAPMessage reconstructedMsg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = reconstructedMsg.getSOAPPart();
        soapPart.setContent(source);
        SOAPEnvelope env = soapPart.getEnvelope();
        SOAPElement content = (SOAPElement) env.getBody().getFirstChild();
        //System.out.println("content.getNamespaceURI() = **" + content.getNamespaceURI() + "**");
        SOAPElement value = (SOAPElement) content.getFirstChild();
        //System.out.println("value.getNamespaceURI() = **" + value.getNamespaceURI() + "**");
        assertNotNull("The namespace of Value element shouldn't have been null", value.getNamespaceURI());
    }

    /*
     * Testcase for Bug Id 6206247 (Synopsis - 3 argument addChildElement
     * doesn't create a ns element with "" uri)
     */
    public void test3ArgAddChildElement() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPBody body = msg.getSOAPBody();
        Name name = SOAPFactory.newInstance().createName("Content", "", "some-uri");
        SOAPElement element =
            body.addBodyElement(name).addChildElement("lname", null, null);
        assertNull(element.getNamespaceURI());
    }

    /*
     * Testcase for Bug Id 6236737
     * Synopsis: SOAPElement.addChildElement("localname"); is adding
     * wrong namespace for the element.
     */
    public void testBug6236737() throws Exception {
        String xml =
            "<SOAP-ENV:Envelope xmlns=\"abc\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body/></SOAP-ENV:Envelope>";
        MessageFactory mFactory = MessageFactory.newInstance();
        SOAPMessage msg = mFactory.createMessage();
        msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        SOAPBody sb = msg.getSOAPPart().getEnvelope().getBody();
        SOAPElement ele =
            sb.addChildElement("lname1","","uri").addChildElement("lname3");
        assertEquals(
            "Element 'ele' lies in namespace 'uri' and not 'abc'",
            ele.getNamespaceURI(),
            "uri");
    }
}
