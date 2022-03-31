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

import com.sun.xml.messaging.saaj.SOAPExceptionImpl;

public class SOAPVersionMismatchException extends SOAPExceptionImpl {

    private static final long serialVersionUID = -3681495393703865847L;

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with no
     * reason or embedded <code>Throwable</code> object.
     */
    public SOAPVersionMismatchException() {
        super();
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with the given
     * <code>String</code> as the reason for the exception being thrown.
     *
     * @param reason a description of what caused the exception
     */
    public SOAPVersionMismatchException(String reason) {
        super(reason);
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with the given
     * <code>String</code> as the reason for the exception being thrown
     * and the given <code>Throwable</code> object as an embedded
     * exception.
     *
     * @param reason a description of what caused the exception
     * @param cause a <code>Throwable</code> object that is to
     *        be embedded in this <code>SOAPExceptionImpl</code> object
     */
    public SOAPVersionMismatchException(String reason, Throwable cause) {
        super(reason, cause);
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object initialized
     * with the given <code>Throwable</code> object.
     *
     * @param cause a <code>Throwable</code> object that is to
     *        be embedded in this <code>SOAPExceptionImpl</code> object
     */
    public SOAPVersionMismatchException(Throwable cause) {
        super(cause);
    }
}
