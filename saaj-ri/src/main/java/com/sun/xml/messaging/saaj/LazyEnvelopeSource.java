/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * LazyEnvelopeSource provides the source to create lazy Envelope
 *
 * @author shih-chang.chen@oracle.com
 */
public interface LazyEnvelopeSource extends javax.xml.transform.Source {
    /**
     * Retrieve payload qname without materializing its contents
     * @return payload QName
     */
    public QName getPayloadQName();
    public XMLStreamReader readToBodyStarTag() throws XMLStreamException;
    public XMLStreamReader readPayload();
    public void writePayloadTo(XMLStreamWriter writer)throws XMLStreamException;
    public boolean isPayloadStreamReader();
}
