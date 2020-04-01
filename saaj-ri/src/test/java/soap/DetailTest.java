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

import jakarta.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.TestCase;

public class DetailTest extends TestCase {
    
    /** Creates a new instance of Test1 */
    public DetailTest(String name) {
	super(name);
    }
    
    public void testDetailImpl() {
        try {
            doit1();
        } catch (Throwable t) {
            System.out.println("Exception: " + t);
            t.printStackTrace();
	    fail();
        }
    }

    public void doit1() throws Exception {
		String testDoc = 		  "<?xml version='1.0' encoding='UTF-8'?>\n" 		+ "<D:Envelope xmlns:D='http://schemas.xmlsoap.org/soap/envelope/'>\n"     		+ "	<D:Body>\n" 	        + "		<D:Fault>\n"             	+ "			<D:faultcode>Client.invalidSignature</D:faultcode>\n"             	+ "			<D:faultstring>invalid signature</D:faultstring>\n"             	+ "			<D:detail>\n"                 + "				27: Invalid Signature\n"             	+ "			</D:detail>\n"         	+ "		</D:Fault>\n"     		+ "	</D:Body>\n" 		+ "</D:Envelope>\n"; 
		byte[] testDocBytes = testDoc.getBytes("UTF-8");
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
		Detail detail = fault.getDetail();
		assertTrue(detail.getPrefix().length()>0);
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

    public void testDetailEntryCR6581434() {
        try {
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage m = mf.createMessage();
            SOAPEnvelope se = m.getSOAPPart().getEnvelope();
            Name codeName  = se.createName("ErrorCode", "as", null);
            SOAPFault fault = m.getSOAPBody().addFault();
            Detail detail = fault.addDetail();
            detail.addNamespaceDeclaration("as", "http://abc.com/test");
            DetailEntry codeDetail = detail.addDetailEntry(codeName);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }
}
