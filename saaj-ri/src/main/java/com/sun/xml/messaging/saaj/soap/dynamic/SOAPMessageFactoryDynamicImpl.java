/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
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


import jakarta.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.MessageFactoryImpl;

public class SOAPMessageFactoryDynamicImpl extends MessageFactoryImpl {
    @Override
    public SOAPMessage createMessage() throws SOAPException {
        throw new UnsupportedOperationException(
                "createMessage() not supported for Dynamic Protocol");
    }
}
