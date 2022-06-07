/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.xml.XMLConstants;

/**
 *
 * @author SAAJ RI Development Team
 */
public class NamespaceContextIterator implements Iterator {
    Node context;
    NamedNodeMap attributes = null;
    int attributesLength;
    int attributeIndex;
    Attr next = null;
    Attr last = null;
    boolean traverseStack = true;

    public NamespaceContextIterator(Node context) {
        this.context = context;
        findContextAttributes();
    }

    public NamespaceContextIterator(Node context, boolean traverseStack) {
        this(context);
        this.traverseStack = traverseStack;
    }

    protected void findContextAttributes() {
        while (context != null) {
            int type = context.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                attributes = context.getAttributes();
                attributesLength = attributes.getLength();
                attributeIndex = 0;
                return;
            } else {
                context = null;
            }
        }
    }

    protected void findNext() {
        while (next == null && context != null) {
            for (; attributeIndex < attributesLength; ++attributeIndex) {
                Node currentAttribute = attributes.item(attributeIndex);
                String attributeName = currentAttribute.getNodeName();
                if (attributeName.startsWith(XMLConstants.XMLNS_ATTRIBUTE)
                    && (attributeName.length() == 5
                        || attributeName.charAt(5) == ':')) {
                    next = (Attr) currentAttribute;
                    ++attributeIndex;
                    return;
                }
            }
            if (traverseStack) {
                context = context.getParentNode();
                findContextAttributes();
            } else {
                context = null;
            }
        }
    }

    @Override
    public boolean hasNext() {
        findNext();
        return next != null;
    }

    @Override
    public Object next() {
        return getNext();
    }
    
    public Attr nextNamespaceAttr() {
        return getNext();
    }

    protected Attr getNext() {
        findNext();
        if (next == null) {
            throw new NoSuchElementException();
        }
        last = next;
        next = null;
        return last;
    }

    @Override
    public void remove() {
        if (last == null) {
            throw new IllegalStateException();
        }
        ((Element) context).removeAttributeNode(last);
    }

}
