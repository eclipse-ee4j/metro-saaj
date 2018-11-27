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
    requires java.xml.bind;
    requires org.jvnet.staxex;
    requires java.xml.soap;
    requires java.logging;
    requires java.desktop;
    requires java.xml;

    exports com.sun.xml.messaging.saaj;
    exports com.sun.xml.messaging.saaj.packaging.mime;
    exports com.sun.xml.messaging.saaj.packaging.mime.internet;
    exports com.sun.xml.messaging.saaj.packaging.mime.util;
    exports com.sun.xml.messaging.saaj.soap;
    exports com.sun.xml.messaging.saaj.soap.dynamic;
    exports com.sun.xml.messaging.saaj.soap.name;
    exports com.sun.xml.messaging.saaj.util;
    exports com.sun.xml.messaging.saaj.util.stax;
    exports com.sun.xml.messaging.saaj.util.transform;

    provides javax.xml.soap.MessageFactory
            with com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;
    provides javax.xml.soap.SAAJMetaFactory
            with com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl;
    provides javax.xml.soap.SOAPConnectionFactory
            with com.sun.xml.messaging.saaj.client.p2p.HttpSOAPConnectionFactory;
    provides javax.xml.soap.SOAPFactory
            with com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl;
}
