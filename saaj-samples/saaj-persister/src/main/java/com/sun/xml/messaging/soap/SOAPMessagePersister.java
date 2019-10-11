/*
 * Copyright (c) 1997, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.soap;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.Iterator;

/** 
 * This class provides a method <code>save</code> for saving a
 * <code>SOAPMessage</code> to a file and a method <code>load</code> for
 * loading a saved <code>SOAPMessage</code> from a file (in which the
 * <code>SOAPMessage</code> was saved using the <code>save</code> method of
 * this class).
 * The format of the saved <code>SOAPMessage</code> is human readable.
 */
public class SOAPMessagePersister {

    /**
     * Saves a given <code>SOAPMessage</code> to a given file location in a
     * human readable format.
     */
    public void save(SOAPMessage msg, File location)
        throws IOException, SOAPException {

        FileWriter writer = new FileWriter(location);

        // Write all the message Mime headers
        msg.saveChanges();
        Iterator iterator = msg.getMimeHeaders().getAllHeaders();
        while(iterator.hasNext()) {
            MimeHeader mimeHeader = (MimeHeader) iterator.next();
            writer.write(mimeHeader.getName() + ": " +
                         mimeHeader.getValue() + "\n");
        }
        writer.write("\n");

        // If there are no attachments
        if(msg.countAttachments() == 0) {

            // Write a boundary to mark the beginning of the soap-part
            writer.write("=--soap-part--=\n");

            // Write the Mime headers of the soap-part
            Iterator it = msg.getSOAPPart().getAllMimeHeaders();
            while(it.hasNext()) {
                MimeHeader mimeHeader = (MimeHeader) it.next();
                writer.write(mimeHeader.getName() + ": " +
                         mimeHeader.getValue() + "\n");
            }    
            writer.write("\n");
        }

        // Do a writeTo() of the message
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        writer.write(baos.toString());

        // If there were no attachments write the boundary to mark the end of
        // the soap-part
        if(msg.countAttachments() == 0)
            writer.write("\n=--soap-part--=\n");

        writer.flush();
        writer.close();
    }

    /**
     * Loads a <code>SOAPMessage</code> from a given file location.
     * The message in the given file location should have been stored using
     * the <code>save</code> method of this class.
     */
    public SOAPMessage load(String location)
        throws IOException, SOAPException {
      
        FileReader reader = new FileReader(location);

        // Read the Mime headers of the message
        MimeHeaders hdrs = new MimeHeaders();
        String headerLine = readLine(reader);
        while(!headerLine.equals("")) {

            int colonIndex = headerLine.indexOf(':');
            String headerName = headerLine.substring(0, colonIndex);
            String headerValue = headerLine.substring(colonIndex + 1);
            hdrs.addHeader(headerName, headerValue);
            headerLine = readLine(reader);
        }

        // If there are no attachments
        if(readLine(reader).equals("=--soap-part--=")) {

            // Read the Mime headers of soap-part
            MimeHeaders soapPartHdrs = new MimeHeaders();
            headerLine = readLine(reader);
            while(!headerLine.equals("")) {
                int colonIndex = headerLine.indexOf(':');
                String headerName = headerLine.substring(0, colonIndex);
                String headerValue = headerLine.substring(colonIndex + 1);
                soapPartHdrs.addHeader(headerName, headerValue);
                headerLine = readLine(reader);
            }

            // Read the content of the soap-part in a StringBuffer
            StringBuffer soapPartBuffer = new StringBuffer();
            String soapPartLine = readLine(reader);
            while(!soapPartLine.equals("=--soap-part--=")) {
                soapPartBuffer.append(soapPartLine);
                soapPartBuffer.append('\n');
                soapPartLine = readLine(reader);
            }
            soapPartBuffer.deleteCharAt(soapPartBuffer.length() - 1);

            // Create a new SOAPMessage
            SOAPMessage msg = MessageFactory.newInstance().createMessage(
                hdrs, new StringBufferInputStream(soapPartBuffer.toString()));

            // Set the Mime headers of its soap-part
            SOAPPart soapPart = msg.getSOAPPart();
            soapPart.removeAllMimeHeaders();
            for(Iterator it = soapPartHdrs.getAllHeaders(); it.hasNext(); ) {
                MimeHeader hdr = (MimeHeader) it.next();
                soapPart.addMimeHeader(hdr.getName(), hdr.getValue());
            }

            msg.saveChanges();

            reader.close();

            // Return message
            return msg;
        }

        reader.close();

        // When there are no attachments, pass the FileInputStream of the
        // saved message to CreateMessage method. This works because the parser
        // ignores everything before the first part boundary
        return MessageFactory.newInstance().createMessage(
            hdrs, new FileInputStream(location));
    }

    private String readLine(FileReader reader) throws IOException {

        StringBuffer buffer = new StringBuffer();
        char ch = (char) reader.read();
        while(ch != '\n') {
            buffer.append(ch);
            ch = (char) reader.read();
        }
        return buffer.toString();      
    }
}
