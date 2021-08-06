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
package com.sun.xml.messaging.saaj.soap.ver1_1;

import java.util.logging.Logger;

import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPException;
import javax.xml.transform.Source;

import com.sun.xml.messaging.saaj.soap.*;
import com.sun.xml.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.messaging.saaj.util.XMLDeclarationParser;

public class SOAPPart1_1Impl extends SOAPPartImpl implements SOAPConstants {

    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.ver1_1.LocalStrings");

    public SOAPPart1_1Impl() {
        super();
     }
    
    public SOAPPart1_1Impl(MessageImpl message) {
        super(message);
    }

    @Override
    protected String getContentType() {
        return isFastInfoset() ? "application/fastinfoset" : "text/xml";
    }

    @Override
    protected Envelope createEnvelopeFromSource() throws SOAPException {
        // Record the presence of xml declaration before the envelope gets
        // created.
        XMLDeclarationParser parser = lookForXmlDecl();
        Source tmp = source;
        source = null;
        EnvelopeImpl envelope =
            (EnvelopeImpl) EnvelopeFactory.createEnvelope(tmp, this);

        if (!envelope.getNamespaceURI().equals(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE)) {
            log.severe("SAAJ0304.ver1_1.msg.invalid.SOAP1.1");
            throw new SOAPException("InputStream does not represent a valid SOAP 1.1 Message");
        }

        if (parser != null && !omitXmlDecl) {
            envelope.setOmitXmlDecl("no");
            envelope.setXmlDecl(parser.getXmlDeclaration());
            envelope.setCharsetEncoding(parser.getEncoding());
        }
        return envelope;
    }

    @Override
    protected Envelope createEmptyEnvelope(String prefix)
        throws SOAPException {
        return new Envelope1_1Impl(getDocument(), prefix, true, true);
    }

    @Override
    protected SOAPPartImpl duplicateType() {
        return new SOAPPart1_1Impl();
    }

    @Override
    public String getSOAPNamespace() {
        return SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE;
    }

}
