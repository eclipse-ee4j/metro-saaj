/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public interface LazyEnvelope extends Envelope {
    public XMLStreamReader getPayloadReader() throws SOAPException;
    public boolean isLazy();
    public void writeTo(XMLStreamWriter writer) throws XMLStreamException, SOAPException;
    
    /**
     * Retrieve payload qname without materializing its contents
     * @return QName
     * @throws SOAPException in case of an error
     */
    public QName getPayloadQName() throws SOAPException;
    
    /**
     * Retrieve payload attribute value without materializing its contents
     * @param localName local name
     * @return payload attribute value
     * @throws SOAPException in case of an error
     */
    public String getPayloadAttributeValue(String localName) throws SOAPException;
    
    /**
     * Retrieve payload attribute value without materializing its contents
     * @param qName QName
     * @return payload attribute value
     * @throws SOAPException in case of an error
     */
    public String getPayloadAttributeValue(QName qName) throws SOAPException;
}
