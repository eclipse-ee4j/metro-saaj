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
package com.sun.xml.messaging.saaj.soap.ver1_2;

import java.util.logging.Logger;

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;

import com.sun.xml.messaging.saaj.soap.*;
import com.sun.xml.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.messaging.saaj.util.XMLDeclarationParser;

public class SOAPPart1_2Impl extends SOAPPartImpl implements SOAPConstants{

    protected static final Logger log =
        Logger.getLogger(SOAPPart1_2Impl.class.getName(),
                         "com.sun.xml.messaging.saaj.soap.ver1_2.LocalStrings");

    public SOAPPart1_2Impl() {
        super();
    }

    public SOAPPart1_2Impl(MessageImpl message) {
        super(message);
    }
    
    @Override
    protected String getContentType() {
        return "application/soap+xml";
    }

    @Override
    protected Envelope createEmptyEnvelope(String prefix) throws SOAPException {
        return new Envelope1_2Impl(getDocument(), prefix, true, true);
    }

    @Override
    protected Envelope createEnvelopeFromSource() throws SOAPException {
        XMLDeclarationParser parser = lookForXmlDecl();
        Source tmp = source;
        source = null;
        EnvelopeImpl envelope = (EnvelopeImpl)EnvelopeFactory.createEnvelope(tmp, this);
        if (!envelope.getNamespaceURI().equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
            log.severe("SAAJ0415.ver1_2.msg.invalid.soap1.2");
            throw new SOAPException("InputStream does not represent a valid SOAP 1.2 Message");
        }

        if (parser != null) { //can be null if source was a DomSource and not StreamSource
            if (!omitXmlDecl) {
                envelope.setOmitXmlDecl("no");
                envelope.setXmlDecl(parser.getXmlDeclaration());
                envelope.setCharsetEncoding(parser.getEncoding());
            }
        }
        return envelope;

    }

    @Override
    protected SOAPPartImpl duplicateType() {
        return new SOAPPart1_2Impl();
    }

    @Override
    public String getSOAPNamespace() {
        return SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;
    }

}
