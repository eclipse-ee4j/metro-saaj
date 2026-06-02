/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package soap;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.BodyImpl;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPMessage;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Count of levels is limited in JDK24+, but SAAJ should be able to handle more levels of
 * nesting than the default limit of 100.
 */
public class DeeplyNestedXmlTest {

    /**
     * Count of levels is limited in JDK24+, but SAAJ should be able to handle more levels of
     * nesting than the default limit of 100.
     */
    @Test
    public void testManyWrappedElements() throws Exception {
        MessageFactory fact = MessageFactory.newInstance();
        MimeHeaders mh = new MimeHeaders();
        mh.addHeader("Content-Type", "text/xml");
        SOAPMessage msg = fact.createMessage(mh,
                new FileInputStream(new File("src/test/resources/soap/deeplyNested.xml")));
        BodyImpl bodyImpl = (BodyImpl) msg.getSOAPBody();
        SOAPDocumentImpl soapDocument = bodyImpl.getSoapDocument();
        NodeList startPaosNodes = bodyImpl.getElementsByTagName("iso:StartPAOS");
        SOAPElement startPaosFirstChild = (SOAPElement) startPaosNodes.item(0);
        Attr isoAttr = startPaosFirstChild.getAttributeNode("xmlns:iso");
        assertEquals("iso", isoAttr.getLocalName());
        assertEquals("xmlns", isoAttr.getPrefix());
        assertEquals("http://www.w3.org/2000/xmlns/", isoAttr.getNamespaceURI());

        NodeList sessionIdNodes = startPaosFirstChild.getElementsByTagName("iso:SessionIdentifier");
        SOAPElement startNode = (SOAPElement) soapDocument.find(sessionIdNodes.item(0));
        int wrapIndex = 0;
        SOAPElement currentElement  = startNode;
        while (currentElement != null) {
            String nextChildElementName = "n" + wrapIndex++;
            NodeList subNodes = currentElement.getElementsByTagName(nextChildElementName);
            currentElement = (SOAPElement) subNodes.item(0);
            if (wrapIndex < 10_001) {
                assertNotNull("Wrap index: " + wrapIndex, currentElement);
            }
        }
        assertEquals(10_001, wrapIndex);
    }

}
