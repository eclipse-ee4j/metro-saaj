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

import javax.xml.namespace.QName;
import jakarta.xml.soap.DetailEntry;
import jakarta.xml.soap.Name;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import org.w3c.dom.Element;

public class Detail1_1Impl extends DetailImpl {

    public Detail1_1Impl(SOAPDocumentImpl ownerDoc, String prefix) {
        super(ownerDoc, NameImpl.createDetail1_1Name(prefix));
    }
    public Detail1_1Impl(SOAPDocumentImpl ownerDoc) {
        super(ownerDoc, NameImpl.createDetail1_1Name());
    }

    public Detail1_1Impl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    @Override
    protected DetailEntry createDetailEntry(Name name) {
        return new DetailEntry1_1Impl(
            (SOAPDocumentImpl) getOwnerDocument(),
            name);
    }
    @Override
    protected DetailEntry createDetailEntry(QName name) {
        return new DetailEntry1_1Impl(
            (SOAPDocumentImpl) getOwnerDocument(),
            name);
    }

}
