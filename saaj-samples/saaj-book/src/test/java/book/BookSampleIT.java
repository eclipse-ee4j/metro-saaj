/*
 *
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 */

package book;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

class BookSampleIT {

    @Test
    void testActorsHeader() throws SOAPException, IOException {
        MessageFactory messageFactory = MessageFactory.newInstance();

        String response = new NetHttpTransport().createRequestFactory()
                .buildGetRequest(new GenericUrl("http://localhost:8080/sender"))
                .execute().parseAsString();

        ResponseHtmlParser htmlParser = new ResponseHtmlParser();
        new ParserDelegator().parse(new StringReader(response), htmlParser, false);

        MimeHeaders mimeHeaders = new MimeHeaders();
        SOAPMessage msg = messageFactory.createMessage(mimeHeaders,
                new ByteArrayInputStream(htmlParser.getSoapResponse().getBytes(StandardCharsets.UTF_8)));

        msg.getSOAPHeader().examineMustUnderstandHeaderElements("http://saaj.sample/receiver")
                .forEachRemaining(bookEl -> {
                            bookEl.getChildElements(QName.valueOf("{http://saaj.sample/book}isbn"))
                                    .forEachRemaining(isbnEl -> Assertions.assertEquals("9-999-99999-9", isbnEl.getValue()));
                            bookEl.getChildElements(QName.valueOf("{http://saaj.sample/book}edition"))
                                    .forEachRemaining(editionEl -> Assertions.assertEquals("2", editionEl.getValue()));
                            ;
                        }
                );
    }

    static class ResponseHtmlParser extends HTMLEditorKit.ParserCallback {
        private boolean isRequest = false;
        private boolean isResponse = false;
        private String soapRequest;
        private String soapResponse;

        @Override
        public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            if (!"xmp".equals(t.toString())) {
                return;
            }
            isRequest = "request".equals(a.getAttribute(HTML.Attribute.ID));
            isResponse = "response".equals(a.getAttribute(HTML.Attribute.ID));
        }

        @Override
        public void handleText(char[] data, int pos) {
            if (isRequest) {
                soapRequest = new String(data);
            } else if (isResponse) {
                soapResponse = new String(data);
            }
        }

        public String getSoapResponse() {
            return soapResponse;
        }

        public String getSoapRequest() {
            return soapRequest;
        }
    }
}
