/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/*
 * @(#)QDecoderStream.java    1.5 02/03/27
 */



package com.sun.xml.messaging.saaj.packaging.mime.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class implements a Q Decoder as defined in RFC 2047
 * for decoding MIME headers. It subclasses the QPDecoderStream class.
 * 
 * @author John Mani
 */

public class QDecoderStream extends QPDecoderStream {

    /**
     * Create a Q-decoder that decodes the specified input stream.
     * @param in        the input stream
     */
    public QDecoderStream(InputStream in) {
	super(in);
    }

    /**
     * Read the next decoded byte from this input stream. The byte
     * is returned as an <code>int</code> in the range <code>0</code>
     * to <code>255</code>. If no byte is available because the end of
     * the stream has been reached, the value <code>-1</code> is returned.
     * This method blocks until input data is available, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
	int c = in.read();

	if (c == '_') // Return '_' as ' '
	    return ' ';
	else if (c == '=') {
	    // QP Encoded atom. Get the next two bytes ..
	    ba[0] = (byte)in.read();
	    ba[1] = (byte)in.read();
	    // .. and decode them
	    try {
		return ASCIIUtility.parseInt(ba, 0, 2, 16);
	    } catch (NumberFormatException nex) {
		throw new IOException("Error in QP stream " + nex.getMessage());
	    }
	} else
	    return c;
    }
}
