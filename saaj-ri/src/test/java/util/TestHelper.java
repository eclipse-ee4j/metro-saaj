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
 * @author Edwin Goei
 */
package util;

import com.sun.xml.messaging.saaj.util.SAAJUtil;

import java.io.*;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

public class TestHelper {
    private static final TestHelper INSTANCE = new TestHelper();

    /** Debug level.  A value of 0 means don't write any output. */
    private int debug;

    /**
     * OutputStream for debug output.  We need this b/c SAAJ writeTo method
     * takes OutputStream-s and not PrintWriter-s.
     */
    private OutputStream ostream = System.err;

    /** PrintWriter for debug output */
    private PrintWriter pw = new PrintWriter(ostream, true);

    public static TestHelper getInstance() {
        return INSTANCE;
    }

    private TestHelper() {
        try {
            debug = Integer.parseInt(System.getProperty("saaj.debug"));
        } catch (NumberFormatException x) {
            // assert(no such property defined or bad string)
            debug = 0;
        }
    }

    /**
     * @param dataId data resource identifier
     * @return an InputStream to the data
     * @throws Exception if resource is not found
     */
    public InputStream getInputStream(String dataId) throws Exception {
        InputStream is = getClass().getResourceAsStream("/resources/" + dataId);
        if (is == null) {
            throw new Exception("Resource not found: " + dataId);
        }
        return is;
    }

    /**
     * @return true if debug level is > 0
     */
    public boolean isDebug() {
        return debug > 0;
    }

    public void println(String s) {
        if (debug == 0) {
            return;
        }
        pw.println(s);
    }

    public void println() {
        if (debug == 0) {
            return;
        }
        pw.println();
    }

    public void print(String s) {
        if (debug == 0) {
            return;
        }
        pw.print(s);
    }

    /**
     * @param msg the SOAPMessage whose envelope to dump out
     * @throws Exception
     */
    public void dumpEnvelope(SOAPMessage msg) throws Exception {
        if (debug == 0) {
            return;
        }
        SOAPPart sp = msg.getSOAPPart();
        Source source = sp.getContent();
        TransformerFactory tf = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
        Transformer xform = tf.newTransformer();
        println("==== TestHelper.dumpEnvelope(...) Start ====");
        xform.transform(source, new StreamResult(pw));
        println();
        println("==== TestHelper.dumpEnvelope(...) End ====");
    }

    /**
     * Output similar to SOAPMessage.writeTo().
     *
     * @param msg the SOAPMessage to dump out
     * @throws Exception
     */
    public void writeTo(SOAPMessage msg) throws Exception {
        if (debug == 0) {
            return;
        }
        println("==== TestHelper.writeTo(...) Start ====");
        msg.writeTo(ostream);
        println();
        println("==== TestHelper.writeTo(...) End ====");
    }
}
