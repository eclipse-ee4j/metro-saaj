/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.client.p2p;

import javax.xml.soap.*;

/**
 * Implementation of the SOAPConnectionFactory
 *
 * @author Anil Vijendran (anil@sun.com)
 */
public class HttpSOAPConnectionFactory extends SOAPConnectionFactory {

    @Override
    public SOAPConnection createConnection()
        throws SOAPException
    {
        return new HttpSOAPConnection();
    }
}

