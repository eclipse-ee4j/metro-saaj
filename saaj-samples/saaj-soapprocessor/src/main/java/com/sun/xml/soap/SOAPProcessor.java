/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.soap;

import java.util.Vector;
import javax.xml.soap.*;

public abstract class SOAPProcessor {

    protected Vector recipients = new Vector();
    protected Vector annotators = new Vector();

    /**
	 * adds a <code>SOAPRecipient</code> to this <code>SOAPProcessor</code>
	 * 
	 * @param recipient
	 *            The <code>SOAPRecipient</code> to be added
	 */
    public void addRecipient(SOAPRecipient recipient) {
        recipients.addElement(recipient);
    }

    /**
	 * adds a <code>SOAPAnnotator</code> to this <code>SOAPProcessor</code>
	 * 
	 * @param annotator
	 *            The <code>SOAPAnnotator</code> to be added
	 */
    public void addAnnotator(SOAPAnnotator annotator) {
        annotators.addElement(annotator);
    }

    /**
	 * Processes an incoming <code>SOAPMessage</code> according to the SOAP
	 * Processing model given in the appropriate SOAP specification. The
	 * message is passed through one or more <code>SOAPRecipient</code> s
	 * 
	 * @param message
	 *            the <code>SOAPMessage</code> to be processed
	 * 
	 * @return the <code>SOAPMessage</code> after processing //todo
	 * @exception SOAPException
	 *                if there is an error accepting the message
	 */

    public abstract SOAPMessage acceptMessage(SOAPMessage message)
        throws Exception;

    /**
	 * Processes an outgoing <code>SOAPMessage</code> through one or more
	 * <code>SOAPAnnotator</code>s.
	 * 
	 * @param message
	 *            the <code>SOAPMessage</code> to be processed
	 * 
	 * @return the <code>SOAPMessage</code> after processing //todo
	 * @exception SOAPException
	 *                if there is an error while preparing the message
	 */
    public abstract SOAPMessage prepareMessage(SOAPMessage message)
        throws Exception;

    protected abstract String getRoleAttributeValue(SOAPHeaderElement element);
}
