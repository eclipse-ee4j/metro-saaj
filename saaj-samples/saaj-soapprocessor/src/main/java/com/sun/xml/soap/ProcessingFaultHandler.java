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
 * $Id: ProcessingFaultHandler.java,v 1.5 2009-01-17 00:39:47 ramapulavarthi Exp $
 */



package com.sun.xml.soap;

/**
 * @author XWS-Security Development Team
 */
public interface ProcessingFaultHandler {
    public void handleIncomingFault(ProcessingContext context);
    public void handleOutgoingFault(ProcessingContext context);
}
