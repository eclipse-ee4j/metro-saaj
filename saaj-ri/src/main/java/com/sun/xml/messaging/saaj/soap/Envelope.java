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

import java.io.IOException;
import java.io.OutputStream;

import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import javax.xml.transform.Source;

/**
 * Different implementations for SOAP Envelope must all implement this
 * interface.
 *
 * @author Anil Vijendran (akv@eng.sun.com)
 */
public interface Envelope extends SOAPEnvelope {
    /**
     * Get the content as a JAXP Source.
     *
     * @return source
     */
    Source getContent();

    /**
     * Output the content.
     *
     * @param out output stream.
     * @exception IOException in case of an I/O error.
     */
    void output(OutputStream out) throws IOException;
    
    /**
     * Output the content.
     *
     * @param out output stream
     * @param isFastInfoset true if it is fast infoset.
     * @exception IOException in case of an I/O error.
     */
    void output(OutputStream out, boolean isFastInfoset) throws IOException;
    
    void setStaxBridge(StaxBridge bridge) throws SOAPException;
    
    StaxBridge getStaxBridge() throws SOAPException;
}
