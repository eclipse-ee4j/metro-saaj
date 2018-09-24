/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Reference Implementation of JSR-67: SOAP with Attachments API for Java.
 */
module com.sun.xml.messaging.saaj {
    requires org.jvnet.mimepull;
    requires transitive java.xml.bind;
    requires org.jvnet.staxex;
    requires transitive java.xml.soap;
    requires java.logging;
    requires java.desktop;

    exports com.sun.xml.messaging.saaj;
    exports com.sun.xml.messaging.saaj.soap;
}
