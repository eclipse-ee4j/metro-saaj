/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.sun.xml.messaging.saaj.LazyEnvelopeSource;
import org.jvnet.staxex.util.XMLStreamReaderToXMLStreamWriter;


/**
 * StaxBridge builds Envelope from LazyEnvelopeSource
 *
 * @author shih-chang.chen@oracle.com
 */
public class StaxLazySourceBridge extends StaxBridge {
    private LazyEnvelopeSource lazySource;

    public StaxLazySourceBridge(LazyEnvelopeSource src, SOAPPartImpl soapPart) throws SOAPException {
        super(soapPart);
        lazySource = src;
        final String soapEnvNS = soapPart.getSOAPNamespace();
        try {
            breakpoint = new XMLStreamReaderToXMLStreamWriter.Breakpoint(src.readToBodyStarTag(), saajWriter) {
                @Override
                public boolean proceedAfterStartElement()  {
                    if ("Body".equals(reader.getLocalName()) && soapEnvNS.equals(reader.getNamespaceURI()) ){
                        return false;
                    } else
                        return true;
                }
            };
        } catch (XMLStreamException e) {
            throw new SOAPException(e);
        }
    }

    @Override
    public XMLStreamReader getPayloadReader() {
        return lazySource.readPayload();
//		throw new UnsupportedOperationException();
    }

    @Override
    public QName getPayloadQName() {
        return lazySource.getPayloadQName();
    }

    @Override
    public String getPayloadAttributeValue(String attName) {
        if (lazySource.isPayloadStreamReader()) {
            XMLStreamReader reader = lazySource.readPayload();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                return reader.getAttributeValue(null, attName);
            }
        }
        return null;
    }

    @Override
    public String getPayloadAttributeValue(QName attName) {
        if (lazySource.isPayloadStreamReader()) {
            XMLStreamReader reader = lazySource.readPayload();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                return reader.getAttributeValue(attName.getNamespaceURI(), attName.getLocalPart());
            }
        }
        return null;
    }

        @Override
    public void bridgePayload() throws XMLStreamException {
        //Assuming out is at Body
        writePayloadTo(saajWriter);
    }

    public void writePayloadTo(XMLStreamWriter writer) throws XMLStreamException {
        lazySource.writePayloadTo(writer);
    }
}
