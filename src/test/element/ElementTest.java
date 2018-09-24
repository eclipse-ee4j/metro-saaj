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
* @author SAAJ RI Development Team
*/

package element;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.ElementImpl;
import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Method;


public class ElementTest extends TestCase {

    public ElementTest(String name) {
        super(name);
    }

    public void testSoapHeaderParentElement() throws SOAPException {
        SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope envelope = sp.getEnvelope();
        SOAPHeader soapHeader = envelope.getHeader();
        SOAPHeaderElement headerInnerElement = soapHeader.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));
        headerInnerElement.setActor("actor-URI");

        headerInnerElement.detachNode();
        headerInnerElement.setParentElement(soapHeader);
        SOAPElement se = headerInnerElement.getParentElement();
        Assert.assertTrue(se instanceof SOAPHeader);
    }

    public void testSoapResult() throws Exception {
        MessageFactory msgFac = MessageFactory.newInstance();

        SOAPMessage message = msgFac.createMessage();

        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = sp.getEnvelope();
        envelope.getHeader();
        SAAJResult result = new SAAJResult(message);
        javax.xml.soap.Node res = result.getResult();

        Assert.assertNotNull(res);
    }

    public void testGetAnAttribute() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");

        Name originalAttributeName =
            factory.createName("unqualifiedName");
        String originalAttributeValue = "aValue";
        element.addAttribute(originalAttributeName, originalAttributeValue);

        Name theAttributeName = null;
        String theAttributeValue = null;
        int count = 0;
        for (Iterator eachAttribute = element.getAllAttributes();
            eachAttribute.hasNext();
            ) {
            theAttributeName = (Name) eachAttribute.next();
            theAttributeValue = element.getAttributeValue(theAttributeName);

            ++count;
            assertEquals(1, count);
        }

        assertEquals(
            "Qualified names of attributes must match",
            originalAttributeName.getQualifiedName(),
            theAttributeName.getQualifiedName());
        assertEquals(
            "Attribute values must match",
            originalAttributeValue,
            theAttributeValue);

        /*
        for (Iterator eachAttribute = element.getAllAttributesAsQNames();
            eachAttribute.hasNext();
            ) {
            assertTrue(eachAttribute.next() instanceof QName);
        }*/
    }

    public void testGetAttributeValue() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");

        Name originalAttributeName =
            factory.createName("unqualifiedName");
        String originalAttributeValue = "aValue";
        element.addAttribute(originalAttributeName, originalAttributeValue);
        element.removeAttribute(originalAttributeName);

        Name theAttributeName = null;
        String theAttributeValue = null;
        String unexpectedAttributelist = "";
        int count = 0;
        for (Iterator eachAttribute = element.getAllAttributes();
            eachAttribute.hasNext();
            ) {
            theAttributeName = (Name) eachAttribute.next();
            theAttributeValue = element.getAttributeValue(theAttributeName);

            ++count;

            unexpectedAttributelist += theAttributeName.getQualifiedName()
                + " = "
                + theAttributeValue
                + "\n";
        }
        assertEquals(
            "Unexpected attributes:\n" + unexpectedAttributelist,
            0,
            count);

        theAttributeValue = element.getAttributeValue(originalAttributeName);

        assertTrue(
            "Should have been null but was: " + "\"" + theAttributeValue + "\"",
            null == theAttributeValue);
    }

    public void testAddTextNode() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");

        String originalText = "<txt>text</txt>";
        element.addTextNode(originalText);
        String returnedText = element.getValue();

        assertEquals(originalText, returnedText);
    }

    public void testAddChildElement() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");

        element.addChildElement("child");

        Iterator eachChild =
            element.getChildElements(factory.createName("child"));

        assertTrue("First element is there", eachChild.hasNext());
        SOAPElement child = (SOAPElement) eachChild.next();
        assertEquals("child", child.getTagName());
        assertFalse("Extra elements", eachChild.hasNext());
    }

    public void testAddHeaderElement() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage msg = messageFactory.createMessage();

        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");
        element.addChildElement("child");

        SOAPHeader header = msg.getSOAPHeader();
        header.addChildElement(element);

        Iterator eachHeader = header.getChildElements();

        assertTrue("First header is there", eachHeader.hasNext());
        SOAPHeaderElement headerElement = (SOAPHeaderElement) eachHeader.next();
        assertEquals("testElement", headerElement.getTagName());
        assertFalse("Extra headers", eachHeader.hasNext());

        Iterator eachChild = headerElement.getChildElements();
        assertTrue("First header child is there", eachChild.hasNext());
        SOAPElement child = (SOAPElement) eachChild.next();
        assertEquals("child", child.getTagName());
        assertFalse("Extra children", eachChild.hasNext());
    }

    public void testGetDetailEntries() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFault fault = body.addFault();

        Detail newDetail = fault.addDetail();
        SOAPFactory soapFact = SOAPFactory.newInstance();
        SOAPElement elem =
            soapFact.createElement("detailElem", "de", "http://foo.org");
        SOAPElement entry = newDetail.addChildElement(elem);
        assertTrue(entry instanceof DetailEntry);

        Detail detail = fault.getDetail();
        Iterator detailEntries = detail.getDetailEntries();

        while (detailEntries.hasNext()) {
            Object obj = detailEntries.next();
            assertTrue(obj instanceof DetailEntry);
        }

    }

    public void testAddFault() throws Exception {
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        //MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPBody body = msg.getSOAPBody();
        SOAPFactory soapFact = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        //SOAPFactory soapFact = SOAPFactory.newInstance();
        SOAPFault fault = soapFact.createFault();
        fault = (SOAPFault)body.addChildElement(fault);
        //fault.addDetail();
        //SOAPFactory soapFact1 = SOAPFactory.newInstance();
        Detail detail = soapFact.createDetail();
        fault.addChildElement(detail); 
        //msg.writeTo(System.out);
   }


    public void testConvertHeaderElement() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPHeader header = msg.getSOAPHeader();

        SOAPFactory soapFact = SOAPFactory.newInstance();
        SOAPElement elem =
            soapFact.createElement("headerElem", "he", "http://foo.org");
        SOAPElement se = header.addChildElement(elem);
        assertTrue(se instanceof SOAPHeaderElement);
    }
    public void testAddBodyElement() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage msg = messageFactory.createMessage();

        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");
        element.addChildElement("child");

        SOAPBody body = msg.getSOAPBody();
        body.addChildElement(element);

        Iterator eachBodyElement = body.getChildElements();

        assertTrue("Body element is there", eachBodyElement.hasNext());
        SOAPBodyElement bodyElement = (SOAPBodyElement) eachBodyElement.next();
        assertEquals("testElement", bodyElement.getTagName());
        assertFalse("Extra bodies", eachBodyElement.hasNext());

        Iterator eachChild = bodyElement.getChildElements();
        assertTrue("First body child is there", eachChild.hasNext());
        SOAPElement child = (SOAPElement) eachChild.next();
        assertEquals("child", child.getTagName());
        assertFalse("Extra children", eachChild.hasNext());
    }

    public void testDetachChildElements() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("parent");

        element.addChildElement("one");
        element.addChildElement("two");
        element.addChildElement("three");

        SOAPElement child;
        Iterator eachChild = element.getChildElements();
        assertTrue("First child", eachChild.hasNext());
        child = (SOAPElement) eachChild.next();
        child.detachNode();
        assertTrue("Second child", eachChild.hasNext());
        child = (SOAPElement) eachChild.next();
        child.detachNode();
        assertTrue("Third child", eachChild.hasNext());
        child = (SOAPElement) eachChild.next();
        child.detachNode();
        assertFalse("Extra children", eachChild.hasNext());

        eachChild = element.getChildElements();
        assertFalse("Still has children", eachChild.hasNext());
    }

    public void testAddChildElementWithQName() throws Exception {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPElement element = factory.createElement("testElement");

        element.addChildElement("child", "prefix", "uri");
        Iterator eachChild =
            element.getChildElements(factory.createName("child", "prefix", "uri"));

        assertTrue("First element is there", eachChild.hasNext());
        SOAPElement child = (SOAPElement) eachChild.next();
        assertEquals("prefix:child", child.getTagName());
        assertFalse("Extra elements", eachChild.hasNext());
    }

    public void testAddDetailEntry() throws Exception {
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        Name name =
            soapFactory.createName(
                "BasicFaultElement",
                "ns0",
                "http://faultservice.org/types");
        Name name2 =
            soapFactory.createName(
                "AdditionalElement",
                "ns0",
                "http://faultservice.org/types");
        Detail detail = soapFactory.createDetail();
        DetailEntry entry = detail.addDetailEntry(name);

        SOAPElement child = entry.addChildElement("Project");
        entry.addChildElement("Mi", "ns0", "http://faultservice.org/types");
        entry.addChildElement("Chiamo", "ns0", "http://faultservice.org/types");
        entry.addChildElement("JAXRPC", "ns0", "http://faultservice.org/types");
        entry.addChildElement("SI", "ns0", "http://faultservice.org/types");
        entry.addChildElement(
            "Implementation",
            "ns0",
            "http://faultservice.org/types");
        child.addTextNode("Il mio nome e JAXRPC SI");
        entry = detail.addDetailEntry(name2);

        child = entry.addChildElement("Project");
        child.addTextNode("2 text");
        
        Node firstChild = detail.getFirstChild();
        assertTrue(firstChild != null);
        Node secondChild = firstChild.getNextSibling();
        assertTrue(secondChild != null);
    }
    
    public void testConvertElementWithXmlnsAttribute() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPHeader header = message.getSOAPHeader();
        
        SOAPPart document = message.getSOAPPart();
        Element element = document.createElementNS("http://bogus", "b:foo");
        element.setAttribute("xmlns", "http://bogus");
        
        header.insertBefore(element,null);
        
        Element firstChild = (Element) header.getChildElements().next();
        assertTrue(firstChild instanceof SOAPHeaderElement);
    }

    public void testCreateElementWithDomElement() throws Exception {
    
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element element = document.createElementNS("http://bogus", "b:foo");
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        Element childElement = document.createElementNS("http://test", "x:child");
        element.appendChild(childElement);
        element.setAttribute("junk","true");

        Element soapElement = SOAPFactory.newInstance().createElement(element);
        assertTrue(soapElement instanceof SOAPElement);
        SOAPDocumentImpl soapDocument = ((ElementImpl)soapElement).getSoapDocument();

        Element soapElementCopy = soapFactory.createElement(soapElement);
        // now copy and soapElement should be the same reference
        assertTrue(soapElementCopy == soapElement);

        NodeList nl = soapElementCopy.getChildNodes();
        assertTrue(nl.getLength() == 1);
        final javax.xml.soap.Node foundSoapElement = (javax.xml.soap.Node) nl.item(0);
        assertTrue(foundSoapElement instanceof SOAPElement);
        assertTrue(soapElementCopy.getAttribute("junk").equals("true"));
    }

    public void testCreateElementWithDomElement2() throws Exception {
    
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element element = document.createElementNS("http://bogus", "foo");
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        Element childElement = document.createElementNS("http://test", "x:child");
        element.appendChild(childElement);
        element.setAttribute("junk","true");

        Element soapElement = SOAPFactory.newInstance().createElement(element);
        assertTrue(soapElement instanceof SOAPElement);
        assertTrue(soapElement.getPrefix() == null);
    }

    public void testRemoveAttribute() throws Exception {

        // testcase for bugfix 6211152

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPFactory soapFactory = SOAPFactory.newInstance(); 
        SOAPElement soapElement = soapFactory.createElement("foo", "b", "http://bogus");
        soapElement =
            soapElement.addAttribute(
                soapFactory.createName("junk", "c", "http://bogus1"),
                "NOTDELETED");
        soapElement = message.getSOAPBody().addChildElement(soapElement);
        soapElement.removeAttribute(soapFactory.createName("junk", "c", "http://bogus1"));
        assertNull(
            soapElement.getAttributeValue(
                soapFactory.createName("junk", "c", "http://bogus1")));
    }

   public void testGetRole() throws Exception { 


        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = factory.createMessage();
        Document document = message.getSOAPPart();

        SOAPElement element = (SOAPElement)document.createElementNS("http://bogus", "b:foo");
        SOAPHeader  header = message.getSOAPHeader();
        SOAPHeaderElement hdrElement = (SOAPHeaderElement)header.addChildElement(element);
        hdrElement.setActor("http://bogus");
        assertTrue(hdrElement.getRole() != null);
        assertTrue(hdrElement.getActor() != null);

        hdrElement.setRole("http://bogus1");
        assertTrue(hdrElement.getActor() != null);

   }

   public void testGetActor() throws Exception { 

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        Document document = message.getSOAPPart();

        SOAPElement element = (SOAPElement)document.createElementNS("http://bogus", "b:foo");
        SOAPHeader  header = message.getSOAPHeader();
        SOAPHeaderElement hdrElement = (SOAPHeaderElement)header.addChildElement(element);
        hdrElement.setActor("http://bogus");
        assertTrue(hdrElement.getActor() != null);
        try {
            hdrElement.getRole();
            assertTrue(false);
        } catch (UnsupportedOperationException ex) {
        }
   }

   public void testGetSetRelay11() throws Exception { 

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        Document document = message.getSOAPPart();

        SOAPElement element = (SOAPElement)document.createElementNS("http://bogus", "b:foo");
        SOAPHeader  header = message.getSOAPHeader();
        SOAPHeaderElement hdrElement = (SOAPHeaderElement)header.addChildElement(element);
        try {
            hdrElement.setRelay(true);
            assertTrue(false);
        } catch (UnsupportedOperationException ex) {

        }

        try {
            hdrElement.getRelay();
            assertTrue(false);
        } catch (UnsupportedOperationException ex) {
        }
   }

   public void testGetSetRelay12() throws Exception { 

        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = factory.createMessage();
        Document document = message.getSOAPPart();

        SOAPElement element = (SOAPElement)document.createElementNS("http://bogus", "b:foo");
        SOAPHeader  header = message.getSOAPHeader();
        SOAPHeaderElement hdrElement = (SOAPHeaderElement)header.addChildElement(element);
        hdrElement.setRelay(true);

        assertTrue(hdrElement.getRelay() == true);
   }
   private static Name createFromTagName(String tagName) {
        Class cls = null;
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
    private static Name createName(String tagName, String uri) {
        Class cls = null;
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
                meth = cls.getMethod("create",String.class, String.class, String.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                int index = tagName.indexOf(':');
                if (index < 0) {

                    Name nm = (Name) meth.invoke(null, (Object[]) new String[]{tagName, "", uri});
                    return nm;

                } else {
                    Name nm = (Name) meth.invoke(null,(Object[]) new String[]{
                                tagName.substring(index + 1),
                                tagName.substring(0, index),
                                uri});
                    return nm;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
