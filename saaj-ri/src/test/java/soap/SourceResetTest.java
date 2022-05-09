/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap;

import java.io.*;

import jakarta.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;


import junit.framework.TestCase;

public class SourceResetTest extends TestCase {

    private String[] msgText;

    public SourceResetTest(String name) {
        super(name);
    }

    public void testReset() throws Exception {
        String message = messageThroughBodyElement +
            "<ns0:helloWorld><String_1 xsi:type=\"xsd:string\">" +
            "foo2</String_1></ns0:helloWorld>" +
            endBodyElementMessage;

        // JAXRPC servlet creates SOAP message
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        //ByteInputStream in = new ByteInputStream(message.getBytes(),
	//		message.getBytes().length);
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
        SOAPMessage msg = MessageFactory.newInstance().createMessage(headers,
			in);

        // JAXRPC streaming parser consumes the of stream partially/fully
        // Then invokes the service method
        jaxrpcParser(msg);

        // Uncomment the following to work without exceptions. This resets the
        // stream. Since SAAJ keeps a copy in JAXMStreamSource, SAAJ can get
        // bytes any time. JAXRPC doesn't call close() consistently.
        //in.close();

        // Service method calls the following to access message. This doesn't
        // work because the stream is incorrect state.
        SOAPPart part = msg.getSOAPPart();
		SOAPEnvelope envelope = part.getEnvelope();
    }

    /*
     * Parses the SOAP message and deserializes to data types
     */
    protected static void jaxrpcParser(SOAPMessage msg)
		throws Exception {

        Source source = msg.getSOAPPart().getContent();
        InputStream istream = ((StreamSource) source).getInputStream();

        if (istream != null) {
        	byte[] buf = new byte[1024];
        	int num = 0;
            while ((num = istream.read(buf)) != -1) {
            }
        }
    }

    private static final String UTF8_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static final String messageThroughBodyElement =
        "<env:Envelope " +
        "xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
        "xmlns:ns0=\"http://hello.org/wsdl\" " +
        "env:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
        "<env:Body>";
    private String endBodyElementMessage = "</env:Body></env:Envelope>";

}
