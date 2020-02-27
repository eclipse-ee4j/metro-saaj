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
 * $Id: Client.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */


import java.io.IOException;
import java.net.InetAddress;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class Client implements SOAPCallback {

	private static final int PORT = 12322;
	private static final int NO_MSGS = 5;
	private SOAPListener httpServer;
	protected int msgNo = 0;
	
	public Client() {
		httpServer = new SOAPListenerImpl();
		start();
	}
	
	public void start() {
		try {
			httpServer.initMsgLoop(PORT, this);
			httpServer.startMsgLoopInThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onMessage(SOAPMessage msg) {
		try {
			++msgNo;
			System.out.println("Received message ..."+msgNo);
			msg.writeTo(System.out);
			System.out.println();
			if (msgNo >= NO_MSGS) {
				httpServer.stopMsgLoopInThread();
			}
		} catch (SOAPException se) {
			se.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public void sendMessage() throws Exception {
		SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
		SOAPConnection con = scf.createConnection();
		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage msg = mf.createMessage();
		SOAPPart sp = msg.getSOAPPart();
		SOAPEnvelope envelope = sp.getEnvelope();
		SOAPBody body = envelope.getBody();
		SOAPBodyElement gltp = body.addBodyElement(envelope.createName(
			"GetTime", "time", "http://wombat.time.com"));
		String addr = "http://"+InetAddress.getLocalHost().getHostAddress()+":"
			+PORT+"/index.html";
		gltp.addChildElement(envelope.createName("addr", "time",
			"http://wombat.time.com")).addTextNode(addr);
		gltp.addChildElement(envelope.createName("no", "time",
			"http://wombat.time.com")).addTextNode(""+NO_MSGS);
/*		
		String xml = "<START><A>Hello World</A></START>";
		StringReader rdr = new StringReader(xml);
		StreamSource source = new StreamSource(rdr);  
		AttachmentPart ap = msg.createAttachmentPart(source, "text/xml");
		msg.addAttachmentPart(ap);
*/
		
		String url = "http://"+InetAddress.getLocalHost().getHostAddress()+":"
			+TimeServer.PORT+"/index.html";
		System.out.println("Sending SOAP message to ="+url);
		SOAPMessage reply = con.call(msg, url);		
		con.close();
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.sendMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
