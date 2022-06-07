/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.impl;

import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;

import java.util.logging.Logger;

public class SOAPCommentImpl extends TextImpl<Comment> implements Comment {

    private static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN, "com.sun.xml.messaging.saaj.soap.impl.LocalStrings");

    public SOAPCommentImpl(SOAPDocumentImpl ownerDoc, String text) {
        super(ownerDoc, text);
    }

    public SOAPCommentImpl(SOAPDocumentImpl ownerDoc, CharacterData data) {
        super(ownerDoc, data);
    }

    @Override
    protected Comment createN(SOAPDocumentImpl ownerDoc, String text) {
        return ownerDoc.getDomDocument().createComment(text);
    }

    @Override
    protected Comment createN(SOAPDocumentImpl ownerDoc, CharacterData data) {
        return (Comment) data;
    }

    @Override
    protected SOAPCommentImpl doClone() {
        return new SOAPCommentImpl(getSoapDocument(), this.getTextContent());
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    public Text splitText(int offset) throws DOMException {
        log.severe("SAAJ0113.impl.cannot.split.text.from.comment");
        throw new UnsupportedOperationException("Cannot split text from a Comment Node.");
    }

    @Override
    public Text replaceWholeText(String content) throws DOMException {
        log.severe("SAAJ0114.impl.cannot.replace.wholetext.from.comment");
        throw new UnsupportedOperationException("Cannot replace Whole Text from a Comment Node.");
    }

    @Override
    public String getWholeText() {
        //TODO: maybe we have to implement this in future.
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public boolean isElementContentWhitespace() {
        //TODO: maybe we have to implement this in future.
        throw new UnsupportedOperationException("Not Supported");
    }

}
