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
 * @(#)Header.java    1.3 02/03/27
 */



package com.sun.xml.messaging.saaj.packaging.mime;


/**
 * The Header class stores a name/value pair to represent headers.
 *
 * @author John Mani
 */

public interface Header {

    /**
     * Returns the name of this header.
     *
     * @return 		name of the header
     */
    String getName();

    /**
     * Returns the value of this header.
     *
     * @return 		value of the header
     */
    String getValue();
}
