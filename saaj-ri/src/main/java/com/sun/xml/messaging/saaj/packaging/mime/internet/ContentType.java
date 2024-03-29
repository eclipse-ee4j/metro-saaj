/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/*
 * @(#)ContentType.java       1.7 02/03/27
 */



package com.sun.xml.messaging.saaj.packaging.mime.internet;


/**
 * This class represents a MIME ContentType value. It provides
 * methods to parse a ContentType string into individual components
 * and to generate a MIME style ContentType string.
 *
 * @version 1.7, 02/03/27
 * @author  John Mani
 */
public final class ContentType {

    private String primaryType;	// primary type
    private String subType;	// subtype
    private ParameterList list;	// parameter list

    /**
     * No-arg Constructor.
     */
    public ContentType() { }

    /**
     * Constructor.
     *
     * @param	primaryType	primary type
     * @param	subType	subType
     * @param	list	ParameterList
     */
    public ContentType(String primaryType, String subType,
                       ParameterList list) {
        this.primaryType = primaryType;
        this.subType = subType;
        if (list == null)
            list = new ParameterList();
        this.list = list;
    }

    /**
     * Constructor that takes a Content-Type string. The String
     * is parsed into its constituents: primaryType, subType
     * and parameters. A ParseException is thrown if the parse fails. 
     *
     * @param	s	the Content-Type string.
     * @exception	ParseException if the parse fails.
     */
    public ContentType(String s) throws ParseException {
	HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
	HeaderTokenizer.Token tk;

	// First "type" ..
	tk = h.next();
	if (tk.getType() != HeaderTokenizer.Token.ATOM)
	    throw new ParseException();
	primaryType = tk.getValue();

	// The '/' separator ..
	tk = h.next();
	if ((char)tk.getType() != '/')
	    throw new ParseException();

	// Then "subType" ..
	tk = h.next();
	if (tk.getType() != HeaderTokenizer.Token.ATOM)
	    throw new ParseException();
	subType = tk.getValue();

	// Finally parameters ..
	String rem = h.getRemainder();
	if (rem != null)
	    list = new ParameterList(rem);
    }

    public ContentType copy() {
        return new ContentType(primaryType,subType,list.copy());
    }

    /**
     * Return the primary type.
     * @return the primary type
     */
    public String getPrimaryType() {
	return primaryType;
    }

    /**
     * Return the subType.
     * @return the subType
     */
    public String getSubType() {
	return subType;
    }

    /**
     * Return the MIME type string, without the parameters.
     * The returned value is basically the concatenation of
     * the primaryType, the '/' character and the secondaryType.
     *
     * @return the type
     */
    public String getBaseType() {
	return primaryType + '/' + subType;
    }

    /**
     * Return the specified parameter value. Returns <code>null</code>
     * if this parameter is absent.
     * @param name parameter name
     * @return	parameter value
     */
    public String getParameter(String name) {
	if (list == null)
	    return null;

	return list.get(name);
    }

    /**
     * Return a ParameterList object that holds all the available 
     * parameters. Returns null if no parameters are available.
     *
     * @return	ParameterList
     */
    public ParameterList getParameterList() {
	return list;
    }

    /**
     * Set the primary type. Overrides existing primary type.
     * @param	primaryType	primary type
     */
    public void setPrimaryType(String primaryType) {
	this.primaryType = primaryType;
    }

    /**
     * Set the subType. Overrides existing subType
     * @param	subType	subType
     */
    public void setSubType(String subType) {
	this.subType = subType;
    }

    /**
     * Set the specified parameter. If this parameter already exists,
     * it is replaced by this new value.
     *
     * @param	name	parameter name
     * @param	value	parameter value
     */
    public void setParameter(String name, String value) {
	if (list == null)
	    list = new ParameterList();

	list.set(name, value);
    }

    /**
     * Set a new ParameterList.
     * @param	list	ParameterList
     */
    public void setParameterList(ParameterList list) {
	this.list = list;
    }

    /**
     * Retrieve a RFC2045 style string representation of
     * this Content-Type. Returns <code>null</code> if
     * the conversion failed.
     *
     * @return	RFC2045 style string
     */
    @Override
    public String toString() {
	if (primaryType == null || subType == null) // need both
	    return null;

	StringBuilder sb = new StringBuilder();
	sb.append(primaryType).append('/').append(subType);
	if (list != null)
        // Http Binding section of the "SOAP with attachments" specification says,
        // "SOAP message senders should send Content-Type headers on a single long line."
        // (http://www.w3.org/TR/SOAP-attachments#HTTPBinding)
            sb.append(list.toString());
        
	return sb.toString();
    }

    /**
     * Match with the specified ContentType object. This method
     * compares <strong>only the <code>primaryType</code> and 
     * <code>primaryType</code> </strong>. The parameters of both operands
     * are ignored. <p>
     *
     * For example, this method will return <code>true</code> when
     * comparing the ContentTypes for <strong>"text/plain"</strong>
     * and <strong>"text/plain; charset=foobar"</strong>.
     *
     * If the <code>subType</code> of either operand is the special
     * character '*', then the subtype is ignored during the match. 
     * For example, this method will return <code>true</code> when 
     * comparing the ContentTypes for <strong>"text/plain"</strong> 
     * and <strong>"text/*" </strong>
     *
     * @param   cType to compare this against
     * @return true if <code>primaryType</code> and <code>subType</code>
     * match specified content type.
     */
    public boolean match(ContentType cType) {
	// Match primaryType
	if (!primaryType.equalsIgnoreCase(cType.getPrimaryType()))
	    return false;
	
	String sType = cType.getSubType();

	// If either one of the subTypes is wildcarded, return true
	if ((subType.charAt(0) == '*') || (sType.charAt(0) == '*'))
	    return true;
	
	// Match subType
	if (!subType.equalsIgnoreCase(sType))
	    return false;

	return true;
    }

    /**
     * Match with the specified content-type string. This method
     * compares <strong>only the <code>primaryType</code> and 
     * <code>subType</code> </strong>.
     * The parameters of both operands are ignored. <p>
     *
     * For example, this method will return <code>true</code> when
     * comparing the ContentType for <strong>"text/plain"</strong>
     * with <strong>"text/plain; charset=foobar"</strong>.
     *
     * If the <code>subType</code> of either operand is the special 
     * character '*', then the subtype is ignored during the match. 
     * For example, this method will return <code>true</code> when 
     * comparing the ContentType for <strong>"text/plain"</strong> 
     * with <strong>"text/*" </strong>
     *
     * @param s content type
     * @return true if <code>primaryType</code> and <code>subType</code>
     * match specified content type.
     */
    public boolean match(String s) {
	try {
	    return match(new ContentType(s));
	} catch (ParseException pex) {
	    return false;
	}
    }
}
