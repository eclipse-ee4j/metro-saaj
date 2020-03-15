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
 * MessageBuilder.java
 *
 * Created on April 4, 2003, 12:03 PM
 */

package mime;

import java.io.*;
import java.util.*;

import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import org.xml.sax.InputSource;

/**
 * @author Krishna Meduri
 */

public class MessageBuilder {
    
    public byte[] saveMimeHeaders(SOAPMessage msg) throws IOException {

        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            Map<String, String> hashTable = new HashMap<String, String>();
            MimeHeaders mimeHeaders = msg.getMimeHeaders();
            @SuppressWarnings("unchecked")
            Iterator<MimeHeader> iterator = mimeHeaders.getAllHeaders();

            while (iterator.hasNext()) {
                MimeHeader mimeHeader = iterator.next();
                hashTable.put(mimeHeader.getName(), mimeHeader.getValue());
            }

            oos.writeObject(hashTable);
            oos.flush();
            return bos.toByteArray();
        } finally {
            if (oos != null)
                oos.close();
        }
    }
    
    public SOAPMessage constructMessage(byte[] mimeHdrBytes, byte[] msgBytes)
    throws Exception {
        SOAPMessage message;
        
        MimeHeaders mimeHeaders = new MimeHeaders();
        ByteArrayInputStream bis = new ByteArrayInputStream(msgBytes);
        
        ObjectInputStream ois = new ObjectInputStream(
        new ByteArrayInputStream(mimeHdrBytes));
        @SuppressWarnings("unchecked")
        Map<String, String> hashTable = (Map<String, String>) ois.readObject();
        ois.close();
        
        if (hashTable.isEmpty())
            System.out.println("MimeHeaders Hashtable is empty");
        else {
            for (Map.Entry<String, String> me : hashTable.entrySet())
                mimeHeaders.addHeader(me.getKey(), me.getValue());
        }
        
        MessageFactory messageFactory = MessageFactory.newInstance();
        message = messageFactory.createMessage(mimeHeaders, bis);
        message.saveChanges(); 
        return message;
    }
    
    public boolean verifyMessage(SOAPMessage msg, SOAPMessage gMsg)
    throws Exception {
        boolean result = false;
        result = verifySOAPPart(msg, gMsg);
        if (result == false) {
            System.out.println("Mismatch in SOAPParts");
            return false;
        }
        result = verifyAttachmentPart(msg, gMsg);
        if (result == false) {
            System.out.println("Mismatch in AttachmentParts");
            return false;
        }
        return true;
    }
    
    public boolean verifyAttachmentPart(SOAPMessage msg, SOAPMessage gMsg)
    throws Exception {
        
        StreamSource streamSource = null;
        StreamSource gStreamSource = null;
        
        BufferedReader reader = null;
        BufferedReader gReader = null;
        
        AttachmentPart ap = null;
        AttachmentPart gAp = null;
        
        String str = null;
        String gStr = null;
        
        int count = msg.countAttachments();
        int gCount = gMsg.countAttachments();
        
        // System.out.println("****attachment count**" + gCount);
        
        if (count != gCount)
            return false;
        
        if (count < 1)
            return true;
        
        @SuppressWarnings("unchecked")
        Iterator<AttachmentPart> attachmentIterator = msg.getAttachments();
        @SuppressWarnings("unchecked")
        Iterator<AttachmentPart> gAttachmentIterator = gMsg.getAttachments();
        
        for (int i=0; i < count; i++) {
            ap = attachmentIterator.next();
            gAp = gAttachmentIterator.next();
            
            String contentType = ap.getContentType();
            String gContentType = gAp.getContentType();
            
            //System.out.println("**contentType**" + contentType);
            //System.out.println("**gContentType**" + gContentType);
            
            if (!contentType.equals(gContentType))
                return false;
            
            //Code for contentType="text/plain yet to write
            if ( gContentType.equals( "text/xml") ) {
//                System.out.println("getContent() returns <" +
//                ap.getContent().getClass() + ">");
                
                streamSource = (StreamSource) ap.getContent();
                gStreamSource = (StreamSource) gAp.getContent();
                                
                reader = new BufferedReader(
                new InputStreamReader(streamSource.getInputStream()));
                gReader = new BufferedReader(
                new InputStreamReader(gStreamSource.getInputStream()));
                
            } else {
                InputStream is = (InputStream)ap.getContent();
                InputStream gIs = (InputStream)gAp.getContent();
                
                reader = new BufferedReader( new InputStreamReader(is));
                gReader = new BufferedReader( new InputStreamReader(gIs));
            }
            
            while (true) {
                boolean gReaderMore = false;
                boolean readerMore = false;
                
                if ((gStr = gReader.readLine()) != null) {
                    gReaderMore = true;
                }
                if ((str = reader.readLine()) != null) {
                    readerMore = true;
                }
                
                if (gReaderMore != readerMore) {
                    System.out.println(
                    "The number of lines in reader differs from golden");
                    return false;
                } else if (gReaderMore == true && readerMore == true) {
                    if (!gStr.equals(str)) {
                        System.out.println( "gStr is <" + gStr + ">");
                        System.out.println( "str is <" + str + ">");
                        System.out.println(
                        "Contents in a line differ from  golden");
                        return false;
                    }
                } else
                    break;
            }
            
        }
        
        return true;
    }
    
    public boolean verifySOAPPart(SOAPMessage msg, SOAPMessage gMsg)
    throws Exception {
        
        String str = null;
        String gStr = null;
        
        BufferedReader reader = null;
        
        SOAPPart soapPart = msg.getSOAPPart();
        
        //System.out.println("**soapPart.getContent().getClass() returns " +
        //"<" + soapPart.getContent().getClass() + ">");
        
        if (soapPart.getContent() instanceof SAXSource) {
            
            SAXSource saxSource = (SAXSource)soapPart.getContent();
            InputSource iSource = saxSource.getInputSource();
            reader = new BufferedReader(iSource.getCharacterStream());
            
        } else if (soapPart.getContent() instanceof StreamSource) {
            
            StreamSource streamSource = (StreamSource) soapPart.getContent();
            InputStream instream = streamSource.getInputStream();
            InputStreamReader isReader = new InputStreamReader(instream);
            
            reader = new BufferedReader(isReader);
            
        } else if (soapPart.getContent() instanceof DOMSource) {
            
            //With SAAJ1.2 we get DOMSOurce for SOAPPart - 02-14-2003 kmeduri
            DOMSource domSource = (DOMSource) soapPart.getContent();
            
            TransformerFactory tFactory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
            Transformer transformer = tFactory.newTransformer();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult(baos);
            
            transformer.transform(domSource, streamResult);
            baos = (ByteArrayOutputStream) streamResult.getOutputStream();
            
            byte[] byteArray = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            
            InputStreamReader isReader = new InputStreamReader(bais);
            reader = new BufferedReader(isReader);
        }
        
        SOAPPart gSoapPart = gMsg.getSOAPPart();
        //System.out.println("gSoapPart.getContent().getClass() returns " +
        //"<" + gSoapPart.getContent().getClass() + ">");
                
        if (gSoapPart.getContent() instanceof SAXSource ) {
            
            SAXSource saxSource2 = (SAXSource)gSoapPart.getContent();
            InputSource iSource2 = saxSource2.getInputSource();
            BufferedReader gReader =
            new BufferedReader(iSource2.getCharacterStream());
            
            while (true) {
                boolean gReaderMore = false;
                boolean readerMore = false;
                
                if ((gStr = gReader.readLine()) != null) {
                    //Added 09-06-2002 - kmeduri
                    if (gStr.startsWith("<?xml"))
                        gStr = gReader.readLine();
                    //System.out.println(gStr);
                    
                    gReaderMore = true;
                }
                if ((str = reader.readLine()) != null) {
                    //Added 09-06-2002 - kmeduri
                    if (str.startsWith("<?xml"))
                        str = reader.readLine();
                    //System.out.println(str);
                    
                    readerMore = true;
                }
                
                if (gReaderMore != readerMore) {
                    return false;
                } else if (gReaderMore == true && readerMore == true) {
                    if (!gStr.equals(str))
                        return false;
                } else
                    break;
            }
        }
        
        if (gSoapPart.getContent() instanceof StreamSource ) {
            
            StreamSource streamSource = (StreamSource)gSoapPart.getContent();
            InputStream instream = streamSource.getInputStream();
            InputStreamReader isReader = new InputStreamReader(instream);
            
            BufferedReader gReader =
            new BufferedReader(isReader);
            
            while (true) {
                boolean gReaderMore = false;
                boolean readerMore = false;
                
                if ((gStr = gReader.readLine()) != null) {
                    
                    if (gStr.startsWith("<?xml"))
                        gStr = gReader.readLine();
                    //System.out.println(gStr);
                    
                    gReaderMore = true;
                }
                
                if ((str = reader.readLine()) != null) {
                    
    /* Anil fixed this for java_xml_pack promoted build b07.
       Had to comment this... kmeduri...04/15/2002
     
                       The fix broke in J2EE1.4 integration build
                        (saaj jar picked from jaxrpc build on Aug 19)
                       Uncommenting on 09-06-2002 ...
     
     **/
                    
                    //SAXSource puts prolog in the beginning which is not the
                    //case with StreamSource. This stmt ignores xml prolog
                    if (str.startsWith("<?xml")) {
                        str = str.substring(str.indexOf('<', 5));
                    }
                    // System.out.println(str);
                    readerMore = true;
                }
                
                if (gReaderMore != readerMore) {
                    return false;
                } else if (gReaderMore == true && readerMore == true) {
                    if (!gStr.equals(str))
                        return false;
                } else
                    break;
            }
        }
        
        return true;
    }
}


