/*
 * Copyright (c) 2019, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package classfile;

import com.sun.xml.messaging.saaj.soap.SOAPDocument;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lukas
 */
public class ByteCodeVersionTest {

    public ByteCodeVersionTest() {
    }

    @Test
    public void testModuleInfoByteCodeVersion() {
        // module-info for JDK 9
        verifyClassFileFormat("/module-info.class", 0x35);
    }

    @Test
    public void testClassByteCodeVersion() {
        // class files for JDK 8
        verifyClassFileFormat("/com/sun/xml/messaging/saaj/soap/SOAPDocument.class", 0x34);
    }
    
    private static void verifyClassFileFormat(String resource, int expectedClassVersion) {
        try (InputStream in = SOAPDocument.class.getModule().getResourceAsStream(resource);
                DataInputStream data = new DataInputStream(in)) {
            if (0xCAFEBABE != data.readInt()) {
                Assert.fail("invalid header");
            }
            // minor
            int i = data.readUnsignedShort();
            // major
            i = data.readUnsignedShort();
            Assert.assertEquals("Class Files compiled for wrong java runtime version", expectedClassVersion, i);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
