/*
 * Copyright (c) 2011, 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 *
 * @author vbkumarjayanti
 */
public final class SAAJUtil {

    public static boolean getSystemBoolean(String arg) {
        try {
            return Boolean.getBoolean(arg);
        } catch (AccessControlException ex) {
            return false;
        }
    }

    public static Integer getSystemInteger(String arg) {
        try {
            return Integer.getInteger(arg);
        } catch (SecurityException ex) {
            return null;
        }
    }

    public static String getSystemProperty(String arg) {
        try {
            return System.getProperty(arg);
        } catch (SecurityException ex) {
            return null;
        }
    }

    public static ClassLoader getSystemClassLoader() {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
}
