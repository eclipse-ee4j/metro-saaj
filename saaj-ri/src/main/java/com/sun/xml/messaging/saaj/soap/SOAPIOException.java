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

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;

public class SOAPIOException extends IOException {

    private static final long serialVersionUID = -5636159039553977703L;

    SOAPExceptionImpl soapException;

    public SOAPIOException() {
        super();
        soapException = new SOAPExceptionImpl();
        soapException.fillInStackTrace();
    }

    public SOAPIOException(String s) {
        super();
        soapException = new SOAPExceptionImpl(s);
        soapException.fillInStackTrace();
    }

    public SOAPIOException(String reason, Throwable cause) {
        super();
        soapException = new SOAPExceptionImpl(reason, cause);
        soapException.fillInStackTrace();
    }

    public SOAPIOException(Throwable cause) {
        super(cause.toString());
        soapException = new SOAPExceptionImpl(cause);
        soapException.fillInStackTrace();
    }

    @Override
    public Throwable fillInStackTrace() {
        if (soapException != null) {
            soapException.fillInStackTrace();
        }
        return this;
    }

    @Override
    public String getLocalizedMessage() {
        return soapException.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return soapException.getMessage();
    }

    @Override
    public void printStackTrace() {
        soapException.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        soapException.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        soapException.printStackTrace(s);
    }

    @Override
    public String toString() {
        return soapException.toString();
    }

}
