/*
 * Copyright (c) 2013, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.util.stax;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import jakarta.activation.DataHandler;
import jakarta.xml.bind.attachment.AttachmentMarshaller;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;

import org.jvnet.staxex.Base64Data;
import org.jvnet.staxex.BinaryText;
import org.jvnet.staxex.MtomEnabled;
import org.jvnet.staxex.NamespaceContextEx;
import org.jvnet.staxex.StreamingDataHandler;
import org.jvnet.staxex.XMLStreamWriterEx;
import org.jvnet.staxex.util.MtomStreamWriter;
//
//import com.sun.xml.ws.api.message.saaj.SaajStaxWriter;
//import com.sun.xml.ws.developer.StreamingDataHandler;
//import com.sun.xml.ws.streaming.MtomStreamWriter;

/**
 * SaajStaxWriterEx converts XMLStreamWriterEx calls to build an orasaaj SOAPMessage with BinaryTextImpl.
 * 
 * @author shih-chang.chen@oracle.com
 */
public class SaajStaxWriterEx extends SaajStaxWriter implements XMLStreamWriterEx, MtomStreamWriter {
    
    static final protected String xopNS = "http://www.w3.org/2004/08/xop/include";
    static final protected String Include = "Include";
    static final protected String href = "href";
    
    private enum State {xopInclude, others};
    private State state = State.others;
    private BinaryText binaryText;

    public SaajStaxWriterEx(SOAPMessage msg, String uri) throws SOAPException {
        super(msg, uri);
    }
    
    @Override
    public void writeStartElement(String prefix, String ln, String ns) throws XMLStreamException {
        if (xopNS.equals(ns) && Include.equals(ln)) {
            state = State.xopInclude;
        } else {
            super.writeStartElement(prefix, ln, ns);
        }
    }
    
    @Override
    public void writeEndElement() throws XMLStreamException {
        if (state.equals(State.xopInclude)) {
            state = State.others;
        } else {
            super.writeEndElement();
        }
    }

    @Override
    public void writeAttribute(String prefix, String ns, String ln, String value) throws XMLStreamException {
        if (binaryText != null && href.equals(ln)) {
            return;
        } else {
            super.writeAttribute(prefix, ns, ln, value);
        }
    }

//    @Override
//    public void writeComment(String data) throws XMLStreamException {
//        ((ElementImpl)currentElement).addCommentNode(data);
//    }
//
//    @Override
//    public void writeCData(String data) throws XMLStreamException {
//      CDataTextImpl cdt = new CDataTextImpl(soap.getSOAPPart(), data);
//        currentElement.appendChild(cdt);
//    }

    @Override
    public NamespaceContextEx getNamespaceContext() {
        return new NamespaceContextEx() {
            @Override
            public String getNamespaceURI(String prefix) {
                return currentElement.getNamespaceURI(prefix);
            }
            @Override
            public String getPrefix(String namespaceURI) {
                return currentElement.lookupPrefix(namespaceURI);
            }
            @Override
            public Iterator<String> getPrefixes(final String namespaceURI) {
                return new Iterator<String>() {
                    String prefix = getPrefix(namespaceURI);
                    @Override
                    public boolean hasNext() {
                        return (prefix != null);
                    }
                    @Override
                    public String next() {
                        if (prefix == null) throw new java.util.NoSuchElementException();
                        String next = prefix;
                        prefix = null;
                        return next;
                    }
                    @Override
                    public void remove() {}                    
                };
            } 
            @Override
            public Iterator<Binding> iterator() {
                return new Iterator<Binding>() {
                    @Override
                    public boolean hasNext() { return false; }
                    @Override
                    public Binding next() { return null; }
                    @Override
                    public void remove() {}                    
                };
            }            
        };
    }

    @Override
    public void writeBinary(DataHandler data) throws XMLStreamException {
//      binaryText = BinaryTextImpl.createBinaryTextFromDataHandler((MessageImpl)soap, null, currentElement.getOwnerDocument(), data);
//      currentElement.appendChild(binaryText);  
        addBinaryText(data);    
    }

