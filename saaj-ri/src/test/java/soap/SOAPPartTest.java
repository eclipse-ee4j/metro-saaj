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

import java.io.ByteArrayInputStream;

import jakarta.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

public class SOAPPartTest extends TestCase {

	public SOAPPartTest(String name) {
	        super(name);
    	}

    	public void testGetChildNodes() throws Exception {
        	String testDoc =
	 	"<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:ns1='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns1:sayHello>\n"
                + "      <String_1>Duke!</String_1>\n"
                + "    </ns1:sayHello>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";

        	byte[] testDocBytes = testDoc.getBytes("UTF-8");
        	ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
	        StreamSource strSource = new StreamSource(bais);
        	MessageFactory mf = MessageFactory.newInstance();
	        SOAPMessage sm = mf.createMessage();
        	SOAPPart sp = sm.getSOAPPart();
	        sp.setContent(strSource);
		assertTrue(sp.getChildNodes().getLength()>0);
    	}
}

