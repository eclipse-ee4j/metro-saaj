/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package book.receiver;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.*;

import com.sun.xml.messaging.soap.server.SAAJServlet;

/**
 * Sample servlet that receives messages.
 *
 * @author Krishna Meduri (krishna.meduri@sun.com)
 */

public class ReceivingServlet extends SAAJServlet
{

    static Logger
        logger = Logger.getLogger("Samples/Book");

    // This is the application code for handling the message.. Once the
    // message is received the application can retrieve the soap part, the
    // attachment part if there are any, or any other information from the
    // message.

    public SOAPMessage onMessage(SOAPMessage message) {
        System.out.println("On message called in receiving servlet");
        try {
            System.out.println("Here's the message: ");
            message.writeTo(System.out);

	    SOAPHeader header = message.getSOAPHeader();
	    SOAPBody body = message.getSOAPBody();

	    Iterator headerElems = header.examineMustUnderstandHeaderElements(
						"http://saaj.sample/receiver");

	    while(headerElems.hasNext()) {
		SOAPHeaderElement elem = (SOAPHeaderElement) headerElems.next();
                String actor = elem.getActor();
                boolean mu = elem.getMustUnderstand();
                Iterator elemChildren = elem.getChildElements();

		System.out.println("\n**************");
		System.out.println("actor and mu are " + actor + " " + mu);
                while(elemChildren.hasNext()) {
                    SOAPElement elem1 = (SOAPElement)elemChildren.next();
                    org.w3c.dom.Node child = elem1.getFirstChild();
                    String childValue = child.getNodeValue();
		    System.out.println("childValue is " + childValue);
		}
	    }

            return message;

        } catch(Exception e) {
            logger.log(
                Level.SEVERE,
                "Error in processing or replying to a message", 
                e);
            return null;
        }
    }
}

