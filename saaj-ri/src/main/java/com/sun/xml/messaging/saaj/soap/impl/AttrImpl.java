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

    @Override
    public String getNodeName() {
        return delegate.getNodeName();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return delegate.getNodeValue();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        delegate.setNodeValue(nodeValue);
    }

    @Override
    public short getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    @Override
    public Node getParentNode() {
        return delegate.getParentNode();
    }

    @Override
    public NodeList getChildNodes() {
        return delegate.getChildNodes();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Node getFirstChild() {
        return delegate.getFirstChild();
    }

    @Override
    public boolean getSpecified() {
        return delegate.getSpecified();
    }

    @Override
    public Node getLastChild() {
        return delegate.getLastChild();
    }

    @Override
    public Node getPreviousSibling() {
        return delegate.getPreviousSibling();
    }

    @Override
    public Node getNextSibling() {
        return delegate.getNextSibling();
    }

    @Override
    public String getValue() {
        return delegate.getValue();
    }

    @Override
    public NamedNodeMap getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Document getOwnerDocument() {
        return soapElement.getOwnerDocument();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return delegate.insertBefore(newChild, refChild);
    }

    @Override
    public void setValue(String value) throws DOMException {
        delegate.setValue(value);
    }

    @Override
    public Element getOwnerElement() {
        return soapElement;
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return delegate.getSchemaTypeInfo();
    }

    @Override
    public boolean isId() {
        return delegate.isId();
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return delegate.replaceChild(newChild, oldChild);
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return delegate.removeChild(oldChild);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return delegate.appendChild(newChild);
    }

    @Override
    public boolean hasChildNodes() {
        return delegate.hasChildNodes();
    }

    @Override
    public Node cloneNode(boolean deep) {
        return delegate.cloneNode(deep);
    }

    @Override
    public void normalize() {
        delegate.normalize();
    }

    @Override
    public boolean isSupported(String feature, String version) {
        return delegate.isSupported(feature, version);
    }

    @Override
    public String getNamespaceURI() {
        return delegate.getNamespaceURI();
    }

    @Override
    public String getPrefix() {
        return delegate.getPrefix();
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        delegate.setPrefix(prefix);
    }

    @Override
    public String getLocalName() {
        return delegate.getLocalName();
    }

    @Override
    public boolean hasAttributes() {
        return delegate.hasAttributes();
    }

    @Override
    public String getBaseURI() {
        return delegate.getBaseURI();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return delegate.compareDocumentPosition(other);
    }

    @Override
    public String getTextContent() throws DOMException {
        return delegate.getTextContent();
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        delegate.setTextContent(textContent);
    }

    @Override
    public boolean isSameNode(Node other) {
        return delegate.isSameNode(other);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return delegate.lookupPrefix(namespaceURI);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return delegate.isDefaultNamespace(namespaceURI);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return delegate.lookupNamespaceURI(prefix);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return delegate.isEqualNode(arg);
    }

    @Override
    public Object getFeature(String feature, String version) {
        return delegate.getFeature(feature, version);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return delegate.setUserData(key, data, handler);
    }

    @Override
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
