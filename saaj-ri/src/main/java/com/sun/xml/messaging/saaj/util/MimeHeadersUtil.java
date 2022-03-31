/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util;

import java.util.Iterator;

import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.MimeHeaders;

public class MimeHeadersUtil {
    public static MimeHeaders copy(MimeHeaders headers) {
        MimeHeaders newHeaders = new MimeHeaders();
        Iterator<MimeHeader> eachHeader = headers.getAllHeaders();
        while (eachHeader.hasNext()) {
            MimeHeader currentHeader = eachHeader.next();

            newHeaders.addHeader(
                currentHeader.getName(),
                currentHeader.getValue());
        }
        return newHeaders;
    }
}
