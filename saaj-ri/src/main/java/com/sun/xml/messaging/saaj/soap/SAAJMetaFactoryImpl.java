/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap;

import jakarta.xml.soap.SAAJMetaFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPException;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xml.messaging.saaj.util.LogDomainConstants;

public class SAAJMetaFactoryImpl extends SAAJMetaFactory {

    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    @Override
    protected  MessageFactory newMessageFactory(String protocol)
        throws SOAPException {
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl();
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl();
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
              return new com.sun.xml.messaging.saaj.soap.dynamic.SOAPMessageFactoryDynamicImpl(); 
        } else {
            log.log(
                Level.SEVERE,
                "SAAJ0569.soap.unknown.protocol",
                new Object[] {protocol, "MessageFactory"});
            throw new SOAPException("Unknown Protocol: " + protocol + 
                                        "  specified for creating MessageFactory");
        }
    }

    @Override
    protected  SOAPFactory newSOAPFactory(String protocol)
        throws SOAPException {
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl();
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.ver1_2.SOAPFactory1_2Impl();
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
            return new com.sun.xml.messaging.saaj.soap.dynamic.SOAPFactoryDynamicImpl(); 
        } else {
            log.log(
                Level.SEVERE,
                "SAAJ0569.soap.unknown.protocol",
                new Object[] {protocol, "SOAPFactory"});
            throw new SOAPException("Unknown Protocol: " + protocol + 
                                        "  specified for creating SOAPFactory");
        }
    }

}
