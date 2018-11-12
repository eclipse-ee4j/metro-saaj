/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * 
 */

package mime;



import junit.framework.TestCase;



/*
 * Attaches an image object and verifies whether it gets the image object back
 */

public class AttachMultipartTest extends TestCase {

//    private static final String IMAGE = "src/test/mime/data/cup.jpg";
//    private static final String IMAGE_SENT = "src/test/mime/data/cup_sent.jpg";

    public AttachMultipartTest(String name) {
        super(name);
    }
     
    public void testAddMultipartAndVerify() throws Exception {

    /*
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

        URL url = new File("src/test/mime/data/message1.txt").toURL();
         
        // create a multipart object 
        MimeMultipart multipart = new MimeMultipart("mixed");
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDataHandler(new DataHandler(url));
        String bpct = bodyPart.getContentType();
        bodyPart.setHeader("Content-Type", bpct);

        multipart.addBodyPart(bodyPart);

        String contentType = multipart.getContentType().toString();
        AttachmentPart ap1 = msg.createAttachmentPart(multipart, contentType);
        ap1.setContentId("cid:someid1234");
        msg.addAttachmentPart(ap1); 

        
        // Attach Image        
		Image img = Toolkit.getDefaultToolkit().getImage(IMAGE);
        AttachmentPart ap = msg.createAttachmentPart(img, "image/jpeg");
        msg.addAttachmentPart(ap);
        msg.saveChanges();
		
                // Save the soap message to file
		FileOutputStream sentFile = new FileOutputStream(IMAGE_SENT);
		msg.writeTo(sentFile);
		sentFile.close();

		// See if we get the image object back
		FileInputStream fin= new FileInputStream(IMAGE_SENT);
		SOAPMessage newMsg = mf.createMessage(msg.getMimeHeaders(), fin);
		Iterator i = newMsg.getAttachments();
		while(i.hasNext()) {
			AttachmentPart att = (AttachmentPart)i.next();
			Object ct = att.getContent();
		        if (!(ct instanceof MimeMultipart)) {
                           fail("Didnt get the Multipart type, instead got " +
                              ct.getClass());
                        }
                        break;  	
		}
		fin.close();
    */
    }
    
}
