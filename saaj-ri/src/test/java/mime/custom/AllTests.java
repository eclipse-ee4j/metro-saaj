/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * 
 */

package mime.custom;

import junit.framework.*;

/**
 *
 * Handling custom objects using custom data content handlers
 *
 * @author Jitendra Kotamraju (jitendra.kotamraju@sun.com)
 */
public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(CustomTypeTest.class);
        return suite;
    }
}
