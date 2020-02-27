/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package bugfixes;

import java.io.*;
import java.util.Iterator;

import javax.xml.namespace.QName;
import jakarta.xml.soap.*;

import junit.framework.TestCase;
import org.w3c.dom.*;

/*
 * Trying to reproduce bug id 4761568.
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */ 
public class NamespaceTest extends TestCase {
    private static util.TestHelper th = util.TestHelper.getInstance();

    public NamespaceTest(String name) {
        super(name);
    }

    /* 
     * Picked code snippet from bug description.
     * This seems like  a dom4j bug.
     */
    private static final String MESSAGE =
        "<soap:Envelope\n"+
        "    xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"+
        "  <soap:Body>\n"+
        "    <function xmlns='www.foo.com'>\n"+
        "      <param>first parameter</param>\n"+
        "      <param xmlns=''>second parameter</param>\n"+
        "    </function>\n"+
        "  </soap:Body>\n"+
        "</soap:Envelope>\n";

    /**
     * Parses a SOAP message contained in a String and builds a SOAPMessage
     * instance. The SOAPMessage's MIME-header gets two fields set:<ul>
     *   <li>SOAP-Action:  www.foo.com#function</li>
     *   <li>Content-Type: text/xml</li></ul>
     * @param message SOAP message contained in a string.
     * @return SOAPMessage instance
     */
    private static SOAPMessage createSOAPMessage(String message)
        throws Exception
    {
        final MimeHeaders headers = new MimeHeaders();
        headers.addHeader("SOAP-Action", "www.foo.com#function");
        headers.addHeader("Content-Type", "text/xml");
        MessageFactory factory = MessageFactory.newInstance();
        InputStream istream = new ByteArrayInputStream(message.getBytes("utf-8"));
        BufferedInputStream bistream = new BufferedInputStream(istream);
        return factory.createMessage(headers, bistream);
    }
    
    /**
     * Inserts a text-node into a SOAPMessage's header. This method will do a
     * lot of bizzare and unnecessary things:<ul>
     *   <li>change the soap namespace prefix to "soap-env"</li>
     *   <li>still include the declaration of the "soap" prefix (this is grand
     *       i suppose since the 'soap' prefix could be referenced in text-nodes
     *       such as XPath expressions, etc.</li>
     *   <li>change the namespace of the second param-element</li>
     *   <li>removes line-break after SOAP Body element</li></ul>
     * @param message The SOAPMessage to insert the header text into.
     */
    private static void addSOAPHeader(SOAPMessage message) throws Exception {
        SOAPPart soap = message.getSOAPPart();
        SOAPEnvelope env = soap.getEnvelope();
        SOAPHeader header = env.getHeader();
        if (header == null) header = env.addHeader();
        header.addTextNode("This goes inside the SOAP header");
    }

    public void testBug4761568() throws Exception {
        th.println("Original message:\n"+MESSAGE);
        SOAPMessage message = createSOAPMessage(MESSAGE);
        addSOAPHeader(message);
        SOAPPart soap = message.getSOAPPart();
        SOAPEnvelope env = soap.getEnvelope();
        SOAPBody body = env.getBody();
        Iterator bodyChildren = body.getChildElements();
        bodyChildren.next();
        SOAPElement element = (SOAPElement) bodyChildren.next();
        Iterator elementChildren = element.getChildElements();
        elementChildren.next();
        elementChildren.next();
        elementChildren.next();
        SOAPElement element2 = (SOAPElement) elementChildren.next();
        assertTrue(element2.getFirstChild().getNodeValue()
                                       .equals("second parameter"));
        assertTrue(element2.getAttributeValue(new QName("xmlns")).equals(""));
        
        th.println("Parsed message:");
        th.writeTo(message);
    }

     // Test for Bug ID: 4988335
    public void testNamespaceDeclaration() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        NamedNodeMap attrs = env.getAttributes();
        int attrsLen = attrs.getLength();
        assertEquals("There's a single attribute", attrsLen, 1);
        
        
        Attr curAttr = (Attr) attrs.item(0);
        System.out.println(curAttr.getNamespaceURI() + ":" + curAttr.getLocalName() + ":" +curAttr.getPrefix());
        assertTrue(curAttr.getNamespaceURI().length() > 0);
        assertTrue(curAttr.getLocalName().length() > 0);
        assertTrue(curAttr.getPrefix().length() > 0); 
    }

    public static void main(String argv[]) {
        
        junit.textui.TestRunner.run(NamespaceTest.class);        

    }

}
