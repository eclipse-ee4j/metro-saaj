/*
 * Copyright (c) 1997, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import com.sun.xml.soap.ProcessingContext;
import com.sun.xml.soap.SOAPAnnotator;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

public class SampleAnnotator extends SOAPAnnotator {

    public void annotateHeader(
        SOAPHeader hdr,
        ProcessingContext context)
        throws SOAPException {

        System.out.println("Annotation done.");
    }

    public void handleIncomingFault(ProcessingContext context) {
    }

    public void handleOutgoingFault(ProcessingContext context) {
    }

}
