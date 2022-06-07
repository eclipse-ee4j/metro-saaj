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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPFaultElement;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import org.w3c.dom.Element;

public abstract class FaultElementImpl
    extends ElementImpl 
    implements SOAPFaultElement {

    private static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN, "com.sun.xml.messaging.saaj.soap.impl.LocalStrings");

    protected FaultElementImpl(SOAPDocumentImpl ownerDoc, NameImpl qname) {
        super(ownerDoc, qname);
    }

    protected FaultElementImpl(SOAPDocumentImpl ownerDoc, QName qname) { 
        super(ownerDoc, qname);
    }

    protected FaultElementImpl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    protected abstract boolean isStandardFaultElement();

    @Override
    public SOAPElement setElementQName(QName newName) throws SOAPException {
            log.log(Level.SEVERE,
                    "SAAJ0146.impl.invalid.name.change.requested",
                    new Object[] {elementQName.getLocalPart().replaceAll("[\r\n]",""),
                                  newName.getLocalPart().replaceAll("[\r\n]","")});
            throw new SOAPException("Cannot change name for "
                                    + elementQName.getLocalPart() + " to "
                                    + newName.getLocalPart());
    }

}
