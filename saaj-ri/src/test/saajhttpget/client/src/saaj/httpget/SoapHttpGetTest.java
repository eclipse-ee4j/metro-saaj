/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package saaj.httpget;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.soap.*;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class SoapHttpGetTest {
    
    public static void main(String args[]) throws Exception {

        try {

            // Create a SOAP connection
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection con = scf.createConnection();

            String to = System.getProperty("to");
            System.out.println("Sending resuest to : " + to);
	
	    SOAPMessage reply = con.get(new URL(to));
            if (!(reply.getSOAPPart().getEnvelope() instanceof 
                 com.sun.xml.messaging.saaj.soap.ver1_1.Envelope1_1Impl)) {
                 throw new Exception("expected a 1.1 message");
            }

            try {
	        reply = con.get(new URL(to));
                throw new Exception("Should throw an error");
            } catch (SOAPException e) {

            }

	    reply = con.get(new URL(to));
            if (!(reply.getSOAPPart().getEnvelope() instanceof 
                 com.sun.xml.messaging.saaj.soap.ver1_2.Envelope1_2Impl)) {
                 throw new Exception("expected a 1.2 message");
            }
            SOAPEnvelope env = reply.getSOAPPart().getEnvelope();

	} catch(Exception e) {
            e.printStackTrace();
	}
    }
}
