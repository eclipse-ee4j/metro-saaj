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

//import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;
//import com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl;
import junit.framework.TestCase;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TestCase for http://java.net/jira/browse/SAAJ-67
 * Loosing Mime Headers when Content-Type header not present
 *
 * @author Miroslav Kos (miroslav.kos at oracle.com)
 */
public class SAAJ67Test extends TestCase {
    private static final String EXTERNAL_1_1_NAME = "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
    private static final String EXTERNAL_1_2_NAME = "com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl";
    private static final String INTERNAL_1_1_NAME = "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
    private static final String INTERNAL_1_2_NAME = "com.sun.xml.internal.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl";

    void runWithFactory(MessageFactory factory, String messagePath, String contentType) throws SOAPException, IOException {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", contentType);
        headers.addHeader("Some-Header", "Some-Value");
        InputStream is = new FileInputStream(messagePath);
        SOAPMessage msg = factory.createMessage(headers, is);
        assertNotNull(msg.getMimeHeaders().getHeader("Some-Header"));

        headers = new MimeHeaders();
        headers.addHeader("Some-Header", "Some-Value");
        is = new FileInputStream(messagePath);
        msg = factory.createMessage(headers, is);
        assertNotNull(msg.getMimeHeaders().getHeader("Some-Header"));
    }

    public void testFactory1_1() throws IOException, SOAPException {
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        String isJDK = System.getProperty("jdk.build", "false");
        if ("true".equalsIgnoreCase(isJDK)) {
            assertEquals(INTERNAL_1_1_NAME, mf.getClass().getName());
        } else {
            assertEquals(EXTERNAL_1_1_NAME, mf.getClass().getName());
        }
        runWithFactory(mf, "src/test/resources/bugfixes/data/empty-message.xml", "text/xml");
    }

    public void testFactory1_2() throws IOException, SOAPException {
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        String isJDK = System.getProperty("jdk.build", "false");
        if ("true".equalsIgnoreCase(isJDK)) {
            assertEquals(INTERNAL_1_2_NAME, mf.getClass().getName());
        } else {
            assertEquals(EXTERNAL_1_2_NAME, mf.getClass().getName());
        }
        runWithFactory(mf, "src/test/resources/bugfixes/data/empty-message12.xml", "application/soap+xml");
    }
}
