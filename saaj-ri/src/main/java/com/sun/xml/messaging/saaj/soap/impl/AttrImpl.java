/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.messaging.saaj.soap.impl;

import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class AttrImpl implements Attr, jakarta.xml.soap.Node {

    private SOAPElement soapElement;

    final Attr delegate;

    AttrImpl(SOAPElement element, Attr attr) {
        this.soapElement = element;
        this.delegate = attr;
    }

    public String getNodeName() {
        return delegate.getNodeName();
    }

    public String getNodeValue() throws DOMException {
        return delegate.getNodeValue();
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        delegate.setNodeValue(nodeValue);
    }

    public short getNodeType() {
        return delegate.getNodeType();
    }

    public Node getParentNode() {
        return delegate.getParentNode();
    }

    public NodeList getChildNodes() {
        return delegate.getChildNodes();
    }

    public String getName() {
        return delegate.getName();
    }

    public Node getFirstChild() {
        return delegate.getFirstChild();
    }

    public boolean getSpecified() {
        return delegate.getSpecified();
    }

    public Node getLastChild() {
        return delegate.getLastChild();
    }

    public Node getPreviousSibling() {
        return delegate.getPreviousSibling();
    }

    public Node getNextSibling() {
        return delegate.getNextSibling();
    }

    public String getValue() {
        return delegate.getValue();
    }

    public NamedNodeMap getAttributes() {
        return delegate.getAttributes();
    }

    public Document getOwnerDocument() {
        return soapElement.getOwnerDocument();
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return delegate.insertBefore(newChild, refChild);
    }

    public void setValue(String value) throws DOMException {
        delegate.setValue(value);
    }

    public Element getOwnerElement() {
        return soapElement;
    }

    public TypeInfo getSchemaTypeInfo() {
        return delegate.getSchemaTypeInfo();
    }

    public boolean isId() {
        return delegate.isId();
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return delegate.replaceChild(newChild, oldChild);
    }

    public Node removeChild(Node oldChild) throws DOMException {
        return delegate.removeChild(oldChild);
    }

    public Node appendChild(Node newChild) throws DOMException {
        return delegate.appendChild(newChild);
    }

    public boolean hasChildNodes() {
        return delegate.hasChildNodes();
    }

    public Node cloneNode(boolean deep) {
        return delegate.cloneNode(deep);
    }

    public void normalize() {
        delegate.normalize();
    }

    public boolean isSupported(String feature, String version) {
        return delegate.isSupported(feature, version);
    }

    public String getNamespaceURI() {
        return delegate.getNamespaceURI();
    }

    public String getPrefix() {
        return delegate.getPrefix();
    }

    public void setPrefix(String prefix) throws DOMException {
        delegate.setPrefix(prefix);
    }

    public String getLocalName() {
        return delegate.getLocalName();
    }

    public boolean hasAttributes() {
        return delegate.hasAttributes();
    }

    public String getBaseURI() {
        return delegate.getBaseURI();
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        return delegate.compareDocumentPosition(other);
    }

    public String getTextContent() throws DOMException {
        return delegate.getTextContent();
    }

    public void setTextContent(String textContent) throws DOMException {
        delegate.setTextContent(textContent);
    }

    public boolean isSameNode(Node other) {
        return delegate.isSameNode(other);
    }

    public String lookupPrefix(String namespaceURI) {
        return delegate.lookupPrefix(namespaceURI);
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        return delegate.isDefaultNamespace(namespaceURI);
    }

    public String lookupNamespaceURI(String prefix) {
        return delegate.lookupNamespaceURI(prefix);
    }

    public boolean isEqualNode(Node arg) {
        return delegate.isEqualNode(arg);
    }

    public Object getFeature(String feature, String version) {
        return delegate.getFeature(feature, version);
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return delegate.setUserData(key, data, handler);
    }

    public Object getUserData(String key) {
        return delegate.getUserData(key);
    }

    @Override
    public void setParentElement(SOAPElement parent) throws SOAPException {
        this.soapElement = parent;
    }

    @Override
    public SOAPElement getParentElement() {
        return this.soapElement;
    }

    @Override
    public void detachNode() {
        this.soapElement.removeAttributeNode(delegate);
    }

    @Override
    public void recycleNode() {
        detachNode();
    }

}
