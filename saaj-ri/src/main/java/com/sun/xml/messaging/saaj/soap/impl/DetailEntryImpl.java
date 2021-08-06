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

import javax.xml.namespace.QName;
import jakarta.xml.soap.DetailEntry;
import jakarta.xml.soap.Name;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import org.w3c.dom.Element;

public abstract class DetailEntryImpl
    extends ElementImpl
    implements DetailEntry {
    public DetailEntryImpl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }
    public DetailEntryImpl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public DetailEntryImpl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }
}
