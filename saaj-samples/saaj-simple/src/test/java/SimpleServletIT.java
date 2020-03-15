/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

class SimpleServletIT {

    @Test
    void simpleServletTest() throws SOAPException, IOException {
        MessageFactory messageFactory = MessageFactory.newInstance();

        String response = new NetHttpTransport().createRequestFactory()
                .buildGetRequest(new GenericUrl("http://localhost:8080/sender"))
                .execute().parseAsString();

        ResponseHtmlParser htmlParser = new ResponseHtmlParser();
        new ParserDelegator().parse(new StringReader(response), htmlParser, false);
        MimeHeaders mimeHeaders = new MimeHeaders();
        SOAPMessage msg = messageFactory.createMessage(mimeHeaders,
                new ByteArrayInputStream(htmlParser.getSoapResponse().getBytes(StandardCharsets.UTF_8)));
        NodeList responseElList = msg.getSOAPBody().getElementsByTagNameNS("", "Response");
        Assertions.assertEquals(1, responseElList.getLength());
        Assertions.assertEquals("This is a response", responseElList.item(0).getTextContent());
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
            isRequest = "request".equals(a.getAttribute(Attribute.ID));
            isResponse = "response".equals(a.getAttribute(Attribute.ID));
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
