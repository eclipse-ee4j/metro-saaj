/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
*
* @author SAAJ RI Development Team
*/
package com.sun.xml.messaging.saaj.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.*;

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
                if (attributeName.startsWith("xmlns")
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
