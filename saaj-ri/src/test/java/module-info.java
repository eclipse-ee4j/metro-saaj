/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
module com.sun.xml.messaging.saaj.test {
    requires jakarta.activation;
    requires transitive java.desktop;
    requires transitive java.xml.soap;
    requires transitive com.sun.xml.messaging.saaj;
    requires junit;
    
    exports mime.custom;
}
