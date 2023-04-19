/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package soap;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ElementOwnerTest {

    /**
     * Verifies that the fix for #183 works
     *
     * @throws IOException
     * @throws SOAPException
     */
    @Test
    public void testSetIdAttributeNode() throws IOException, SOAPException {
        final var headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml");
        final var testDoc = "<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:ns1='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns1:element attr='attr-value'/>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";
        try (var in = new ByteArrayInputStream(testDoc.getBytes(StandardCharsets.UTF_8))) {
            final var part = MessageFactory.newInstance().createMessage(headers, in).getSOAPPart();
            final var elements = part.getElementsByTagNameNS("http://example.com/wsdl", "element");
            assertThat(elements.getLength(), is(1));
            assertThat(elements.item(0), instanceOf(Element.class));
            final var element = (Element) elements.item(0);
            final var attr = element.getAttributeNodeNS(null, "attr");
            assertThat(attr, notNullValue());
            assertThat(attr.isId(), is(false));
            // this would fail with org.w3c.dom.DOMException: NOT_FOUND_ERR
            element.setIdAttributeNode(attr, true);
            assertThat(attr.isId(), is(true));
        }
    }
}
