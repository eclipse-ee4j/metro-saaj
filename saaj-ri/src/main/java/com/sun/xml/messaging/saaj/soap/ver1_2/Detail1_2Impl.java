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
package com.sun.xml.messaging.saaj.soap.ver1_2;

import java.util.logging.Logger;

import javax.xml.namespace.QName;
import jakarta.xml.soap.*;

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import org.w3c.dom.Element;

public class Detail1_2Impl extends DetailImpl {

    protected static final Logger log =
        Logger.getLogger(Detail1_2Impl.class.getName(),
                         "com.sun.xml.messaging.saaj.soap.ver1_2.LocalStrings");

    public Detail1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createSOAP12Name("Detail", prefix));
    }

    public Detail1_2Impl(SOAPDocumentImpl ownerDocument) {
        super(ownerDocument, NameImpl.createSOAP12Name("Detail"));
    }

    public Detail1_2Impl(SOAPDocumentImpl ownerDoc, Element domElement) {
        super(ownerDoc, domElement);
    }

    @Override
    protected DetailEntry createDetailEntry(Name name) {
        return new DetailEntry1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    @Override
    protected DetailEntry createDetailEntry(QName name) {
        return new DetailEntry1_2Impl(
            ((SOAPDocument) getOwnerDocument()).getDocument(),
            name);
    }

    /*
     * Override setEncodingStyle of ElementImpl to restrict adding encodingStyle
     * attribute to SOAP Detail (SOAP 1.2 spec, part 1, section 5.1.1)
     */
    @Override
    public void setEncodingStyle(String encodingStyle) throws SOAPException {        
        log.severe("SAAJ0403.ver1_2.no.encodingStyle.in.detail");
        throw new SOAPExceptionImpl("EncodingStyle attribute cannot appear in Detail");
    }

    @Override
    public SOAPElement addAttribute(Name name, String value)
        throws SOAPException {
        if (name.getLocalName().equals("encodingStyle")
            && name.getURI().equals(NameImpl.SOAP12_NAMESPACE)) {                
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    @Override
    public SOAPElement addAttribute(QName name, String value)
        throws SOAPException {
        if (name.getLocalPart().equals("encodingStyle")
            && name.getNamespaceURI().equals(NameImpl.SOAP12_NAMESPACE)) {                
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }
}
