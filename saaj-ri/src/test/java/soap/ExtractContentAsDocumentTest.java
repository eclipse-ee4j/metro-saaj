/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import java.util.Iterator;

import jakarta.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.TestCase;

import org.w3c.dom.*;

public class ExtractContentAsDocumentTest extends TestCase {

	private SOAPMessage sm1;
	private SOAPEnvelope envelopeOne;

	public ExtractContentAsDocumentTest(String name) {
	        super(name);
    	}

        @Override
	public void setUp() throws Exception {
		createMessageOne();
	}

	private void createMessageOne() throws Exception {
        System.setProperty("saaj.lazy.contentlength", "true");
        	String testDoc =
	 	"<env:Envelope"
                + " xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'"
                + " xmlns:ns1='http://example.com/wsdl'>"
                +    "<env:Header/>"
                +    "<env:Body>"
                +      "<ns1:Hi/>"
                +      "<ns1:Hello>"
                +        "<String_1>Duke!</String_1>"
                +        "<String_2>Hi!</String_2>"
                +      "</ns1:Hello>"
                +    "</env:Body>"
                + "</env:Envelope>";

        	byte[] testDocBytes = testDoc.getBytes("UTF-8");
        	ByteArrayInputStream bais = 
                	new ByteArrayInputStream(testDocBytes);
	        StreamSource strSource = new StreamSource(bais);
        	MessageFactory mf = MessageFactory.newInstance();
	        sm1 = mf.createMessage();
        	SOAPPart sp = sm1.getSOAPPart();
	        sp.setContent(strSource);
		envelopeOne = sp.getEnvelope();
	}
        
    	public void testExtractContentAsDocument() throws Exception {

		SOAPBody body = envelopeOne.getBody();
		String exception = null;
		Document document = null;

                try {
			document = body.extractContentAsDocument();
		} catch(Exception e) {
			exception = e.getMessage();
		}
		assertNotNull("Body has 2 child elements so extract fails.",
			      exception);

		// Remove one out of the two body elements
		Iterator eachChild = body.getChildElements();
		SOAPBodyElement firstElement = (SOAPBodyElement)
					       eachChild.next();
		firstElement.detachNode();
		sm1.saveChanges();

		exception = null;
                try {
                        document = body.extractContentAsDocument();
                } catch(Exception e) {
			fail("Body has exactly one child element.");
                }

		Element element = document.getDocumentElement();
		assertEquals("element has a particular tag name.",
			     element.getTagName(), "ns1:Hello");
		assertEquals("first child element has a particular tag name",
			     ((Element) element.getFirstChild()).getTagName(),
			     "String_1");
                //System.out.println(nodeToString(document));
                //System.out.println(nodeToString(element));
		
                try {
			document = body.extractContentAsDocument();
		} catch(Exception e) {
			exception = e.getMessage();
		}
		assertNotNull("Body is empty so extract fails.", exception);
    	}
    
	public String nodeToString(org.w3c.dom.Node node) throws Exception {
        	// Use a Transformer for output
        	TransformerFactory tFactory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
        	Transformer transformer = tFactory.newTransformer();
        	StringWriter stringWriter = new StringWriter();

        	DOMSource source = new DOMSource(node);
        	StreamResult result = new StreamResult(stringWriter);

        	transformer.transform(source, result);
        	return stringWriter.toString();
	}
}
