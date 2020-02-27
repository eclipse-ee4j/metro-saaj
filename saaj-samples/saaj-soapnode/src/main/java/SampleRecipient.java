/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import com.sun.xml.soap.ProcessingContext;
import com.sun.xml.soap.SOAPRecipient;

import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeaderElement;

public class SampleRecipient extends SOAPRecipient {

    public void acceptHeaderElement(
        SOAPHeaderElement elem,
        ProcessingContext context)
        throws SOAPException {

        System.out.println("Processed header block : " +
                            elem.getTagName());
    }

    public void handleIncomingFault(ProcessingContext context) {
    }

    public void handleOutgoingFault(ProcessingContext context) {
    }

}
