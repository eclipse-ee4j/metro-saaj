/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.activation.ActivationDataFlavor;
import jakarta.activation.DataContentHandler;
import jakarta.activation.DataSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.transform.EfficientStreamingTransformer;

/**
 * JAF data handler for XML content
 *
 * @author Anil Vijendran
 */
public class XmlDataContentHandler implements DataContentHandler {

    public XmlDataContentHandler() {
    }

    /**
     * return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    @Override
    public ActivationDataFlavor[] getTransferDataFlavors() {
        ActivationDataFlavor[] flavors = new ActivationDataFlavor[2];

        flavors[0] =
            new ActivationDataFlavor(StreamSource.class, "text/xml", "XML");
        flavors[1] =
            new ActivationDataFlavor(StreamSource.class, "application/xml", "XML");

        return flavors;
    }

    /**
     * return the Transfer Data of type DataFlavor from InputStream
     * @param flavor The DataFlavor.
     * @param dataSource The DataSource.
     * @return The constructed Object.
     */
    @Override
    public Object getTransferData(ActivationDataFlavor flavor, DataSource dataSource)
        throws IOException {
        if (flavor.getMimeType().startsWith("text/xml") || 
                flavor.getMimeType().startsWith("application/xml")) {
            if (StreamSource.class.getName().equals(flavor.getRepresentationClass().getName())) {
                return new StreamSource(dataSource.getInputStream());
            }
        }
        return null;
    }

    /**
     *
     */
    @Override
    public Object getContent(DataSource dataSource) throws IOException {
        return new StreamSource(dataSource.getInputStream());
    }

    /**
     * construct an object from a byte stream
     * (similar semantically to previous method, we are deciding
     *  which one to support)
     */
    @Override
    public void writeTo(Object obj, String mimeType, OutputStream os)
        throws IOException {
        if (!mimeType.startsWith("text/xml") && !mimeType.startsWith("application/xml"))
            throw new IOException(
                "Invalid content type \"" + mimeType + "\" for XmlDCH");

            
        try {
            Transformer transformer = EfficientStreamingTransformer.newTransformer();
            StreamResult result = new StreamResult(os);
            if (obj instanceof DataSource) {
                // Streaming transform applies only to javax.xml.transform.StreamSource 
                transformer.transform((Source) getContent((DataSource)obj), result);                
            } else {
                Source src=null;
                if (obj instanceof String) {
                     src= new StreamSource(new java.io.StringReader((String) obj));
                } else {
                    src=(Source) obj;
                }
                transformer.transform(src, result);
            }
        } catch (Exception ex) {
            throw new IOException(
                "Unable to run the JAXP transformer on a stream "
                    + ex.getMessage());
        }
    }
}
