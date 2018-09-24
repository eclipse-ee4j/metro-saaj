/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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



import java.io.FileInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.soap.*;

public class UddiPing {

    public static void main(String[] args) {
        try {

            if (args.length != 2)  {
                System.err.println("Usage: UddiPing properties-file business-name");
                System.exit(1);
            }


            Properties myprops = new Properties();
            myprops.load(new FileInputStream(args[0]));


            Properties props = System.getProperties();

            Enumeration it = myprops.propertyNames();
            while (it.hasMoreElements()) {
                String s = (String) it.nextElement();
                props.put(s, myprops.getProperty(s));
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
  
            body.addChildElement(envelope.createName("find_service", "",
                                                     "urn:uddi-org:api_v2"))
                .addAttribute(envelope.createName("generic"), "2.0")
                .addAttribute(envelope.createName("maxRows"), "100")
                .addChildElement(envelope.createName("name"))
                .addTextNode(args[1]);

            URL endpoint
                = new URL(System.getProperties().getProperty("URL"));

            msg.saveChanges();

            System.out.println("\n----------- Request Message ----------\n");
            msg.writeTo(System.out);
            
            SOAPMessage reply = connection.call(msg, endpoint);

            System.out.println("Received reply from: "+endpoint);

            System.out.println("\n----------- Reply Message ----------\n");
            reply.writeTo(System.out);

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}






