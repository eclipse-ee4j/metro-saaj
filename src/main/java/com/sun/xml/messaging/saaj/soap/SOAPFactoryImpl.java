/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import com.sun.xml.messaging.saaj.soap.impl.ElementFactory;
import com.sun.xml.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.messaging.saaj.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

public abstract class SOAPFactoryImpl extends SOAPFactory {

    protected static final Logger
        log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN,
                               "com.sun.xml.messaging.saaj.soap.LocalStrings");

    protected abstract SOAPDocumentImpl createDocument();

    @Override
    public SOAPElement createElement(String tagName) throws SOAPException {
         if (tagName == null) {
             log.log(
                 Level.SEVERE,"SAAJ0567.soap.null.input", 
                 new Object[] {"tagName","SOAPFactory.createElement"});
             throw new SOAPException("Null tagName argument passed to createElement");
         } 
        return ElementFactory.createElement(createDocument(),
                        NameImpl.createFromTagName(tagName));
    }

    @Override
    public SOAPElement createElement(Name name) throws SOAPException {
        // @since SAAJ 1.3
        // If the Name was null it would cause a NullPointerException in earlier release
        if (name == null) {
            log.log(Level.SEVERE,"SAAJ0567.soap.null.input", 
                        new Object[] {"name","SOAPFactory.createElement"});
            throw new SOAPException("Null name argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(), name);
    }

    @Override
    public SOAPElement createElement(QName qname) throws SOAPException {
        if (qname == null) {
            log.log(Level.SEVERE,"SAAJ0567.soap.null.input", 
                        new Object[] {"qname","SOAPFactory.createElement"});
            throw new SOAPException("Null qname argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(),qname);
    }

    @Override
    public SOAPElement createElement(
        String localName,
        String prefix,
        String uri)  throws SOAPException {

        // @since SAAJ 1.3
        // if prefix !=null but localName== null then in earlier releases it would create
        // a Qualified Name  <prefix>:null which is not meaningful
        if (localName == null) {
            log.log(Level.SEVERE,"SAAJ0567.soap.null.input", 
                        new Object[] {"localName","SOAPFactory.createElement"});
            throw new SOAPException("Null localName argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(), localName, prefix, uri);
    }

    @Override
    public Name createName(String localName, String prefix, String uri)
        throws SOAPException {
        // @since SAAJ 1.3
        // if localName==null, earlier impl would create Name with localName="" 
        // which is absurd.
        if (localName == null) {
            log.log(
                 Level.SEVERE,"SAAJ0567.soap.null.input", 
                 new Object[] {"localName","SOAPFactory.createName"});
            throw new SOAPException("Null localName argument passed to createName");
        } 
        return NameImpl.create(localName, prefix, uri);
    }

    @Override
    public Name createName(String localName) throws SOAPException {
        // @since SAAJ 1.3
        // if localName==null, earlier impl would create Name with localName=null 
        // which is absurd.
        if (localName == null) {
            log.log(
                Level.SEVERE,"SAAJ0567.soap.null.input", 
                new Object[] {"localName","SOAPFactory.createName"});
            throw new SOAPException("Null localName argument passed to createName");
        } 
        return NameImpl.createFromUnqualifiedName(localName);
    }

    // Note: the child elements might still be org.w3c.dom.Element's, but the
    // getChildElements will do the conversion to SOAPElement when called.
    @Override
    public SOAPElement createElement(Element domElement) throws SOAPException {
        if (domElement == null) {
            return null;
        }
        return convertToSoapElement(domElement);
    }

    private  SOAPElement convertToSoapElement(Element element) throws SOAPException {

        if (element instanceof SOAPElement) {
            return (SOAPElement) element;
        } 

        SOAPElement copy = createElement(
                                element.getLocalName(), 
                                element.getPrefix(),
                                element.getNamespaceURI());

        Document ownerDoc = copy.getOwnerDocument();

        NamedNodeMap attrMap = element.getAttributes();
        for (int i=0; i < attrMap.getLength(); i++) {
            Attr nextAttr = (Attr)attrMap.item(i);
            Attr importedAttr = (Attr)ownerDoc.importNode(nextAttr, true);
            copy.setAttributeNodeNS(importedAttr);
        }


        NodeList nl = element.getChildNodes();
        for (int i=0; i < nl.getLength(); i++) {
            org.w3c.dom.Node next = nl.item(i);
            org.w3c.dom.Node imported = ownerDoc.importNode(next, true);
            copy.appendChild(imported);
        }
        
        return copy;
    }

    @Override
    public Detail createDetail() throws SOAPException {
        throw new UnsupportedOperationException();
    }

    @Override
    public  SOAPFault createFault(String reasonText, QName faultCode) throws SOAPException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SOAPFault createFault() throws SOAPException {
        throw new UnsupportedOperationException();
    }

}
