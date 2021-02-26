/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.impl;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class SOAPTextImpl extends TextImpl<Text> implements Text {

    public SOAPTextImpl(SOAPDocumentImpl ownerDoc, String text) {
        super(ownerDoc, text);
    }

    public SOAPTextImpl(SOAPDocumentImpl ownerDoc, CharacterData data) {
        super(ownerDoc, data);
    }

    @Override
    public short getNodeType() {
        return Node.TEXT_NODE;
    }

    @Override
    protected SOAPTextImpl doClone() {
        return new SOAPTextImpl(getSoapDocument(), this.getTextContent());
    }

    @Override
    protected Text createN(SOAPDocumentImpl ownerDoc, String text) {
        return ownerDoc.getDomDocument().createTextNode(text);
    }

    @Override
    protected Text createN(SOAPDocumentImpl ownerDoc, CharacterData data) {
        return (Text) data;
    }

    @Override
    public Text splitText(int offset) throws DOMException {
        Text text = getDomElement().splitText(offset);
        getSoapDocument().registerChildNodes(text, true);
        return text;
    }

    @Override
    public boolean isElementContentWhitespace() {
        return getDomElement().isElementContentWhitespace();
    }

    @Override
    public String getWholeText() {
        return getDomElement().getWholeText();
    }

    @Override
    public Text replaceWholeText(String content) throws DOMException {
        Text text = getDomElement().replaceWholeText(content);
        getSoapDocument().registerChildNodes(text, true);
        return text;
    }

    @Override
    public boolean isComment() {
        String txt = getNodeValue();
        if (txt == null) {
            return false;
        }
        return txt.startsWith("<!--") && txt.endsWith("-->");
    }

}
