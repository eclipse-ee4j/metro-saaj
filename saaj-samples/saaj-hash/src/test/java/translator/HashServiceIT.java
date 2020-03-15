/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package translator;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Set;

class HashServiceIT {

    static final String TEST_TEXT = "Oracle";

    @Test
    void testAttachements() throws IOException {
        testDigests("http://localhost:8080/translation?trAs=attachments&translate=" + TEST_TEXT);
    }

    @Test
    void testBody() throws IOException {
        testDigests("http://localhost:8080/translation?trAs=body&translate=" + TEST_TEXT);
    }

    private void testDigests(String url) throws IOException {
        String response = new NetHttpTransport().createRequestFactory()
                .buildGetRequest(new GenericUrl(url))
                .execute().parseAsString();
        new ParserDelegator().parse(new StringReader(response), new ResponseHtmlParser(), false);
    }

    static class ResponseHtmlParser extends HTMLEditorKit.ParserCallback {
        private String digestType = null;
        private static final Set<String> AVAILABLE_DIGESTS =
                Security.getAlgorithms(MessageDigest.class.getSimpleName());

        @Override
        public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            if ("div".equals(t.toString()) && null != a.getAttribute("digest-type")) {
                digestType = String.valueOf(a.getAttribute("digest-type"));
            } else {
                digestType = null;
            }
        }

        @Override
        public void handleEndTag(HTML.Tag t, int pos) {
            digestType = null;
        }

        @Override
        public void handleText(char[] data, int pos) {
            if (digestType != null) {
                HashingService service = new HashingService();
                if (AVAILABLE_DIGESTS.contains(digestType)) {
                    Assertions.assertEquals(service.makeHash(TEST_TEXT, digestType), new String(data));
                }
            }
        }
    }
}
