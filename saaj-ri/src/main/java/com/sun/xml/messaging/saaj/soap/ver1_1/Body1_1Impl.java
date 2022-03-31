/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.util.Locale;

import javax.xml.namespace.QName;
import jakarta.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.BodyImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import org.w3c.dom.Element;

public class Body1_1Impl extends BodyImpl {
    public Body1_1Impl(SOAPDocumentImpl ownerDocument, String prefix) {
            super(ownerDocument, NameImpl.createBody1_1Name(prefix));
    }

    public Body1_1Impl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    public SOAPFault addSOAP12Fault(QName faultCode, String faultReason, Locale locale) {
        // log message here
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override
    protected NameImpl getFaultName(String name) {
        // Ignore name
        return NameImpl.createFault1_1Name(null);
    }

    @Override
    protected SOAPBodyElement createBodyElement(Name name) {
        return new BodyElement1_1Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    @Override
    protected SOAPBodyElement createBodyElement(QName name) {
        return new BodyElement1_1Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    @Override
    protected QName getDefaultFaultCode() {
        return new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Server");
    }

    @Override
    protected boolean isFault(SOAPElement child) {
        // SOAP 1.1 faults always use the default name
        return child.getElementName().equals(getFaultName(null));
    }

    @Override
    protected SOAPFault createFaultElement() {
        return new Fault1_1Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(), getPrefix());
    }

}
