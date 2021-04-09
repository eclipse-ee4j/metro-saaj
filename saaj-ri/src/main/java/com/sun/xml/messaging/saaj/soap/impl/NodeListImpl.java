/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.impl;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Node list wrapper, finding SOAP elements automatically when possible.
 *
 * @author Roman Grigoriadi
 */
public class NodeListImpl implements NodeList {

    private final SOAPDocumentImpl soapDocument;

    private final NodeList nodeList;

    public NodeListImpl(SOAPDocumentImpl soapDocument, NodeList nodeList) {
        this.soapDocument = requireNonNull(soapDocument);
        this.nodeList = requireNonNull(nodeList);
    }

    @Override
    public Node item(int index) {
        return soapDocument.findIfPresent(nodeList.item(index));
    }

    @Override
    public int getLength() {
        return nodeList.getLength();
    }
}
