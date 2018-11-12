/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package soap12;

import junit.framework.*;

public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(MetaFactoryTest.class);
        suite.addTestSuite(SOAPElementTests.class);
        suite.addTestSuite(SOAPFaultTests.class);
        suite.addTestSuite(SOAPHeaderTests.class);
        return suite;
    }
}
