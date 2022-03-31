/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.ver1_2;

import jakarta.xml.soap.Detail;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFault;

import javax.xml.namespace.QName;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.SOAPFactoryImpl;

public class SOAPFactory1_2Impl extends SOAPFactoryImpl {
    @Override
    protected SOAPDocumentImpl createDocument() {
        return (new SOAPPart1_2Impl()).getDocument();
    }

    @Override
    public Detail createDetail() throws SOAPException {
        return new Detail1_2Impl(createDocument());
    }

    @Override
    public SOAPFault createFault(String reasonText, QName faultCode)
        throws SOAPException {
         if (faultCode == null) {
            throw new IllegalArgumentException("faultCode argument for createFault was passed NULL");
        }
        if (reasonText == null) {
            throw new IllegalArgumentException("reasonText argument for createFault was passed NULL");
        }
        Fault1_2Impl fault = new Fault1_2Impl(createDocument());
        fault.setFaultCode(faultCode);
        fault.setFaultString(reasonText);
        return fault;
    }
                                                                                                           
    @Override
    public SOAPFault createFault() throws SOAPException {
        Fault1_2Impl fault = new Fault1_2Impl(createDocument());
        fault.setFaultCode(fault.getDefaultFaultCode());
        fault.setFaultString("Fault string, and possibly fault code, not set");
        return fault;
    }

}