    @Override
    public OutputStream writeBinary(String arg0) throws XMLStreamException {
        return null;
    }

    @Override
    public void writeBinary(byte[] data, int offset, int length, String contentType) throws XMLStreamException {
//        if (mtomThreshold == -1 || mtomThreshold > length) return null;
        byte[] bytes = (offset == 0 && length == data.length) ? data : Arrays.copyOfRange(data, offset, offset + length);
        if (currentElement instanceof MtomEnabled) {
            binaryText = ((MtomEnabled) currentElement).addBinaryText(bytes);
        } else {
            throw new IllegalStateException("The currentElement is not MtomEnabled " + currentElement);
        }
    }

    @Override
    public void writePCDATA(CharSequence arg0) throws XMLStreamException {
        if (arg0 instanceof Base64Data) {
            // The fix of StreamReaderBufferCreator preserves this dataHandler
            addBinaryText(((Base64Data) arg0).getDataHandler());
        } else {
            // We should not normally get here as we expect a DataHandler,
            // but this is the most general solution.  If we do get
            // something other than a Data Handler, create a Text node with
            // the data.  Another alternative would be to throw an exception,
            // but in the most general case, we don't know whether this input
            // is expected.
            try {
                currentElement.addTextNode(arg0.toString());
            } catch (SOAPException e) {
                throw new XMLStreamException("Cannot add Text node", e);
            }
        }
    }
    
    static private String encodeCid() {
        String cid = "example.jaxws.sun.com";
        String name = UUID.randomUUID() + "@";
        return name + cid;
    }

    private String addBinaryText(DataHandler data) {
        String hrefOrCid = null;
        if (data instanceof StreamingDataHandler) {
            hrefOrCid = ((StreamingDataHandler) data).getHrefCid();
        }
        if (hrefOrCid == null) hrefOrCid = encodeCid();                

        String prefixedCid = (hrefOrCid.startsWith("cid:")) ? hrefOrCid : "cid:" + hrefOrCid;                
        // Should we do the threshold processing on DataHandler ? But that would be
        // expensive as DataHolder need to read the data again from its source
      //binaryText = BinaryTextImpl.createBinaryTextFromDataHandler((MessageImpl) soap, prefixedCid, currentElement.getOwnerDocument(), data);
      //currentElement.appendChild(binaryText);
        if (currentElement instanceof MtomEnabled) {
            binaryText = ((MtomEnabled) currentElement).addBinaryText(prefixedCid, data);
        } else {
            throw new IllegalStateException("The currentElement is not MtomEnabled " + currentElement);
        }        
        return hrefOrCid;
    }

    @Override
    public AttachmentMarshaller getAttachmentMarshaller() {
        return new AttachmentMarshaller() {
            @Override
            public String addMtomAttachment(DataHandler data, String ns, String ln) {
//                if (mtomThreshold == -1) return null;
                String hrefOrCid = addBinaryText(data); 
//                return binaryText.getHref();                
                return hrefOrCid;
            }

            @Override
            public String addMtomAttachment(byte[] data, int offset, int length, String mimeType, String ns, String ln) {
//                if (mtomThreshold == -1 || mtomThreshold > length) return null;
                byte[] bytes = (offset == 0 && length == data.length) ? data : Arrays.copyOfRange(data, offset, offset + length);
//                binaryText = (BinaryTextImpl) ((ElementImpl) currentElement).addAsBase64TextNode(bytes);
                if (currentElement instanceof MtomEnabled) {
                    binaryText = ((MtomEnabled) currentElement).addBinaryText(bytes);
                } else {
                    throw new IllegalStateException("The currentElement is not MtomEnabled " + currentElement);
                } 
                return binaryText.getHref();
            }

            @Override
            public String addSwaRefAttachment(DataHandler data) {
                return "cid:"+encodeCid();
            }

            @Override
            public boolean isXOPPackage() {
                return true;
            }
        };
    }
}
