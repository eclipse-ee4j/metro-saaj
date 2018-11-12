/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Test to reproduce and hopefully fix the namespace bug.
 *
 * 1. xmlns should not be allowed to be redefined.
 * 2. Two attributes with same name cannot exist in an element. 
 *    The attribute names are not namespace qualified, so the 
 *    exact names would have to match.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */
package namespace;

import junit.framework.*;

public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        
        // testing the fix made locally in our implementation
        // throwing exception before sending it to dom4j
        
        suite.addTestSuite(NamespaceTest.class);
        suite.addTestSuite(NSDeclTest.class);     
        suite.addTestSuite(DefaultNamespaceTest.class);     
        suite.addTestSuite(GetNamespaceURITest.class);     
        return suite;
    }
}
