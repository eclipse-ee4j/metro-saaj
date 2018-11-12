/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap;

import com.sun.xml.messaging.saaj.soap.impl.NodeListImpl;
import org.w3c.dom.*;

/**
 * SAAJ wrapper for {@link DocumentFragment}
 *
 * @author Yan GAO.
 */
public class SOAPDocumentFragment implements DocumentFragment {

    private SOAPDocumentImpl soapDocument;
    private DocumentFragment documentFragment;

    public SOAPDocumentFragment(SOAPDocumentImpl ownerDoc) {
        this.soapDocument = ownerDoc;
        this.documentFragment = soapDocument.getDomDocument().createDocumentFragment();
    }

    public SOAPDocumentFragment(SOAPDocumentImpl soapDocument, DocumentFragment documentFragment) {
        this.soapDocument = soapDocument;
        this.documentFragment = documentFragment;
    }

    public SOAPDocumentFragment() {}

    @Override
    public boolean hasAttributes() {
        return documentFragment.hasAttributes();
    }

    @Override
    public boolean isSameNode(Node other) {
        return documentFragment.isSameNode(getDomNode(other));
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return documentFragment.lookupNamespaceURI(prefix);
    }

    @Override
    public Node getParentNode() {
        return soapDocument.findIfPresent(documentFragment.getParentNode());
    }

    @Override
    public Node getFirstChild() {
        return soapDocument.findIfPresent(documentFragment.getFirstChild());
    }

    @Override
    public Object getUserData(String key) {
        return documentFragment.getUserData(key);
    }

    @Override
    public String getTextContent() throws DOMException {
        return documentFragment.getTextContent();
    }
    @Override
    public short getNodeType() {
        return documentFragment.getNodeType();
    }

    public Node getDomNode(Node node) {
        return soapDocument.getDomNode(node);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        Node node = soapDocument.importNode(newChild, true);
        return soapDocument.findIfPresent(documentFragment.appendChild(getDomNode(node)));
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return soapDocument.findIfPresent(documentFragment.removeChild(getDomNode(oldChild)));
    }

    @Override
    public NamedNodeMap getAttributes() {
        return documentFragment.getAttributes();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return documentFragment.compareDocumentPosition(getDomNode(other));
    }
    @Override
    public void setTextContent(String textContent) throws DOMException {
        documentFragment.setTextContent(textContent);
    }
    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        Node node = soapDocument.importNode(newChild, true);
        return soapDocument.findIfPresent(documentFragment.insertBefore(getDomNode(node), getDomNode(refChild)));
    }
    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return documentFragment.setUserData(key, data, handler);
    }
    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return documentFragment.isDefaultNamespace(namespaceURI);
    }

    @Override
    public Node getLastChild() {
        return soapDocument.findIfPresent(documentFragment.getLastChild());
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        documentFragment.setPrefix(prefix);
    }
    @Override
    public String getNodeName() {
        return documentFragment.getNodeName();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        documentFragment.setNodeValue(nodeValue);
    }
    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        Node node = soapDocument.importNode(newChild, true);
        return soapDocument.findIfPresent(documentFragment.replaceChild(getDomNode(node), getDomNode(oldChild)));
    }
    @Override
    public String getLocalName() {
        return documentFragment.getLocalName();
    }

    @Override
    public void normalize() {
        documentFragment.normalize();
    }

    @Override
    public Node cloneNode(boolean deep) {
        Node node= documentFragment.cloneNode(deep);
        soapDocument.registerChildNodes(node, deep);
        return soapDocument.findIfPresent(node);
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return documentFragment.isSupported(feature, version);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return documentFragment.isEqualNode(getDomNode(arg));
    }

    @Override
    public boolean hasChildNodes() {
        return documentFragment.hasChildNodes();
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return documentFragment.lookupPrefix(namespaceURI);
    }

    @Override
    public String getNodeValue() throws DOMException {
        return documentFragment.getNodeValue();
    }
    @Override
    public Document getOwnerDocument() {
        return soapDocument;
    }
    @Override
    public Object getFeature(String feature, String version) {
        return documentFragment.getFeature(feature, version);
    }

    @Override
    public Node getPreviousSibling() {
        return soapDocument.findIfPresent(documentFragment.getPreviousSibling());
    }

    @Override
    public NodeList getChildNodes() {
        return new NodeListImpl(soapDocument, documentFragment.getChildNodes());
    }

    @Override
    public String getBaseURI() {
        return documentFragment.getBaseURI();
    }

    @Override
    public Node getNextSibling() {
        return soapDocument.findIfPresent(documentFragment.getNextSibling());
    }

    @Override
    public String getPrefix() {
        return documentFragment.getPrefix();
    }

    @Override
    public String getNamespaceURI() {
        return documentFragment.getNamespaceURI();
    }
    public Document getSoapDocument() {
        return soapDocument;
    }

    public Node getDomNode() {
        return documentFragment;
    }
}
