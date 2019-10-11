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
 * @(#)UniqueValue.java       1.6 02/03/27
 */



package com.sun.xml.messaging.saaj.packaging.mime.internet;


/**
 * This is a utility class that generates unique values. The generated
 * String contains only US-ASCII characters and hence is safe for use
 * in RFC822 headers. <p>
 *
 * This is a package private class.
 *
 * @author John Mani
 * @author Max Spivak
 * @author Bill Shannon
 */

class UniqueValue {
    /**
     * A global part number.  Access is not synchronized because the
     * value is only one part of the unique value and so doesn't need
     * to be accurate.
     */
    private static int part = 0;

    /**
     * Get a unique value for use in a multipart boundary string.
     *
     * This implementation generates it by concatenating a global
     * part number, a newly created object's <code>hashCode()</code>,
     * and the current time (in milliseconds).
     */
    public static String getUniqueBoundaryValue() {
	StringBuilder s = new StringBuilder();

	// Unique string is ----=_Part_<part>_<hashcode>.<currentTime>
	s.append("----=_Part_").append(part++).append("_").
	  append(s.hashCode()).append('.').
	  append(System.currentTimeMillis());
	return s.toString();
    }
}
