/*
 * Copyright (c) 1997, 2014 Oracle and/or its affiliates. All rights reserved.
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

import static com.sun.xml.messaging.saaj.soap.MessageImpl.SAAJ_MIME_SOAP_BODY_PART_SIZE_LIMIT;
import static java.lang.String.format;


/**
 *
 * @author Anil Vijendran
 */
public class JAXMStreamSource extends StreamSource {

    private static final Integer soapBodyPartSizeLimit;

    InputStream in;
    Reader reader;
    private static final boolean lazyContentLength;
    static {
        lazyContentLength = SAAJUtil.getSystemBoolean("saaj.lazy.contentlength");
        soapBodyPartSizeLimit = SAAJUtil.getSystemInteger(SAAJ_MIME_SOAP_BODY_PART_SIZE_LIMIT);
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
                ByteInputStream byteInputStream = bout.newInputStream();
                if (soapBodyPartSizeLimit != null && byteInputStream.getCount() > soapBodyPartSizeLimit) {
                    throw new IOException(format("SOAP body part of size %s exceeded size limitation: %s", byteInputStream.getCount(), soapBodyPartSizeLimit));
                }
                this.in = byteInputStream;
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
