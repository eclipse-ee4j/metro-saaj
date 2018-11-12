/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap;

import java.io.*;
import java.util.logging.Logger;

import javax.xml.soap.*;
import javax.xml.stream.XMLStreamReader;

import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ParseException;
import com.sun.xml.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.messaging.saaj.soap.ver1_1.Message1_1Impl;
import com.sun.xml.messaging.saaj.soap.ver1_2.Message1_2Impl;
import com.sun.xml.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.messaging.saaj.util.TeeInputStream;

/**
 * A factory for creating SOAP messages.
 *
 * Converted to a placeholder for common functionality between SOAP
 * implementations.
 *
 * @author Phil Goodwin (phil.goodwin@sun.com)
 */
public class MessageFactoryImpl extends MessageFactory {

    protected static final Logger log =
        Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                         "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected  OutputStream listener;

    protected boolean lazyAttachments = false;
    
    public  OutputStream listen(OutputStream newListener) {
        OutputStream oldListener = listener;
        listener = newListener;
        return oldListener;
    }
    
    @Override
    public SOAPMessage createMessage() throws SOAPException {
        throw new UnsupportedOperationException();
    }
    
    public SOAPMessage createMessage(String protocol) throws SOAPException {
    	if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol))
    		return new com.sun.xml.messaging.saaj.soap.ver1_1.Message1_1Impl();
    	else
    		return new com.sun.xml.messaging.saaj.soap.ver1_2.Message1_2Impl();
    }

    public SOAPMessage createMessage(boolean isFastInfoset, 
        boolean acceptFastInfoset) throws SOAPException 
    {
        throw new UnsupportedOperationException();
    }
    
    public SOAPMessage createMessage(MimeHeaders headers, XMLStreamReader reader) throws SOAPException, IOException {
        String contentTypeString = MessageImpl.getContentType(headers);

        if (listener != null) {
            throw new SOAPException("Listener OutputStream is not supported with XMLStreamReader");
        }

        try {
            ContentType contentType = new ContentType(contentTypeString);
            int stat = MessageImpl.identifyContentType(contentType);

            if (MessageImpl.isSoap1_1Content(stat)) {
                return new Message1_1Impl(headers,contentType,stat,reader);
            } else if (MessageImpl.isSoap1_2Content(stat)) {
                return new Message1_2Impl(headers,contentType,stat,reader);
            } else {
                log.severe("SAAJ0530.soap.unknown.Content-Type");
                throw new SOAPExceptionImpl("Unrecognized Content-Type");
            }
        } catch (ParseException e) {            
            log.severe("SAAJ0531.soap.cannot.parse.Content-Type");
            throw new SOAPExceptionImpl(
                "Unable to parse content type: " + e.getMessage());
        }
    }
    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in)
        throws SOAPException, IOException {
        String contentTypeString = MessageImpl.getContentType(headers);

        if (listener != null) {
            in = new TeeInputStream(in, listener);
        }

        try {
            ContentType contentType = new ContentType(contentTypeString);
            int stat = MessageImpl.identifyContentType(contentType);

            if (MessageImpl.isSoap1_1Content(stat)) {
                return new Message1_1Impl(headers,contentType,stat,in);
            } else if (MessageImpl.isSoap1_2Content(stat)) {
                return new Message1_2Impl(headers,contentType,stat,in);
            } else {
                log.severe("SAAJ0530.soap.unknown.Content-Type");
                throw new SOAPExceptionImpl("Unrecognized Content-Type");
            }
        } catch (ParseException e) {            
            log.severe("SAAJ0531.soap.cannot.parse.Content-Type");
            throw new SOAPExceptionImpl(
                "Unable to parse content type: " + e.getMessage());
        }
    }

    protected static final String getContentType(MimeHeaders headers) {
        String[] values = headers.getHeader("Content-Type");
        if (values == null)
            return null;
        else
            return values[0];
    }

    public void setLazyAttachmentOptimization(boolean flag) {
        lazyAttachments = flag;
    }

}
