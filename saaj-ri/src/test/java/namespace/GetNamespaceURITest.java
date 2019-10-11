/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package namespace;

import javax.xml.soap.*;

import junit.framework.TestCase;


public class GetNamespaceURITest extends TestCase {

    public GetNamespaceURITest(String name) {
        super(name);
    }

    public void testGetNamespaceURI() throws Exception {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPBody body = msg.getSOAPBody();
        Name name = SOAPFactory.newInstance().createName("Content", "p", "some-uri");
        SOAPElement element = body.addBodyElement(name).addChildElement("Value").addTextNode("SUNW");
        assertEquals(element.getNamespaceURI("p"), "some-uri");
    }
}
