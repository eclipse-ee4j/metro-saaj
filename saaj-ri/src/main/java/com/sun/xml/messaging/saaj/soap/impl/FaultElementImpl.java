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

import java.util.logging.Level;

import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPFaultElement;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import org.w3c.dom.Element;

public abstract class FaultElementImpl
    extends ElementImpl 
    implements SOAPFaultElement {

    protected FaultElementImpl(SOAPDocumentImpl ownerDoc, NameImpl qname) { 
        super(ownerDoc, qname);
    }

    protected FaultElementImpl(SOAPDocumentImpl ownerDoc, QName qname) { 
        super(ownerDoc, qname);
    }

    public FaultElementImpl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    protected abstract boolean isStandardFaultElement();

    @Override
    public SOAPElement setElementQName(QName newName) throws SOAPException {
            log.log(Level.SEVERE,
                    "SAAJ0146.impl.invalid.name.change.requested",
                    new Object[] {elementQName.getLocalPart(),
                                  newName.getLocalPart()});
            throw new SOAPException("Cannot change name for "
                                    + elementQName.getLocalPart() + " to "
                                    + newName.getLocalPart());
    }

}
