/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/*
 * $Id: ProcessingStates.java,v 1.5 2009-01-17 00:39:48 ramapulavarthi Exp $
 */



package com.sun.xml.soap;

/**
*
* @author SAAJ Development Team
*/
public class ProcessingStates {
    public static final ProcessingStates CONTINUE = new ProcessingStates();
    public static final ProcessingStates STOP = new ProcessingStates();
    public static final ProcessingStates HEADER_DONE = new ProcessingStates();
    public static final ProcessingStates FAULT = new ProcessingStates();
    
    private ProcessingStates() {
    }
}
