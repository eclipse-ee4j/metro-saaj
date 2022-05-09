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

import java.io.*;
import jakarta.activation.*;
import com.sun.xml.messaging.saaj.packaging.mime.internet.MimeMultipart;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

public class MultipartDataContentHandler implements DataContentHandler {
    private ActivationDataFlavor myDF = new ActivationDataFlavor(
	    com.sun.xml.messaging.saaj.packaging.mime.internet.MimeMultipart.class,
	    "multipart/mixed", 
	    "Multipart");

    public MultipartDataContentHandler() {}

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    @Override
    public ActivationDataFlavor[] getTransferDataFlavors() { // throws Exception;
	return new ActivationDataFlavor[] { myDF };
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ds The DataSource
     * @return String object
     */
    @Override
    public Object getTransferData(ActivationDataFlavor df, DataSource ds) {
	// use myDF.equals to be sure to get ActivationDataFlavor.equals,
	// which properly ignores Content-Type parameters in comparison
	if (myDF.equals(df))
	    return getContent(ds);
	else
	    return null;
    }
    
    /**
     * Return the content.
     *
     * @param ds The DataSource
     * @return content
     */
    @Override
    public Object getContent(DataSource ds) {
	try {
	    return new MimeMultipart(
                ds, new ContentType(ds.getContentType())); 
	} catch (Exception e) {
	    return null;
	}
    }
    
    /**
     * Write the object to the output stream, using the specific MIME type.
     */
    @Override
    public void writeTo(Object obj, String mimeType, OutputStream os)
			throws IOException {
	if (obj instanceof MimeMultipart) {
	    try {
                //TODO: temporarily allow only ByteOutputStream
                // Need to add writeTo(OutputStream) on MimeMultipart
                ByteOutputStream baos = null; 
                if (os instanceof ByteOutputStream) {
                    baos = (ByteOutputStream)os;
                } else {
                    throw new IOException("Input Stream expected to be a com.sun.xml.messaging.saaj.util.ByteOutputStream, but found " + 
                        os.getClass().getName());
                }
		((MimeMultipart)obj).writeTo(baos);
	    } catch (Exception e) {
		throw new IOException(e.toString());
	    }
	}
    }
}

