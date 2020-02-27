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

import jakarta.xml.soap.*;

import java.io.ByteArrayInputStream;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

public class CharacterSetEncodingTest extends TestCase {

    public CharacterSetEncodingTest(String name) {
        super(name);
    }

    public void testCharacterSetEncoding() throws Exception {
        String testString = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>";
        byte[] utf16bytes = testString.getBytes("utf-16");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf16bytes);
        MimeHeaders headers = new MimeHeaders();
        headers.setHeader("Content-Type", "text/xml; charset=utf-16");
        SOAPMessage msg =
            MessageFactory.newInstance().createMessage(headers, bais);
        msg.saveChanges();

        headers = msg.getMimeHeaders();
        String contentTypeString  = headers.getHeader("Content-Type")[0];
        assertEquals(contentTypeString, "text/xml; charset=utf-16");
   }
    
    public void testCharacterSetUtf16() throws Exception {
    
        MessageFactory factory = MessageFactory.newInstance();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><p:content SOAP-ENV:encodingStyle=\"http://example.com/encoding\" xmlns:p=\"some-uri\">Jeu universel de caract�res cod�s � plusieurs octets</p:content></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        SOAPMessage msg = factory.createMessage();
        msg.getMimeHeaders().setHeader("Content-Type","text/xml; charset=utf-16");
        msg.getSOAPPart().setContent(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-16"))));   
        msg.saveChanges(); 

        MimeHeaders headers = msg.getMimeHeaders();
                
        String contentTypeString  = headers.getHeader("Content-Type")[0];
        assertEquals(contentTypeString, "text/xml; charset=utf-16");
    }
    
    /* 
     * MimeHeaders.getHeader seems to fail in SOAP 1.2
     * bug id: 6267874 
     */
    
       public void testCharacterSetSOAP12() throws Exception {
        String testString = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Body/></soap:Envelope>";
        byte[] utf16bytes = testString.getBytes("utf-16");
        ByteArrayInputStream bais = new ByteArrayInputStream(utf16bytes);
        MimeHeaders headers = new MimeHeaders();
        //headers.setHeader("Content-Type", "");
        SOAPMessage msg =
            MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(headers, bais);
        msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING,"utf-16");
        msg.saveChanges();
      
        headers = msg.getMimeHeaders();
        headers.setHeader("Content-Type", "");
        String contentTypeString  = headers.getHeader("Content-Type")[0];
        assertEquals(contentTypeString, "");
   }
}
