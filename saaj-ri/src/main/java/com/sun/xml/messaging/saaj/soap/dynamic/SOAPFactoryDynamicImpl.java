/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.dynamic;

import jakarta.xml.soap.Detail;
import jakarta.xml.soap.SOAPException;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.SOAPFactoryImpl;

/**
 *
 * @author SAAJ RI Development Team
 */
public class SOAPFactoryDynamicImpl extends SOAPFactoryImpl {

    public SOAPFactoryDynamicImpl() {}

    @Override
    protected SOAPDocumentImpl createDocument() {
        return null;
    }

    @Override
    public Detail createDetail() throws SOAPException {
        throw new UnsupportedOperationException(
                "createDetail() not supported for Dynamic Protocol");
    }

}
