/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util;


import org.xml.sax.SAXException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;


/**
 * Pool of SAXParser objects
 */
public class ParserPool {
    private final BlockingQueue<SAXParser> queue;
    private SAXParserFactory factory;

    public ParserPool(int capacity) {
        queue = new ArrayBlockingQueue<SAXParser>(capacity);
        factory = SAXParserFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", SAAJUtil.getSystemClassLoader());
        try {
            factory.setFeature("jdk.xml.resetSymbolTable", true);
        } catch(SAXException | ParserConfigurationException e) {
        }
        factory.setNamespaceAware(true);
        for (int i = 0; i < capacity; i++) {
           try {
                queue.put(factory.newSAXParser());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (SAXException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public SAXParser get() throws ParserConfigurationException,
		SAXException {

        try {
            return (SAXParser) queue.take();
        } catch (InterruptedException ex) {
            throw new SAXException(ex);
        }

    }

    public boolean put(SAXParser parser) {
        return queue.offer(parser);
    }
    
    public void returnParser(SAXParser saxParser) {
        saxParser.reset();
        put(saxParser);
    }

}
