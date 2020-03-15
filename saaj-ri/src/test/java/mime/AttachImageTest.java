/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package mime;

//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

/*
 * Attaches an image object and verifies whether it gets the image object back
 */
public class AttachImageTest extends TestCase {

//    private static final String IMAGE_GIF = "src/test/resources/mime/data/cup.gif";
    private static final String IMAGE_JPG = "src/test/resources/mime/data/cup.jpg";
    private static final String IMAGE_JPG_SENT = "target/test-out/cup_sent.jpg";

    public AttachImageTest(String name) {
        super(name);
    }

    public void testAddImageAndVerify() throws Exception {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(
                envelope.createName("GetLastTradePrice", "ztrade",
                        "http://wombat.ztrade.com"));

        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attach Image
        Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        AttachmentPart ap = msg.createAttachmentPart(img, "image/jpeg");
        AttachmentPart ap1 = msg.createAttachmentPart(img, "image/jpeg");
        msg.addAttachmentPart(ap);
        msg.addAttachmentPart(ap1);
        msg.saveChanges();

        File f = new File(IMAGE_JPG_SENT);
        f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        f.createNewFile();
        try ( // Save the soap message to file
                FileOutputStream sentFile = new FileOutputStream(f)) {
            msg.writeTo(sentFile);
        }

        try ( // See if we get the image object back
                FileInputStream fin = new FileInputStream(f)) {
            SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);

            SOAPElement elment = newMsg.getSOAPBody();
            newMsg.writeTo(new ByteArrayOutputStream());

            Iterator i = newMsg.getAttachments();
            while (i.hasNext()) {
                AttachmentPart att = (AttachmentPart) i.next();
                Object obj = att.getContent();
                if (!(obj instanceof Image)) {
                    fail("Didn't get the image type, instead got:" + obj.getClass());
                }
            }
        }
    }

    /*
    public void testAddGifImageAndVerify() throws Exception {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

		// Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(
			envelope.createName("GetLastTradePrice", "ztrade",
				"http://wombat.ztrade.com"));

        gltp.addChildElement(envelope.createName("symbol", "ztrade",
			"http://wombat.ztrade.com")).addTextNode("SUNW");

           System.out.println("Setting content via Image");

           Image image = Toolkit.getDefaultToolkit().createImage(IMAGE_GIF);

           AttachmentPart ap = msg.createAttachmentPart(image, "image/gif");
           ap.setContentType("image/gif");

           System.out.println("Created the IMAGE object OK");

            Object o = ap.getContent();
            System.out.println("Content type " + ap.getContentType());
            if (o!=null) {
                if (o instanceof Image) {
                    System.out.println("Image object was found");
                } else {
                    System.out.println("Unexpected object was found" + o);
                }
            } else {
                    System.out.println("null was returned");
            }

            msg.addAttachmentPart(ap);
            msg.saveChanges();
            //msg.writeTo(System.out);

    }
     */
    public void testGetAttachmentByHref() throws Exception {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(
                envelope.createName("GetLastTradePrice", "ztrade",
                        "http://wombat.ztrade.com"));

        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");
        gltp.setAttribute("href", "cid:MYIMG");

        // Attach Image
        Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        AttachmentPart ap = msg.createAttachmentPart(img, "image/jpeg");
        ap.setContentId("<MYIMG>");
        msg.addAttachmentPart(ap);
        msg.saveChanges();
        AttachmentPart bp = msg.getAttachment(gltp);
        assertTrue(bp != null);
    }

    public void testGetAttachmentBySwaRef() throws Exception {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();
        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(
                envelope.createName("GetLastTradePrice", "ztrade",
                        "http://wombat.ztrade.com"));

        gltp.addTextNode("cid:MYIMG");

        // Attach Image
        Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        AttachmentPart ap = msg.createAttachmentPart(img, "image/jpeg");
        ap.setContentId("<MYIMG>");
        msg.addAttachmentPart(ap);
        msg.saveChanges();
        AttachmentPart bp = msg.getAttachment(gltp);
        assertTrue(bp != null);
    }

    public void testAddXmlAndVerify() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(envelope.createName(
                "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attachment1 :  XML from DataHandler(URL)
        {
            URL url1 = new URL("file:src/test/resources/mime/data/attach1.xml");
            AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));
            ap1.setContentType("text/xml");
            msg.addAttachmentPart(ap1);
        }

        // Attachment1 :  XML from DataHandler(URL)
        {
            URL url1 = new URL("file:src/test/resources/mime/data/attach1.xml");
            AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));
            ap1.setContentType("text/xml");
            msg.addAttachmentPart(ap1);
        }

        // Attachment2: XML from reader. The text has XML decl
        {
            String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                    + "<START><A>Hello</A><B>World</B></START>";
            StringReader reader2 = new StringReader(xml2);
            StreamSource source2 = new StreamSource(reader2);
            AttachmentPart ap2 = msg.createAttachmentPart();
            ap2.setContent(source2, "text/xml");
            msg.addAttachmentPart(ap2);
        }

        // Attachment3: XML from reader. The text has no XML decl
        {
            String xml3 = "<START><A>Hello</A><B>World</B></START>";
            StringReader reader3 = new StringReader(xml3);
            StreamSource source3 = new StreamSource(reader3);
            AttachmentPart ap3 = msg.createAttachmentPart(source3, "text/xml");
            msg.addAttachmentPart(ap3);
        }

        // Attachment4: XML from stream
        {
            String xml4 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                    + "<START><A>Hello</A><B>World</B></START>";
            InputStream stream4 = new ByteArrayInputStream(xml4.getBytes());
            StreamSource source4 = new StreamSource(stream4);
            AttachmentPart ap4 = msg.createAttachmentPart(source4, "text/xml");
            msg.addAttachmentPart(ap4);
        }

        File f = new File("target/test-out/saving.cnt");
        f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        f.createNewFile();
        try ( // Save the soap message to file
                FileOutputStream sentFile = new FileOutputStream(f)) {
            msg.writeTo(sentFile);
        }

        try ( // See if we get the attachment back
                FileInputStream fin = new FileInputStream(f)) {
            SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
            Iterator i = newMsg.getAttachments();
            while (i.hasNext()) {
                AttachmentPart att = (AttachmentPart) i.next();
                Object obj = att.getContent();
                if (!(obj instanceof StreamSource)) {
                    fail("Didn't get StreamSource, instead got:" + obj.getClass());
                }
            }
        }
    }

    public void testAddTextPlainAndVerify() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(envelope.createName(
                "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attachment1 :  XML from DataHandler(URL)
        {
            URL url1 = new URL("file:src/test/resources/mime/data/attach1.xml");
            AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));
            ap1.setContentType("text/plain");
            msg.addAttachmentPart(ap1);
        }

        File f = new File("target/test-out/saving_text.cnt");
        f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        f.createNewFile();
        try ( // Save the soap message to file
                FileOutputStream sentFile = new FileOutputStream(f)) {
            msg.writeTo(sentFile);
        }

        try ( // See if we get the attachment back
                FileInputStream fin = new FileInputStream(f)) {
            SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
            Iterator i = newMsg.getAttachments();
            while (i.hasNext()) {
                AttachmentPart att = (AttachmentPart) i.next();
                Object obj = att.getContent();
                if (!(obj instanceof String)) {
                    fail("Didn't get String, instead got:" + obj.getClass());
                }
            }
        }
    }

    public void testSetGetRawContent() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(envelope.createName(
                "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attachment1 :  XML from file
        FileInputStream fis = new FileInputStream("src/test/resources/mime/data/attach1.xml");
        AttachmentPart ap1 = msg.createAttachmentPart();
        ap1.setRawContent(fis, "text/xml");
        msg.addAttachmentPart(ap1);
        InputStream content = ap1.getRawContent();
        assertTrue(content != null);
    }

    public void testSetContentGetRawContent() throws Exception {

        byte buf1[] = new byte[30000];
        byte buf2[] = new byte[30000];
        byte buf3[] = new byte[30000];
        byte buf4[] = new byte[30000];

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        AttachmentPart ap = msg.createAttachmentPart();

        int length1;
        try (FileInputStream is1 = new FileInputStream(IMAGE_JPG)) {
            length1 = is1.read(buf1, 0, 30000);
            //System.out.println("Length1 =" + length1);
        }

        FileInputStream is11 = new FileInputStream(IMAGE_JPG);
        ap.setRawContent(is11, "image/jpeg");
        InputStream is12 = ap.getRawContent();
        int length11 = ap.getSize();
        //System.out.println("Length11 =" + length11);
        assertTrue(length11 == length1);

        Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        ap.setContent(img, "image/jpeg");
        InputStream is3 = ap.getRawContent();
        int length2 = is3.read(buf2, 0, 30000);
        //System.out.println("Length2 =" + length2);

        Image img1 = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        ap.setContent(img1, "image/jpeg");
        InputStream is4 = ap.getDataHandler().getInputStream();
        int length3 = is4.read(buf3, 0, 30000);
        //System.out.println("Length3 =" + length3);

        Image img2 = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        ap.setContent(img2, "image/jpeg");
        int length4 = ap.getSize();
        //System.out.println("Length4 =" + length4);

        Image img3 = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        DataHandler dh = new DataHandler(img3, "image/jpeg");
        InputStream is5 = dh.getInputStream();
        int length5 = is5.read(buf4, 0, 30000);
        //System.out.println("Length5 =" + length5);
    }

    public void testSetContentGetRawContent2() throws Exception {

        byte buf1[] = new byte[30000];
        byte buf2[] = new byte[30000];
        byte buf3[] = new byte[30000];
        byte buf4[] = new byte[30000];

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        AttachmentPart ap = msg.createAttachmentPart();

        int length1;
        try (FileInputStream is1 = new FileInputStream("src/test/resources/mime/data/attach1.xml")) {
            length1 = is1.read(buf1, 0, 30000);
            //System.out.println("Length1 =" + length1);
        }

        FileInputStream is2 = new FileInputStream("src/test/resources/mime/data/attach1.xml");
        ap.setRawContent(is2, "text/xml");
        InputStream is3 = ap.getRawContent();
        int length2 = is3.read(buf2, 0, 30000);
        //System.out.println("Length2 =" + length2);
        assertTrue(length2 == length1);

        FileInputStream fis2 = new FileInputStream("src/test/resources/mime/data/attach1.xml");
        ap.setContent(fis2, "text/xml");
        InputStream is4 = ap.getDataHandler().getInputStream();
        int length3 = is4.read(buf3, 0, 30000);
        //System.out.println("Length3 =" + length3);

        /*
            FileDataSource fis3 = new FileDataSource("src/test/resources/mime/data/attach1.xml");
            ap.setContent(fis3 ,"text/xml");
            int length4 = ap.getSize();
            System.out.println("Length4 =" + length4);
         */
        FileInputStream fis4 = new FileInputStream("src/test/resources/mime/data/attach1.xml");
        DataHandler dh = new DataHandler(fis4, "text/xml");
        InputStream is5 = dh.getInputStream();
        int length5 = is5.read(buf4, 0, 30000);
        //System.out.println("Length5 =" + length5);
    }

    public void testSetRawContentBytes() throws Exception {

        byte buf1[] = new byte[30000];
        byte buf2[] = new byte[30000];
        byte buf3[] = new byte[30000];
        byte buf4[] = new byte[30000];

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        AttachmentPart ap = msg.createAttachmentPart();

        int length1;
        try (FileInputStream is1 = new FileInputStream("src/test/resources/mime/data/attach1.xml")) {
            length1 = is1.read(buf1, 0, 30000);
        }

        ap.setRawContentBytes(buf1, 0, length1, "text/xml");
        msg.addAttachmentPart(ap);
        msg.saveChanges();
        byte[] bufx = ap.getRawContentBytes();
        // on Win2k CRLF changes into CRCRLF
        // assertTrue((bufx.length == 1634) || (bufx.length == 1710));
        assertTrue(length1 == bufx.length);
    }

    public void testGetBase64Content() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(envelope.createName(
                "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attachment1 :  XML from file
        FileInputStream fis = new FileInputStream("src/test/resources/mime/data/attach1.xml");
        AttachmentPart ap1 = msg.createAttachmentPart();
        ap1.setRawContent(fis, "text/xml");
        msg.addAttachmentPart(ap1);
        InputStream content = ap1.getBase64Content();
        assertTrue(content != null);
        /*
                int len;
                int size = 1024;
                byte [] buf;

                buf = new byte[size];
                while ((len = content.read(buf, 0, size)) != -1) {
                    System.out.println(new String(buf));
                }*/

    }

    public void testSetBase64Content() throws Exception {
        /* temporarily comment
                MessageFactory mf = MessageFactory.newInstance();
                SOAPMessage msg = mf.createMessage();
                SOAPPart sp = msg.getSOAPPart();

                SOAPEnvelope envelope = sp.getEnvelope();

                SOAPHeader hdr = envelope.getHeader();
                SOAPBody bdy = envelope.getBody();

                // Add to body
                SOAPBodyElement gltp = bdy.addBodyElement(envelope.createName(
                        "GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
                gltp.addChildElement(envelope.createName("symbol", "ztrade",
                        "http://wombat.ztrade.com")).addTextNode("SUNW");

                // Attachment1 :  XML from file
                FileInputStream fis = new FileInputStream("src/test/resources/mime/data/attach1.xml");
                AttachmentPart ap1 = msg.createAttachmentPart();

                InputStream stream = null;
                int len;
                int size = 1024;
                byte [] buf;

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                OutputStream ret = MimeUtility.encode(bos, "base64");
                //OutputStream ret = Base64.encode(bos);
                buf = new byte[size];

                while ((len = fis.read(buf, 0, size)) != -1) {
                        ret.write(buf, 0, len);
                }
                ret.flush();
                buf = bos.toByteArray();
                stream = new ByteArrayInputStream(buf);

                ap1.setBase64Content(stream,"text/xml");
                msg.addAttachmentPart(ap1);
                InputStream content = ap1.getRawContent();
                assertTrue(content != null);
         */
 /*
                buf = new byte[size];
                while ((len = content.read(buf, 0, size)) != -1) {
                    System.out.println(new String(buf));
                }*/

    }

    public void testRemoveImageAndVerify() throws Exception {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPPart sp = msg.getSOAPPart();

        SOAPEnvelope envelope = sp.getEnvelope();

        SOAPHeader hdr = envelope.getHeader();
        SOAPBody bdy = envelope.getBody();

        // Add to body
        SOAPBodyElement gltp = bdy.addBodyElement(
                envelope.createName("GetLastTradePrice", "ztrade",
                        "http://wombat.ztrade.com"));

        gltp.addChildElement(envelope.createName("symbol", "ztrade",
                "http://wombat.ztrade.com")).addTextNode("SUNW");

        // Attach Image
        Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        AttachmentPart ap = msg.createAttachmentPart(img, "image/jpeg");
        msg.addAttachmentPart(ap);
        AttachmentPart ap1 = msg.createAttachmentPart(img, "image/jpeg");
        msg.addAttachmentPart(ap1);
        FileInputStream fis = new FileInputStream("src/test/resources/mime/data/attach1.xml");
        AttachmentPart ap2 = msg.createAttachmentPart();
        ap2.setRawContent(fis, "text/xml");
        msg.addAttachmentPart(ap2);
        msg.saveChanges();

        // Save the soap message to file
        File f = new File(IMAGE_JPG_SENT);
        f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        f.createNewFile();
        try (FileOutputStream sentFile = new FileOutputStream(f)) {
            msg.writeTo(sentFile);
        }

        try ( // See if we get the image object back
                FileInputStream fin = new FileInputStream(f)) {
            SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
            Iterator i = newMsg.getAttachments();
            //System.out.println("Count before remove:" + newMsg.countAttachments());
            MimeHeaders headers = new MimeHeaders();
            headers.addHeader("Content-Type", "image/jpeg");
            while (i.hasNext()) {
                AttachmentPart att = (AttachmentPart) i.next();
                newMsg.removeAttachments(headers);
                break;
            }
            //System.out.println("Count after remove:" + newMsg.countAttachments());
            assertTrue(newMsg.countAttachments() == 1);
        }
    }

}
