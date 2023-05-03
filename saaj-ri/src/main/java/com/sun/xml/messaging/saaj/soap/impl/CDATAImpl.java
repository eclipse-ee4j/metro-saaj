/*
 * Copyright (c) 1997, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.impl;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

public class CDATAImpl extends TextImpl<CDATASection> implements CDATASection {

    static final String cdataUC = "<![CDATA[";
    static final String cdataLC = "<![cdata[";
    static final String CDATA_CLOSE = "]]>";

    public CDATAImpl(SOAPDocumentImpl ownerDoc, String text) {
        super(ownerDoc, text);
    }

    public CDATAImpl(SOAPDocumentImpl ownerDoc, CharacterData data) {
        super(ownerDoc, data);
    }

    @Override
    protected CDATASection createN(SOAPDocumentImpl ownerDoc, String text) {
        return ownerDoc.getDomDocument().createCDATASection(text);
    }

    @Override
    protected CDATASection createN(SOAPDocumentImpl ownerDoc, CharacterData data) {
        return (CDATASection) data;
    }

    @Override
    protected CDATAImpl doClone() {
        return new CDATAImpl(getSoapDocument(), this.getTextContent());
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
        return false;
    }

}
