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
 * $Id: SOAPProcessorFactory.java,v 1.5 2009-01-17 00:39:48 ramapulavarthi Exp $
 */



package com.sun.xml.soap;

/**
*
* @author SAAJ RI Development Team
*/
public class SOAPProcessorFactory {
    public static SOAPProcessor createSOAPProcessor() {
        return new SOAPProcessorImpl();
    }
}
