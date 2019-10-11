/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.File;
import java.io.IOException;

public class SaveAndLoadTest {

    public static final String SYMBOL = "ORAW";

    @Test
    void reloadTest() throws IOException, SOAPException {
        File tempFile = new File(
                System.getProperty("buildDirectory", new File("").getAbsolutePath()),
                "savedMsg.txt"
        );
        SOAPMessage msg = SaveAndLoadMessage.saveAndLoad(tempFile, SYMBOL);
        NodeList symbolNodeList = msg.getSOAPBody().getElementsByTagNameNS("http://wombat.ztrade.com", "symbol");
        Assertions.assertEquals(1, symbolNodeList.getLength());
        Assertions.assertEquals(SYMBOL, symbolNodeList.item(0).getTextContent());
    }
}
