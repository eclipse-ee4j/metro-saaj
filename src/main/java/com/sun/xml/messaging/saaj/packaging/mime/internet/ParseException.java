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
 * @(#)ParseException.java    1.3 02/03/27
 */



package com.sun.xml.messaging.saaj.packaging.mime.internet;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

/**
 * The exception thrown due to an error in parsing RFC822 
 * or MIME headers
 *
 * @author John Mani
 */

public class ParseException extends MessagingException {

    /**
     * Constructs a ParseException with no detail message.
     */
    public ParseException() {
	super();
    }

    /**
     * Constructs a ParseException with the specified detail message.
     * @param s		the detail message
     */
    public ParseException(String s) {
	super(s);
    }
}
