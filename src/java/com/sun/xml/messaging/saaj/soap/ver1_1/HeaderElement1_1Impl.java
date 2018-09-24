/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;

import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.HeaderElementImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import org.w3c.dom.Element;

public class HeaderElement1_1Impl extends HeaderElementImpl {
    
    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");
    
    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }
    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    @Override
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        HeaderElementImpl copy =
            new HeaderElement1_1Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this,copy);
    }

    @Override
    protected NameImpl getActorAttributeName() {
        return NameImpl.create("actor", null, NameImpl.SOAP11_NAMESPACE);
    }

    // role not supported by SOAP 1.1
    @Override
    protected NameImpl getRoleAttributeName() {
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Role" });
        throw new UnsupportedOperationException("Role not supported by SOAP 1.1");
    }

    @Override
    protected NameImpl getMustunderstandAttributeName() {
        return NameImpl.create("mustUnderstand", null, NameImpl.SOAP11_NAMESPACE);
    }

    // mustUnderstand attribute has literal value "1" or "0"
    @Override
    protected String getMustunderstandLiteralValue(boolean mustUnderstand) {
        return (mustUnderstand == true ? "1" : "0");
    }

    @Override
    protected boolean getMustunderstandAttributeValue(String mu) {
        if ("1".equals(mu) || "true".equalsIgnoreCase(mu))
            return true;
        return false;
    }

    // relay not supported by SOAP 1.1
    @Override
    protected NameImpl getRelayAttributeName() {        
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Relay" });
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    @Override
    protected String getRelayLiteralValue(boolean relayAttr) {
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Relay" });
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    @Override
    protected boolean getRelayAttributeValue(String mu) {
        log.log(
            Level.SEVERE,
            "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1",
            new String[] { "Relay" });
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    @Override
    protected String getActorOrRole() {
        return getActor();
    }

}
