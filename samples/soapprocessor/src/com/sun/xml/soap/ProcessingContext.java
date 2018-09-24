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
 * $Id: ProcessingContext.java,v 1.5 2009-01-17 00:39:47 ramapulavarthi Exp $
 */



package com.sun.xml.soap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SAAJ RI Development Team
 */
public class ProcessingContext {
    protected Map properties = new HashMap();
    
    public ProcessingContext() {
        setProperty(SOAPProcessorConstants.STATE_PROPERTY, ProcessingStates.CONTINUE);
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public void removeProperty(String name) {
        properties.remove(name);
    }

    public boolean containsProperty(String name) {
        return properties.containsKey(name);
    }

    public java.util.Iterator getPropertyNames() {
        return properties.keySet().iterator();
    }

}
