/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package mime.custom;

import jakarta.activation.ActivationDataFlavor;
import jakarta.activation.DataContentHandler;
import jakarta.activation.DataSource;

import java.io.IOException;
import java.io.OutputStream;

/**
 * DataContentHandler for Content-Type : custom/mailcap
 *
 * @author Jitendra Kotamraju (jitendra.kotamraju@sun.com)
 */
public class MailcapDataContentHandler implements DataContentHandler {
   
    private static ActivationDataFlavor myDF =
        new ActivationDataFlavor(
            mime.custom.CustomType.class,
            "custom/mailcap",
            "Custom Type");

    protected ActivationDataFlavor getDF() {
        return myDF;
    }

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    @Override
    public ActivationDataFlavor[] getTransferDataFlavors() { // throws Exception;
        return new ActivationDataFlavor[] { getDF() };
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ds The InputStream corresponding to the data
     * @return String object
     */
    @Override
    public Object getTransferData(ActivationDataFlavor df, DataSource ds)
        throws IOException {
        // use myDF.equals to be sure to get ActivationDataFlavor.equals,
        // which properly ignores Content-Type parameters in comparison
        if (getDF().equals(df))
            return getContent(ds);
        else
            return null;
    }

    @Override
    public Object getContent(DataSource ds) throws IOException {
        return new CustomType();
    }

    /**
     * Write the object to the output stream, using the specified MIME type.
     */
    @Override
    public void writeTo(Object obj, String type, OutputStream os)
        throws IOException {
        if (!(obj instanceof CustomType)) {             
            throw new IOException(
                    getDF().getMimeType()
                    + " DataContentHandler requires CustomType object, "
                    + "instead got "
                    + obj.getClass().toString());
        }
		os.write('M');
		os.flush();
    }
}
