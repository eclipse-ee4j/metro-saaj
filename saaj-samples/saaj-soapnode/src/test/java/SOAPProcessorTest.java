/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import com.sun.xml.soap.ProcessingContext;
import com.sun.xml.soap.SOAPAnnotator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import java.util.Collections;

public class SOAPProcessorTest {

    @Test
    void name() throws Exception {
        SOAPMessage message = SOAPProcessorSample.process(Collections.singletonList(new AssertingAnnotator()));
        Assertions.assertEquals(1,1);
    }

    static class AssertingAnnotator extends SOAPAnnotator{

        @Override
        public void annotateHeader(SOAPHeader header, ProcessingContext context) throws Exception {
            System.out.println();
        }

        @Override
        public void handleIncomingFault(ProcessingContext context) {

        }

        @Override
        public void handleOutgoingFault(ProcessingContext context) {

        }
    }
}
