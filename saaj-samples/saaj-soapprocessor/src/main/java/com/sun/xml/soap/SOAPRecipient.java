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

import java.util.Iterator;
import java.util.Vector;

import jakarta.xml.soap.SOAPHeaderElement;
import javax.xml.namespace.QName;

public abstract class SOAPRecipient implements ProcessingFaultHandler {

    protected Vector roles = new Vector();
    protected Vector supportedHeaders = new Vector();

    public void addRole(String role) {
        roles.addElement(role);
    }

    public Iterator getRoles() {
        return roles.iterator();
    }

    public boolean supportsRole(String role) {
        for (Iterator it = roles.iterator(); it.hasNext();) {
            String thisRole = (String) it.next();
            if (role.equals(thisRole))
                return true;
        }
        return false;
    }

    public void addHeader(QName header) {
        supportedHeaders.addElement(header);
    }

    public QName[] getHeaders() {
        QName[] returnValue = new QName[supportedHeaders.size()];
        return (QName[]) supportedHeaders.toArray(returnValue);
    }

    public boolean supportsHeader(QName header) {
        for (Iterator it = supportedHeaders.iterator(); it.hasNext();) {
            QName thisHeader = (QName) it.next();
            if (header.equals(thisHeader))
                return true;
        }
        return false;
    }

    public abstract void acceptHeaderElement(SOAPHeaderElement element, ProcessingContext context)
        throws Exception;
    
}
