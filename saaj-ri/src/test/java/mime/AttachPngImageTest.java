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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import jakarta.xml.soap.*;

import junit.framework.TestCase;

/*
 * Attaches an image object and verifies whether it gets the image object back
 */
public class AttachPngImageTest extends TestCase {

    private static final String IMAGE_JPG = "src/test/resources/mime/data/cup.jpg";
    private static final String IMAGE_PNG = "src/test/resources/mime/data/image.png";
    private static final String IMAGE_SENT_PNG = "target/test-out/image_sent.png";

    public AttachPngImageTest(String name) {
        super(name);
    }
     
    public void testAddPngImageAndVerify() throws Exception {
        
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
		Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_PNG);
        AttachmentPart ap = msg.createAttachmentPart(img, "image/png");
        msg.addAttachmentPart(ap);

        // Attach Image        
	Image img1 = Toolkit.getDefaultToolkit().getImage(IMAGE_JPG);
        AttachmentPart ap1 = msg.createAttachmentPart(img1, "image/jpeg");
        msg.addAttachmentPart(ap1);

        msg.saveChanges();

        File f = new File(IMAGE_SENT_PNG);
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
            Iterator i = newMsg.getAttachments();
            while(i.hasNext()) {
                AttachmentPart att = (AttachmentPart)i.next();
                Object obj = att.getContent();
                if (!(obj instanceof Image)) {
                    fail("Didn't get the image type, instead got:"+obj.getClass());
                }
            }
        }
    }

}
