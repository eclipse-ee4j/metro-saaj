/*
 * Copyright (c) 1997, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/*
 * $Id: UddiPing.java,v 1.5 2009-01-17 00:39:50 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:50 $
 */


import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

public class UddiPing {

    public static void main(String[] args) {
        try {

            if (args.length != 2)  {
                System.err.println("Usage: UddiPing http://localhost:8080/juddiv3/services/inquiryv2 \"UDDI Inquiry REST Service\"");
                System.exit(1);
            }

            // Create the connection and the message factory.
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = scf.createConnection();
            MessageFactory msgFactory = MessageFactory.newInstance();

            // Create a message
            SOAPMessage msg = msgFactory.createMessage();

            // Create an envelope in the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            // Get hold of the the body
            SOAPBody body = envelope.getBody();

            body.addChildElement(envelope.createName("find_service", "uddi",
                                                     "urn:uddi-org:api_v2"))
                .addAttribute(envelope.createName("generic"), "2.0")
                .addAttribute(envelope.createName("maxRows"), "100")
                    .addChildElement(envelope.createName("name", "uddi", "urn:uddi-org:api_v2"))
                .addTextNode(args[1]);

            URL endpoint = new URL(args[0]);

            msg.saveChanges();

            System.out.println("\n----------- Request Message ----------\n");
            prettyPrint(msg,System.out);

            SOAPMessage reply = connection.call(msg, endpoint);

            System.out.println("Received reply from: "+endpoint);

            System.out.println("\n----------- Reply Message ----------\n");
            prettyPrint(reply,System.out);

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void prettyPrint(SOAPMessage msg, PrintStream printStream) throws IOException, SOAPException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(msg.getSOAPPart().getContent(),new StreamResult(printStream));
        } catch (Exception e) {
            e.printStackTrace();
            msg.writeTo(System.out);
        }
    }
}






