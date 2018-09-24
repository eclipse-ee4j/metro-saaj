/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.net.URL;

import javax.activation.DataHandler;

import java.io.*;

import javax.xml.soap.*;

import com.sun.xml.messaging.soap.SOAPMessagePersister;

/**
 * This class demonstrates the use of SOAPMessagePersister sample
 */
public class SaveAndLoadMessage {

    public static void main(String[] args) throws Exception {
 
        SOAPMessagePersister persister = new SOAPMessagePersister();

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();        
        SOAPPart soapPart = msg.getSOAPPart();
        soapPart.setContentId("soapPart");
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Populate the message body
        SOAPBodyElement gltp =
            bdy.addBodyElement(envelope.createName(
                "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
        gltp.addChildElement(envelope.createName(
            "symbol", "ztrade", "http://wombat.ztrade.com"))
                .addTextNode("SUNW");

        // Add an atachment
        URL url = new URL("file", null, "message.xml");
        AttachmentPart ap = msg.createAttachmentPart(new DataHandler(url));
        ap.setContentType("text/xml");
        ap.setContentId("attachmentPart");
        msg.addAttachmentPart(ap);

        msg.saveChanges();
        
        // Save the message to a file
        persister.save(msg, "savedMsg.txt");
        System.out.println("Message saved to savedMsg.txt");
        
        // Load the message from the file
        SOAPMessage newMsg = persister.load("savedMsg.txt");
    }
}
