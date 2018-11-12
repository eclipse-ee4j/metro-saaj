/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util;

import java.io.*;

import javax.xml.transform.TransformerException;

/* 
 * Class that parses the very first construct in the document i.e.
 *  <?xml ... ?>
 *
 * @author Panos Kougiouris (panos@acm.org)
 * @version  
 */

public class XMLDeclarationParser {
    private String m_encoding;
    private PushbackReader m_pushbackReader;
    private boolean m_hasHeader; // preserve the case where no XML Header exists
    private String xmlDecl = null;
    static String gt16 = null;
    static String utf16Decl = null;
    static {
         try {
             gt16 = new String(">".getBytes("utf-16"));
             utf16Decl = new String("<?xml".getBytes("utf-16"));
         } catch (Exception e) {}
    }

    //---------------------------------------------------------------------

    public XMLDeclarationParser(PushbackReader pr)
    {
        m_pushbackReader = pr;
        m_encoding = "utf-8";
        m_hasHeader = false;
    }

    //---------------------------------------------------------------------
    public String getEncoding()
    {
        return m_encoding;
    }

    public String getXmlDeclaration() {
        return xmlDecl;
    }

    //---------------------------------------------------------------------

     public void parse()  throws TransformerException, IOException
     {
        int c = 0;
        int index = 0;
        StringBuilder xmlDeclStr = new StringBuilder();
        while ((c = m_pushbackReader.read()) != -1) {
            xmlDeclStr.append((char)c);
            index++;
            if (c == '>') {
                break;
            }
        }
        int len = index;

        String decl = xmlDeclStr.toString();
        boolean utf16 = false;
        boolean utf8 = false;

        int xmlIndex = decl.indexOf(utf16Decl);
        if (xmlIndex > -1) {
            utf16 = true;
        } else {
            xmlIndex = decl.indexOf("<?xml");
            if (xmlIndex > -1) {
                utf8 = true;
            }
        }

        // no XML decl
        if (!utf16 && !utf8) {
            m_pushbackReader.unread(decl.toCharArray(), 0, len);
            return;
        }
        m_hasHeader = true;
        
        if (utf16) {
            xmlDecl = new String(decl.getBytes(), "utf-16");
            xmlDecl = xmlDecl.substring(xmlDecl.indexOf("<"));
        } else {
            xmlDecl = decl;
        }
        // do we want to check that there are no other characters preceeding <?xml
        if (xmlIndex != 0) {
            throw new IOException("Unexpected characters before XML declaration");
        }

        int versionIndex =  xmlDecl.indexOf("version");
        if (versionIndex == -1) {
            throw new IOException("Mandatory 'version' attribute Missing in XML declaration");
        }

        // now set
        int encodingIndex = xmlDecl.indexOf("encoding");
        if (encodingIndex == -1) {
            return;
        }

        if (versionIndex > encodingIndex) {
            throw new IOException("The 'version' attribute should preceed the 'encoding' attribute in an XML Declaration");
        }

        int stdAloneIndex = xmlDecl.indexOf("standalone");
        if ((stdAloneIndex > -1) && ((stdAloneIndex < versionIndex) || (stdAloneIndex < encodingIndex))) {
            throw new IOException("The 'standalone' attribute should be the last attribute in an XML Declaration");
        }

        int eqIndex = xmlDecl.indexOf("=", encodingIndex);
        if (eqIndex == -1) {
            throw new IOException("Missing '=' character after 'encoding' in XML declaration");
        }

        m_encoding = parseEncoding(xmlDecl, eqIndex);
        if(m_encoding.startsWith("\"")){
            m_encoding = m_encoding.substring(m_encoding.indexOf("\"")+1, m_encoding.lastIndexOf("\""));
        } else if(m_encoding.startsWith("\'")){
            m_encoding = m_encoding.substring(m_encoding.indexOf("\'")+1, m_encoding.lastIndexOf("\'"));
        }
     }

     //--------------------------------------------------------------------

    public void writeTo(Writer wr) throws IOException {
        if (!m_hasHeader) return;
        wr.write(xmlDecl.toString());
    }

    private String parseEncoding(String xmlDeclFinal, int eqIndex) throws IOException {
        java.util.StringTokenizer strTok = new java.util.StringTokenizer(
            xmlDeclFinal.substring(eqIndex + 1));
        if (strTok.hasMoreTokens()) {
            String encodingTok = strTok.nextToken();
            int indexofQ = encodingTok.indexOf("?");
            if (indexofQ > -1) {
                return encodingTok.substring(0,indexofQ);
            } else {
                return encodingTok;
            }
        } else {
            throw new IOException("Error parsing 'encoding' attribute in XML declaration");
        }
    }

}
    
