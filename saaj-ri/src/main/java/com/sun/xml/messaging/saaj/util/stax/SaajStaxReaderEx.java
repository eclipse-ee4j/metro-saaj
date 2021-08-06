/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util.stax;

import org.jvnet.staxex.Base64Data;
import org.jvnet.staxex.BinaryText;
import org.jvnet.staxex.XMLStreamReaderEx;
import org.jvnet.staxex.util.DOMStreamReader;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import jakarta.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import java.util.Iterator;

/**
 * SaajStaxReaderEx 
 * 
 * @author shih-chang.chen@oracle.com
 */
public class SaajStaxReaderEx extends DOMStreamReader implements XMLStreamReaderEx {
    //TODO extends com.sun.xml.ws.streaming.DOMStreamReader
    private BinaryText binaryText = null;
    private Base64Data base64AttData = null;
    
    public SaajStaxReaderEx(SOAPElement se) {
        super(se);
    }
    
    @Override
    public int next() throws XMLStreamException {
        binaryText = null;
        base64AttData = null;
        while(true) {
            int r = _next();
            switch (r) {
            case CHARACTERS:
                if (_current instanceof BinaryText) {
                    binaryText = (BinaryText) _current;
                    base64AttData = new Base64Data();
                    base64AttData.set(binaryText.getDataHandler());
//System.out.println("--------------- debug SaajStaxReaderEx binaryText " + binaryText);
                } else {
                    // if we are currently at text node, make sure that this is a meaningful text node.
                    Node prev = _current.getPreviousSibling();
                    if(prev!=null && prev.getNodeType()==Node.TEXT_NODE)
                        continue;   // nope. this is just a continuation of previous text that should be invisible
    
                    Text t = (Text)_current;
                    wholeText = t.getWholeText();
                    if(wholeText.length()==0)
                        continue;   // nope. this is empty text.
                }
                return CHARACTERS;
            case START_ELEMENT:
                splitAttributes();
                return START_ELEMENT;
            default:
                return r;
            }
        }
    }

    @Override
    public String getElementTextTrim() throws XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CharSequence getPCDATA() throws XMLStreamException {
        return (binaryText != null) ? base64AttData : getText();
    }   

    @Override
    public org.jvnet.staxex.NamespaceContextEx getNamespaceContext() {        
        return new org.jvnet.staxex.NamespaceContextEx() {

            @Override
            public String getNamespaceURI(String prefix) {
                return _current.lookupNamespaceURI(prefix);
            }

            @Override
            public String getPrefix(String uri) {
                return _current.lookupPrefix(uri);
            }

            @Override
            public Iterator getPrefixes(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Iterator<Binding> iterator() {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }


    @Override
    public int getTextLength() {
        return (binaryText != null) ? base64AttData.length() : super.getTextLength();
    }

    @Override
    public int getTextStart() {
        return (binaryText != null) ? 0: super.getTextStart();
    } 

    @Override
    public char[] getTextCharacters() {
        if (binaryText != null) {
            char[] chars = new char[base64AttData.length()];
            base64AttData.writeTo(chars, 0);
            return chars;
        }
        return super.getTextCharacters();
    }   
}
