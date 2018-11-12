/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.xml.soap.SOAPException;

/**
 * An exception that signals that a SOAP exception has occurred. A
 * <code>SOAPExceptionImpl</code> object may contain a <code>String</code>
 * that gives the reason for the exception, an embedded
 * <code>Throwable</code> object, or both. This class provides methods
 * for retrieving reason messages and for retrieving the embedded
 * <code>Throwable</code> object.
 *
 * <P> Typical reasons for throwing a <code>SOAPExceptionImpl</code>
 * object are problems such as difficulty setting a header, not being
 * able to send a message, and not being able to get a connection with
 * the provider.  Reasons for embedding a <code>Throwable</code>
 * object include problems such as input/output errors or a parsing
 * problem, such as an error in parsing a header.
 */
public class SOAPExceptionImpl extends SOAPException {
    private Throwable cause;

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with no
     * reason or embedded <code>Throwable</code> object.
     */
    public SOAPExceptionImpl() {
        super();
	this.cause = null;
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object with the given
     * <code>String</code> as the reason for the exception being thrown.
     *
     * @param reason a description of what caused the exception
     */
    public SOAPExceptionImpl(String reason) {
        super(reason);
	this.cause = null;
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
    public SOAPExceptionImpl(String reason, Throwable cause) {
       super (reason);
       initCause(cause);
    }

    /**
     * Constructs a <code>SOAPExceptionImpl</code> object initialized
     * with the given <code>Throwable</code> object.
     * @param cause cause
     */
    public SOAPExceptionImpl(Throwable cause) {
	super (cause.toString());
	initCause(cause);
    }

    /**
     * Returns the detail message for this <code>SOAPExceptionImpl</code>
     * object.
     * <P>
     * If there is an embedded <code>Throwable</code> object, and if the 
     * <code>SOAPExceptionImpl</code> object has no detail message of its 
     * own, this method will return the detail message from the embedded 
     * <code>Throwable</code> object.
     *
     * @return the error or warning message for this 
     *         <code>SOAPExceptionImpl</code> or, if it has none, the 
     *         message of the embedded <code>Throwable</code> object,
     *         if there is one
     */
    @Override
    public String getMessage() {
   	String message = super.getMessage ();
	if (message == null && cause != null) {
	    return cause.getMessage();	
	} else {
	    return message;
	}
    }

    /**
     * Returns the <code>Throwable</code> object embedded in this 
     * <code>SOAPExceptionImpl</code> if there is one. Otherwise, this method 
     * returns <code>null</code>.
     *
     * @return the embedded <code>Throwable</code> object or <code>null</code> 
     *         if there is none
     */

    @Override
    public Throwable getCause() {
	return cause;
    }

    /**
     * Initializes the <code>cause</code> field of this <code>SOAPExceptionImpl</code> 
     * object with the given <code>Throwable</code> object.
     * <P>
     * This method can be called at most once.  It is generally called from
     * within the constructor or immediately after the constructor has
     * returned a new <code>SOAPExceptionImpl</code> object.
     * If this <code>SOAPExceptionImpl</code> object was created with the
     * constructor {@link #SOAPExceptionImpl(Throwable)} or
     * {@link #SOAPExceptionImpl(String,Throwable)}, meaning that its
     * <code>cause</code> field already has a value, this method cannot be 
     * called even once.
     *
     * @param  cause the <code>Throwable</code> object that caused this
     *         <code>SOAPExceptionImpl</code> object to be thrown.  The value of this
     *         parameter is saved for later retrieval by the
     *         {@link #getCause()} method.  A {@code null} value is
     *         permitted and indicates that the cause is nonexistent or
     *         unknown.
     * @return  a reference to this <code>SOAPExceptionImpl</code> instance
     * @throws IllegalArgumentException if <code>cause</code> is this
     *         <code>Throwable</code> object.  (A <code>Throwable</code> object
     *         cannot be its own cause.)
     * @throws IllegalStateException if this <code>SOAPExceptionImpl</code> object
     *         was created with {@link #SOAPExceptionImpl(Throwable)} or
     *         {@link #SOAPExceptionImpl(String,Throwable)}, or this 
     *         method has already been called on this <code>SOAPExceptionImpl</code>
     *         object
     */
    @Override
    public synchronized Throwable initCause(Throwable cause)
    {
   	if(this.cause != null) {
	    throw new IllegalStateException("Can't override cause");
	}
	if(cause == this) {
	    throw new IllegalArgumentException("Self-causation not permitted");	
	}
	this.cause = cause;

	return this;
    }
    
    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if (cause != null) {
            System.err.println("\nCAUSE:\n");
            cause.printStackTrace();
        }
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.println("\nCAUSE:\n");
            cause.printStackTrace(s);
        }
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (cause != null) {
            s.println("\nCAUSE:\n");
            cause.printStackTrace(s);
        }
    }
}
