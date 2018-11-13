/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.impl;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Objects;

/**
 * {@link NamedNodeMap} wrapper, finding SOAP elements automatically when possible.
 *
 * @author Roman Grigoriadi
 */
public class NamedNodeMapImpl implements NamedNodeMap {

    private final NamedNodeMap namedNodeMap;

    private final SOAPDocumentImpl soapDocument;

    /**
     * Create wrapper.
     *
     * @param namedNodeMap node map to wrap
     * @param soapDocument soap document to find soap elements
     */
    public NamedNodeMapImpl(NamedNodeMap namedNodeMap, SOAPDocumentImpl soapDocument) {
        Objects.requireNonNull(namedNodeMap);
        Objects.requireNonNull(soapDocument);
        this.namedNodeMap = namedNodeMap;
        this.soapDocument = soapDocument;
    }

    @Override
    public Node getNamedItem(String name) {
        return soapDocument.findIfPresent(namedNodeMap.getNamedItem(name));
    }

    @Override
    public Node setNamedItem(Node arg) throws DOMException {
        return namedNodeMap.setNamedItem(arg);
    }

    @Override
    public Node removeNamedItem(String name) throws DOMException {
        return namedNodeMap.removeNamedItem(name);
    }

    @Override
    public Node item(int index) {
        return namedNodeMap.item(index);
    }

    @Override
    public int getLength() {
        return namedNodeMap.getLength();
    }

    @Override
    public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return namedNodeMap.getNamedItemNS(namespaceURI, localName);
    }

    @Override
    public Node setNamedItemNS(Node arg) throws DOMException {
        return namedNodeMap.setNamedItemNS(arg);
    }

    @Override
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return namedNodeMap.removeNamedItemNS(namespaceURI, localName);
    }
}
