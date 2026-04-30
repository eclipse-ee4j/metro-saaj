/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package bugfixes;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import javax.net.ssl.HttpsURLConnection;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import junit.framework.TestCase;

/**
 *
 * @author lukas
 */
public class SSLTest extends TestCase {

    public void testSSL() throws Exception {
        Handler h = new Handler();
        URL endpoint = new URL(null, "https://www.oracle.com", h);
        // The SOAP call is wrong, but it is used to check if
        // the URLConnection is instance of HttpsURLConnection.
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPConnection con = null;
        try {
            con = SOAPConnectionFactory.newInstance().createConnection();
            con.call(msg, endpoint);
        } catch (Exception t) {
            assertNotNull(h.urlCon);
            assertTrue("invalid type of HttpsURLConnection: " + h.urlCon.getClass().getName(), h.urlCon instanceof HttpsURLConnection);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SOAPException ex) {
                    // ignore
                }
            }
        }
    }

    private static class Handler extends URLStreamHandler {

        URLConnection urlCon;

        @Override
        protected URLConnection openConnection(URL url)
                throws IOException {
            URL urlClone = new URL(url.toString());
            return urlCon = urlClone.openConnection();
        }
    }

}
