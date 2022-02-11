/*
 * Copyright (c) 1997, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package mime;

import jakarta.activation.DataHandler;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class StartParameterTest extends TestCase {

    public StartParameterTest(String name) {
        super(name);
        String  mimeOpt = System.getProperty("saaj.mime.optimization");
        if (mimeOpt != null && "false".equals(mimeOpt)) {
           System.setProperty("saaj.mime.multipart.ignoremissingendboundary", "true");
        }
    }

    public void changeAndSaveMimeHeaders(SOAPMessage msg, String fileName)
    throws IOException {

        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        Hashtable hashTable = new Hashtable();
        MimeHeaders mimeHeaders = msg.getMimeHeaders();
        Iterator iterator = mimeHeaders.getAllHeaders();

        while(iterator.hasNext()) {
            MimeHeader mimeHeader = (MimeHeader) iterator.next();
            if(mimeHeader.getName().equals("Content-Type"))
                hashTable.put(mimeHeader.getName(),
                              mimeHeader.getValue()
                              + "; start=attachmentPart");
            else
                hashTable.put(mimeHeader.getName(), mimeHeader.getValue());
        }

        oos.writeObject(hashTable);
        oos.flush();
        oos.close();

        fos.flush();
        fos.close();
    }

    public SOAPMessage getModifiedMessage(String mimeHdrsFile, String msgFile)
    throws Exception {
        SOAPMessage message;

        MimeHeaders mimeHeaders = new MimeHeaders();
        FileInputStream fis = new FileInputStream(msgFile);

        ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(mimeHdrsFile));
        Hashtable hashTable = (Hashtable) ois.readObject();
        ois.close();

        if(hashTable.isEmpty())
            fail("MimeHeaders Hashtable is empty");
        else {
            for(int i=0; i < hashTable.size(); i++) {
                Enumeration keys = hashTable.keys();
                Enumeration values = hashTable.elements();
                while (keys.hasMoreElements() && values.hasMoreElements()) {
                    String name = (String) keys.nextElement();
                    String value = (String) values.nextElement();
                    mimeHeaders.addHeader(name, value);
                }
            }
        }

        MessageFactory messageFactory = MessageFactory.newInstance();
        message = messageFactory.createMessage(mimeHeaders, fis);

        message.saveChanges();

        return message;
    }

    public void testStartParameter() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();
        sp.setContentId("soapPart");

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(
                       envelope.createName("GetLastTradePrice", "ztrade",
				"http://wombat.ztrade.com"));

        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                       "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attach an xml file containing empty Body message
        URL url = new URL("file", null, "src/test/resources/mime/data/message.xml");

        AttachmentPart ap = msg.createAttachmentPart(new DataHandler(url));

        ap.setContentType("text/xml");
        ap.setContentId("attachmentPart");

        msg.addAttachmentPart(ap);

        msg.saveChanges();

        File f = new File("target/test-out/message.txt");
        f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        f.createNewFile();
        FileOutputStream sentFile =
                new FileOutputStream(f);
        msg.writeTo(sentFile);
        sentFile.close();
        changeAndSaveMimeHeaders(msg, "target/test-out/headers.txt");

        SOAPMessage newMsg =
            getModifiedMessage("target/test-out/headers.txt",
                               "target/test-out/message.txt");
        assertFalse("newMsg has an empty body",
                    newMsg.getSOAPBody().getChildElements().hasNext());
        assertTrue("Soap part has the Content-Id: attachmentPart",
                   newMsg.getSOAPPart()
                   .getContentId().equals("attachmentPart"));
        assertTrue("Attachment part has the Content-Id: soapPart",
                   ((AttachmentPart) newMsg.getAttachments().next())
                   .getContentId().equals("soapPart"));
    }

    public void testSampleMessageOne() throws Exception {
        MimeHeaders hdrs = new MimeHeaders();
        hdrs.addHeader("Server","WebSphere Application Server/5.1");
        hdrs.addHeader("Content-Type","multipart/related; " +
                       "type=\"text/xml\"; " +
                       "start=\"<139912840220.1065629194743.IBM.WEBSERVICES@ibm-7pr28r4m35k>\";         " +
                       "boundary=\"----=_Part_4_910054940.1065629194743\"");
        hdrs.addHeader("Content-Language","en-US");
        hdrs.addHeader("Connection","close");

        FileInputStream fis =
            new FileInputStream("src/test/resources/mime/data/msg.txt");
        MessageFactory factory =  MessageFactory.newInstance();
        SOAPMessage msg = factory.createMessage(hdrs,fis);
        String[] s = msg.getSOAPPart().getMimeHeader("Content-Description");
    }

    public void testContentDescription() throws Exception {
        MimeHeaders hdrs = new MimeHeaders();
        hdrs.addHeader("Server","WebSphere Application Server/5.1");
        hdrs.addHeader("Content-Type","multipart/related; " +
                       "type=\"text/xml\"; " +
                       "start=\"<139912840220.1065629194743.IBM.WEBSERVICES@ibm-7pr28r4m35k>\";         " +
                       "boundary=\"----=_Part_4_910054940.1065629194743\"");
        hdrs.addHeader("Content-Language","en-US");
        hdrs.addHeader("Connection","close");

        FileInputStream fis =
            new FileInputStream("src/test/resources/mime/data/msg.txt");
        MessageFactory factory =  MessageFactory.newInstance();
        try {
            factory.createMessage(hdrs,fis);
        } catch(Exception e) {
            fail("Exception should not be thrown " +
                 "while internalizing the message");
        }
    }

    public void testSampleMessageOneWithoutTypeParameter() throws Exception {
        MimeHeaders hdrs = new MimeHeaders();
        hdrs.addHeader("Server","WebSphere Application Server/5.1");
        hdrs.addHeader("Content-Type","multipart/related; " +
                       "start=\"<139912840220.1065629194743.IBM.WEBSERVICES@ibm-7pr28r4m35k>\";         " +
                       "boundary=\"----=_Part_4_910054940.1065629194743\"");
        hdrs.addHeader("Content-Language","en-US");
        hdrs.addHeader("Connection","close");

        FileInputStream fis =
            new FileInputStream("src/test/resources/mime/data/msg.txt");
        MessageFactory factory =  MessageFactory.newInstance();
        try {
            factory.createMessage(hdrs,fis);
        } catch(Exception e) {
            fail("Exception should not be thrown " +
                 "while internalizing the message");
        }
    }

    public void testSampleMessageTwo() throws Exception {
        MimeHeaders hdrs = new MimeHeaders();
        hdrs.addHeader("Server","WebSphere Application Server/5.1");
        hdrs.addHeader("Content-Type","multipart/related; " +
                       "type=\"text/xml\"; " +
                       "start=\"<1071294019496.1066069460327.IBM.WEBSERVICES@ibm-7pr28r4m35k>\";         " +
                       "boundary=\"----=_Part_1_807283631.1066069460327\"");
        hdrs.addHeader("Content-Language","en-US");
        hdrs.addHeader("Connection","close");

        FileInputStream fis =
            new FileInputStream("src/test/resources/mime/data/msg2.txt");
        MessageFactory factory =  MessageFactory.newInstance();
        factory.createMessage(hdrs,fis);
    }
}
