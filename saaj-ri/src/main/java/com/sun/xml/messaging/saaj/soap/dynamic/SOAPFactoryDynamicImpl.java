/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.xml.messaging.saaj.soap.dynamic;

import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.SOAPFactoryImpl;

public class SOAPFactoryDynamicImpl extends SOAPFactoryImpl {
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
