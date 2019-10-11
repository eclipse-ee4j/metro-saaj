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

import com.sun.xml.messaging.saaj.util.stax.SaajStaxWriter;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jvnet.staxex.util.XMLStreamReaderToXMLStreamWriter;


/**
 * StaxBridge builds Envelope using a XMLStreamReaderToXMLStreamWriter 
 *
 * @author shih-chang.chen@oracle.com
 */
public abstract class StaxBridge {
	protected SaajStaxWriter saajWriter;
	protected XMLStreamReaderToXMLStreamWriter readerToWriter;
	protected XMLStreamReaderToXMLStreamWriter.Breakpoint breakpoint;
	
	
	public StaxBridge(SOAPPartImpl soapPart) throws SOAPException {
		readerToWriter = new XMLStreamReaderToXMLStreamWriter();
		saajWriter = new SaajStaxWriter(soapPart.message, soapPart.getSOAPNamespace());
	}

	public void bridgeEnvelopeAndHeaders() throws XMLStreamException {
		readerToWriter.bridge(breakpoint);
	}
	
	public void bridgePayload() throws XMLStreamException {
		readerToWriter.bridge(breakpoint);
	}

    abstract public XMLStreamReader getPayloadReader();

    abstract public QName getPayloadQName();
    
    abstract public String getPayloadAttributeValue(String attName) ;

    abstract public String getPayloadAttributeValue(QName attName) ;
}
