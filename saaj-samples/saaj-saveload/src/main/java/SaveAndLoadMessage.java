/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;

import jakarta.activation.DataHandler;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

import com.sun.xml.messaging.soap.SOAPMessagePersister;

/**
 * This class demonstrates the use of SOAPMessagePersister sample
 */
public class SaveAndLoadMessage {

    public static void main(String[] args) throws Exception {
        File persistedFile = new File("savedMsg.txt");

        if(args.length > 0){
            persistedFile = new File(args[0]);
        }

        SOAPMessage soapMessage = saveAndLoad(persistedFile,"SUNW");
    }

    static SOAPMessage saveAndLoad(File persistedFile, String symbol) throws SOAPException, IOException {
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
                .addTextNode(symbol);

        // Add an atachment
        URL url = SaveAndLoadMessage.class.getResource("message.xml");

        AttachmentPart ap = msg.createAttachmentPart(new DataHandler(url));
        ap.setContentType("text/xml");
        ap.setContentId("attachmentPart");
        msg.addAttachmentPart(ap);

        msg.saveChanges();

        // Save the message to a file
        persister.save(msg, persistedFile);
        System.out.println("Message saved to savedMsg.txt");

        // Load the message from the file
        return persister.load(persistedFile.getAbsolutePath());
    }
}
