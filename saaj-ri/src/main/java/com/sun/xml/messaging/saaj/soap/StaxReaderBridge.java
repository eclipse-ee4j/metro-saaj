/*
 * Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap;

import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.jvnet.staxex.util.XMLStreamReaderToXMLStreamWriter;

/**
 * StaxBridge builds Envelope using a XMLStreamReaderToXMLStreamWriter 
 *
 * @author shih-chang.chen@oracle.com
 */
public class StaxReaderBridge extends StaxBridge {
    private XMLStreamReader in;

    public StaxReaderBridge(XMLStreamReader reader, SOAPPartImpl soapPart) throws SOAPException {
        super(soapPart);
        in = reader;
        final String soapEnvNS = soapPart.getSOAPNamespace();
        breakpoint =  new XMLStreamReaderToXMLStreamWriter.Breakpoint(reader, saajWriter) {
                boolean seenBody = false;
                boolean stopedAtBody = false;
                @Override
                public boolean proceedBeforeStartElement()  {
                    if (stopedAtBody) return true;
                    if (seenBody) {
                        stopedAtBody = true;
                        return false;
                    }
                    if ("Body".equals(reader.getLocalName()) && soapEnvNS.equals(reader.getNamespaceURI()) ){
                        seenBody = true;
                    }
                    return true;
                }
            };
    }

    @Override
    public XMLStreamReader getPayloadReader() {
        return in;
    }

    @Override
    public QName getPayloadQName() {
        return (in.getEventType() == XMLStreamConstants.START_ELEMENT) ? in.getName() : null;
    }

    @Override
    public String getPayloadAttributeValue(String attName) {
        return (in.getEventType() == XMLStreamConstants.START_ELEMENT) ? in.getAttributeValue(null, attName) : null;
    }

    @Override
    public String getPayloadAttributeValue(QName attName) {
        return (in.getEventType() == XMLStreamConstants.START_ELEMENT) ? in.getAttributeValue(attName.getNamespaceURI(), attName.getLocalPart()) : null;
    }
}
