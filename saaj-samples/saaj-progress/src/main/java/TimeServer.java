/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/*
 * $Id: TimeServer.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */


import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class TimeServer implements SOAPCallback {

	public static final int PORT = 12321;
	private SOAPListener httpServer;

	public TimeServer() {
		httpServer = new SOAPListenerImpl();
		start();
	}

	public void start() {
		try {
			httpServer.initMsgLoop(PORT, this);
			httpServer.startMsgLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onMessage(SOAPMessage msg) {
		MessageProcessor proc = new MessageProcessor(msg);
		// Start a new thread to process this client's request
		new Thread(proc, "Message Processor").start();
	}

	public static void main(String[] args) {
		TimeServer server = new TimeServer();
	}

	public class MessageProcessor implements Runnable {

		private SOAPMessage msg;

		public MessageProcessor(SOAPMessage msg) {
			this.msg = msg;
		}

		public void run() {
			try {
				System.out.println("Received Client Message...");
				msg.writeTo(System.out);
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope envelope = sp.getEnvelope();
				SOAPBody body = envelope.getBody();
				SOAPElement elem = (SOAPElement)body.getFirstChild().getFirstChild();

				String clientAddr = elem.getValue();
				String noOfTimes = ((SOAPElement)elem.getNextSibling()).getValue();
				int no = new Integer(noOfTimes).intValue();
				SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
				for(int i=0; i < no; i++) {
					SOAPConnection con = scf.createConnection();
					SOAPMessage msg = createMessage();
					System.out.println("Sending msg="+i+" to client at:"+clientAddr);
					SOAPMessage reply = con.call(msg, clientAddr);
					con.close();
					try {
						Thread.sleep(500);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (SOAPException se) {
				se.printStackTrace();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}

		public SOAPMessage createMessage() throws SOAPException {
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage msg = mf.createMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope envelope = sp.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPBodyElement gltp = body.addBodyElement(
				envelope.createName("GetTime", "time",
					"http://wombat.time.com"));
			gltp.addTextNode(DateTimeFormatter
					.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
					.withZone(ZoneId.systemDefault())
					.format(Instant.now()));
			return msg;
		}

	}

}
