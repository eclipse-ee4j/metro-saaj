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

package soap;

import junit.framework.*;

/**
 * Test suite that contains test cases to unit test SOAP 1.2 features and restrictions.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 *
 */
public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(SOAPStructureTest.class);
        suite.addTestSuite(FaultTest.class);
        suite.addTestSuite(SOAPPartTest.class);
        suite.addTestSuite(QNameTest.class);
        suite.addTestSuite(DetailTest.class);
        suite.addTestSuite(SetElementNameTest.class);
        suite.addTestSuite(ExtractContentAsDocumentTest.class);
        suite.addTestSuite(WriteXmlDeclarationTest.class);
        suite.addTestSuite(XmlDeclarationUtf16Test.class);
        suite.addTestSuite(SourceResetTest.class);
        suite.addTestSuite(GetChildElementsTest.class);

        return suite;
    }
}
