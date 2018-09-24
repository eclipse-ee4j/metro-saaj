/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util;

import java.io.*;

import javax.xml.transform.stream.StreamSource;


/**
 *
 * @author Anil Vijendran
 */
public class JAXMStreamSource extends StreamSource {
    InputStream in;
    Reader reader;
    private static final boolean lazyContentLength;
    static {
        lazyContentLength = SAAJUtil.getSystemBoolean("saaj.lazy.contentlength");
    }
    public JAXMStreamSource(InputStream is) throws IOException {
        if (lazyContentLength) {
            in = is;
        } else if (is instanceof ByteInputStream) {
            this.in = (ByteInputStream) is;
        } else {
            ByteOutputStream bout = null;
            try {
                bout = new ByteOutputStream();
                bout.write(is);
                this.in = bout.newInputStream();
            } finally {
                if (bout != null)
                    bout.close();
            }
        }
    }

    public JAXMStreamSource(Reader rdr) throws IOException {

        if (lazyContentLength) {
            this.reader = rdr;
            return;
        }
        CharArrayWriter cout = new CharArrayWriter();
        char[] temp = new char[1024];
        int len;
                                                                                
        while (-1 != (len = rdr.read(temp)))
            cout.write(temp, 0, len);
                                                                                
        this.reader = new CharArrayReader(cout.toCharArray(), 0, cout.size());
    }

    @Override
    public InputStream getInputStream() {
	return in;
    }
    
    @Override
    public Reader getReader() {
	return reader;
    }

    public void reset() throws IOException {
	    if (in != null)
		in.reset();
	    if (reader != null)
		reader.reset();
    }
}
