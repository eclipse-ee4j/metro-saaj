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

import java.io.ByteArrayOutputStream;
import java.net.URL;

import jakarta.activation.*;
import jakarta.xml.soap.*;

import junit.framework.TestCase;

/*
 * These code snippets taken from Krishna's test workspace.
 * The whole aim of the test is to create a message, and write it
 * out to a .msg file and write the mime headers to a .mh file
 *
 * Later use these to recreate the message and verify that the
 * original and the recreated messages are indeed the same.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */

public class MimeRecreateTest extends TestCase {

    private static final int MESSAGE_HEADERS = 0;
    private static final int MESSAGE_BYTES = 1;

    public SOAPMessage createMessage(byte[][] result) throws Exception {

        MessageBuilder mBuilder = new MessageBuilder();

        // Create a message factory.
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();
        SOAPBody bdy = envelope.getBody();

        SOAPBodyElement gltp
        = bdy.addBodyElement(envelope.createName("GetLastTradePrice",
        "ztrade",
        "http://wombat.ztrade.com"));

        gltp.addChildElement(envelope.createName("symbol",
        "ztrade",
        "http://wombat.ztrade.com"))
        .addTextNode("SUNW");

        URL url = getClass().getResource("data/attach1.xml");

        //-----This code is written as a work around for one problem.

        class XmlDataSource extends URLDataSource {
            public XmlDataSource(URL u) { super(u); }
            @Override
            public String getContentType() { return "text/xml"; }
        }
        DataSource xmlDataSource = new XmlDataSource(url);

        AttachmentPart ap = msg.createAttachmentPart(
        new DataHandler(xmlDataSource));

        /*
        AttachmentPart ap =
        msg.createAttachmentPart(new DataHandler(url));

        ap.setContentType("text/xml");
        */
         //-----

        msg.addAttachmentPart(ap);

        /*
        AttachmentPart ap = msg.createAttachmentPart();
        ap.setContent("<foo>hello</foo>","text/plain");
        msg.addAttachmentPart(ap);
        */

        msg.saveChanges();

        result[MESSAGE_HEADERS] = mBuilder.saveMimeHeaders(msg);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        result[MESSAGE_BYTES] = baos.toByteArray();
        baos.close();

        //System.out.println("\n\n------Original message----------");
        //msg.writeTo(System.out);

        return msg;
    }

    public void testMessageRecreation() {
        try {
            byte[][] result = new byte[2][];
            SOAPMessage originalMsg = createMessage(result);
            MessageBuilder mBuilder = new MessageBuilder();
            SOAPMessage newMsg = mBuilder.constructMessage(result[0], result[1]);

            //System.out.println("\n\n------Recreated message---------");
            //newMsg.writeTo(System.out);

            assertTrue(
                    "Messages must match",
                    mBuilder.verifyMessage(originalMsg, newMsg));


        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should have been thrown");
        }

    }

    public static void main(String[] argv) {

        junit.textui.TestRunner.run(MimeRecreateTest.class);

    }

}
