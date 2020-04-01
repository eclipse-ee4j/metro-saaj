/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class ProgressTest {

    private static Validator responseValidator;

    @BeforeAll
    static void setUp() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source responseSchemaSource = new StreamSource(ProgressTest.class.getResourceAsStream("progress-response.xsd"));
            Source wombatSchemaSource = new StreamSource(ProgressTest.class.getResourceAsStream("wombat-time.xsd"));
            Schema schema = factory.newSchema(new Source[]{wombatSchemaSource, responseSchemaSource});
            responseValidator = schema.newValidator();
        } catch (SAXException e) {
            throw new RuntimeException("Unable to prepare XSD validator", e);
        }
    }

    @Test
    void validateMessages() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> {
            System.out.println("Starting server");
            TimeServer.main(new String[0]);
        });
        executor.execute(() -> {
            System.out.println("Starting client");
            Client client = new Client() {
                @Override
                public void onMessage(SOAPMessage msg) {
                    try {
                        responseValidator.validate(msg.getSOAPPart().getContent());
                    } catch (SAXException | IOException | SOAPException e) {
                        e.printStackTrace();
                        Assertions.fail("Error when validating response" + e.getMessage());
                    }
                    super.onMessage(msg);
                    if (msgNo >= 5) {
                        System.out.println("Its been 5 messages, end of the test");
                        executor.shutdownNow();
                    }
                }
            };
            try {
                client.sendMessage();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(20, TimeUnit.SECONDS);
    }
}
