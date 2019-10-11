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
 * $Id: SOAPListenerImpl.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.*;
import java.util.Iterator;
import java.util.Set;

import javax.xml.soap.*;

public class SOAPListenerImpl implements SOAPListener {

	private Selector selector;
	private SOAPCallback callback;
	private ServerSocketChannel serverChannel;
	private Thread startThread;

	private static final String HTTP_OK = 
		"HTTP/1.1 200 OK\r\n"+
		"Content-Length: 0\r\n"+
		"Content-Type: text/xml\r\n"+
		"\r\n";

	public void initMsgLoop(int port, SOAPCallback callback)
		throws IOException {

		this.callback = callback;
		selector = SelectorProvider.provider().openSelector();
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		InetAddress lh = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(lh, port );
		serverChannel.socket().bind(isa);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	public void startMsgLoop() throws IOException, ClosedChannelException,
		InterruptedException {
			
		while(selector.select() > 0) {
			Set keys = selector.selectedKeys();
			Iterator i = keys.iterator();
			while(i.hasNext()) {
				SelectionKey key = (SelectionKey)i.next();
				if (key.isAcceptable()) {
					SocketChannel channel = serverChannel.accept();
					channel.configureBlocking(false);
					SelectionKey newKey = channel.register(selector,
						SelectionKey.OP_READ|SelectionKey.OP_WRITE);
					newKey.attach(new ChannelClosure(channel));
				} else if (key.isReadable()) {
					boolean close = readMessage((ChannelClosure)key.attachment());
					if (close) {
						key.channel().close();
						key.cancel();
					}
				}
			}
			keys.clear();
		}
	}

	public void startMsgLoopInThread() {
		startThread = new Thread(new Runnable() {
			public void run() {
				try {
					startMsgLoop();
				} catch(IOException ie) {
					ie.printStackTrace();
				} catch(InterruptedException ine) {
					ine.printStackTrace();
				}
			}
		});
		startThread.start();
	}

	public void stopMsgLoopInThread() {
		startThread.interrupt();
	}

	private boolean readMessage(ChannelClosure closure)
		throws IOException, InterruptedException {
		
		boolean close = false;
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		int nbytes = closure.getChannel().read(byteBuffer);
		byteBuffer.flip();
		String result = this.decode(byteBuffer);
		closure.append(result);
		if (closure.gotFullRequest()) {
			writeMessage(closure.getChannel(), HTTP_OK);
			callback.onMessage(closure.getMessage());
			close = true;
		}
		return close;
	}
	
	private String decode(ByteBuffer byteBuffer)
		throws CharacterCodingException {
			
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer = decoder.decode( byteBuffer );
		String result = charBuffer.toString();
		return result;
	}
	
	private void writeMessage(SocketChannel channel, String message)
		throws IOException {
			
		ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
		int nbytes = channel.write( buf );
	}

	public class ChannelClosure {
		private SocketChannel channel;
		private StringBuffer buffer;
		private SimpleHTTP http;

		public ChannelClosure( SocketChannel channel ) {
			this.channel = channel;
			this.buffer = new StringBuffer();
			this.http = new SimpleHTTP(buffer);
		}

		public void inputOver() throws IOException {
			System.out.println( this.buffer.toString() );
			writeMessage( this.channel, this.buffer.toString() );
			buffer = new StringBuffer();
		}

		public SocketChannel getChannel() {
			return this.channel;
		}

		public void append(String part) {
			buffer.append(part);
		}
		
		public boolean gotFullRequest() {
			http.updateRequest();
			return http.gotFullRequest();
		}
		
		public SOAPMessage getMessage() {
			return http.getContent();
		}
	}
	
	public class SimpleHTTP {
		private StringBuffer requestStr;
		private boolean gotFull;
		private int curIndex;
		private int lineBegIndex;
		private int lineEndIndex;
		private boolean gotHeaders;
		private MimeHeaders headers;
		private int contentLength = -1;
		private String xmlData;
		private String lastHdrAttr;
		private String lastHdrValue;
		private SOAPMessage msg;

		public SimpleHTTP(StringBuffer requestStr) {
			this.requestStr = requestStr;
			headers = new MimeHeaders();
			this.curIndex = 0;
		}
		
		public void updateRequest() {
			while(!gotHeaders && curIndex < requestStr.length()) {
				char ch = requestStr.charAt(curIndex);
				if (ch == '\n' && curIndex > 0 &&
					requestStr.charAt(curIndex-1) == '\r') {
						
					String hdr = requestStr.substring(lineBegIndex, curIndex-1);
					addHeaders(hdr);
					if (lineBegIndex+1 == curIndex) {
						gotHeaders = true;
					}
					lineBegIndex = curIndex+1;
				}
				curIndex++;
			}
			if (gotHeaders && requestStr.length() >= curIndex+contentLength) {
				this.gotFull = true;
				xmlData = requestStr.substring(curIndex, requestStr.length());
			}
		}
		
		public boolean gotFullRequest() {
			return gotFull;
		}
		
		public SOAPMessage getContent() {
			if (msg == null) {
				try {
					MessageFactory msgFactory = MessageFactory.newInstance();
					msg = msgFactory.createMessage(headers,
						new ByteArrayInputStream(xmlData.getBytes()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return msg;
		}
		
		private void addHeaders(String hdr) {
			int colonIndex = hdr.indexOf(':');
			if (colonIndex == -1) {
				if (lastHdrAttr == null) {
					return;
				}
				lastHdrValue += hdr;
//System.out.println("LastHdrAttr=<"+lastHdrAttr+">");
//System.out.println("LastHdrValu=<"+lastHdrValue+">");
				headers.setHeader(lastHdrAttr, lastHdrValue);
			} else {
				lastHdrAttr = hdr.substring(0, colonIndex);
				lastHdrValue = hdr.substring(colonIndex+2, hdr.length());
//System.out.println("LastHdrAttr=<"+lastHdrAttr+">");
//System.out.println("LastHdrValu=<"+lastHdrValue+">");
				headers.addHeader(lastHdrAttr, lastHdrValue);
			}
			if (lastHdrAttr.equalsIgnoreCase("Content-Length")) {
				contentLength = new Integer(lastHdrValue).intValue( );
			}
		}
	}

}
