/*
 * Copyright (c) 1997, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/*
 * $Id: SAAJServlet.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */


package com.sun.xml.messaging.soap.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;


/**
 * The superclass for components that
 * live in a servlet container that receives SAAJ messages.
 * A <code>SAAJServlet</code> object is notified of a message's arrival
 * using the HTTP-SOAP binding.
 * <P>
 * The <code>SAAJServlet</code> class is a support/utility class and is
 * provided purely as a convenience.  It is not a mandatory component, and
 * there is no requirement that it be implemented or extended.
 * <P>
 */
public abstract class SAAJServlet extends HttpServlet {

    /**
     * A <code>SOAPConnection</code> for use of the <code>SAAJServlet</code>.
     */
    protected SOAPConnection conn = null;

    /**
     * The <code>MessageFactory</code> object that will be used internally
     * to create the <code>SOAPMessage</code> object to be passed to the
     * method <code>onMessage</code>. This new message will contain the data
     * from the message that was posted to the servlet.
     * Based on the incoming message's content type a SOAP 1.1 or 1.2 Message
     * reply is created. A dynamic messageFactory is created which automatically
     * figures out the SOAP version.
     */
    protected MessageFactory msgFactory = null;

    /**
     * Initializes this <code>SAAJServlet</code> object using the given
     * <code>ServletConfig</code> object and initializing the
     * <code>msgFactory</code> field with a default
     * <code>MessageFactory</code> object.
     *
     * @param servletConfig the <code>ServletConfig</code> object to be
     *        used in initializing this <code>SAAJServlet</code> object
     */
    public void init(ServletConfig servletConfig)
            throws ServletException {
        super.init(servletConfig);
        try {
            msgFactory = MessageFactory.newInstance();
            SOAPConnectionFactory connFactory =
                SOAPConnectionFactory.newInstance();
            conn = connFactory.createConnection();
        } catch (SOAPException ex) {
            throw new ServletException("Unable to create message factory"
                    + ex.getMessage());
        }
    }

    /**
     * Sets this <code>SAAJServlet</code> object's <code>msgFactory</code>
     * field with the given <code>MessageFactory</code> object.
     * A <code>MessageFactory</code> object for a particular profile needs to
     * be set before a message is received in order for the message to be
     * successfully internalized.
     *
     * @param msgFactory the <code>MessageFactory</code> object that will
     *        be used to create the <code>SOAPMessage</code> object that
     *        will be used to internalize the message that was posted to
     *        the servlet
     */
    public void setMessageFactory(MessageFactory msgFactory) {
        this.msgFactory = msgFactory;
    }

    /**
     * Returns a <code>MimeHeaders</code> object that contains the headers
     * in the given <code>HttpServletRequest</code> object.
     *
     * @param req the <code>HttpServletRequest</code> object that a
     *        messaging provider sent to the servlet
     * @return a new <code>MimeHeaders</code> object containing the headers
     *         in the message sent to the servlet
     */
    protected static MimeHeaders getHeaders(HttpServletRequest req) {
        Enumeration enumeration = req.getHeaderNames();
        MimeHeaders headers = new MimeHeaders();

        while (enumeration.hasMoreElements()) {
            String headerName = (String) enumeration.nextElement();
            String headerValue = req.getHeader(headerName);

            StringTokenizer values = new StringTokenizer(headerValue, ",");

            while (values.hasMoreTokens()) {
                headers.addHeader(headerName, values.nextToken().trim());
            }
        }

        return headers;
    }

    /**
     * Sets the given <code>HttpServletResponse</code> object with the
     * headers in the given <code>MimeHeaders</code> object.
     *
     * @param headers the <code>MimeHeaders</code> object containing the
     *        the headers in the message sent to the servlet
     * @param res the <code>HttpServletResponse</code> object to which the
     *        headers are to be written
     * @see #getHeaders
     */
    protected static void putHeaders(MimeHeaders headers, HttpServletResponse res) {
        Iterator it = headers.getAllHeaders();

        while (it.hasNext()) {
            MimeHeader header = (MimeHeader) it.next();

            String[] values = headers.getHeader(header.getName());

            if (values.length == 1) {
                res.setHeader(header.getName(), header.getValue());
            } else {
                StringBuffer concat = new StringBuffer();
                int i = 0;

                while (i < values.length) {
                    if (i != 0) {
                        concat.append(',');
                    }
                    concat.append(values[i++]);
                }
                res.setHeader(header.getName(), concat.toString());
            }
        }
    }

    /**
     * Internalizes the given <code>HttpServletRequest</code> object
     * and writes the reply to the given <code>HttpServletResponse</code>
     * object.
     * <P>
     * Note that the value for the <code>msgFactory</code> field will be used to
     * internalize the message. This ensures that the message
     * factory for the correct profile is used.
     *
     * @param req the <code>HttpServletRequest</code> object containing the
     *        message that was sent to the servlet
     * @param resp the <code>HttpServletResponse</code> object to which the
     *        response to the message will be written
     * @throws ServletException if there is a servlet error
     * @throws IOException if there is an input or output error
     */
    public void doPost(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Get all the headers from the HTTP request.
            MimeHeaders headers = getHeaders(req);

            // Get the body of the HTTP request.
            InputStream is = req.getInputStream();

            // Now internalize the contents of a HTTP request and
            // create a SOAPMessage
            SOAPMessage msg = msgFactory.createMessage(headers, is);

            SOAPMessage reply = null;

            // There are no replies in case of an OnewayListener.
            reply = onMessage(msg);
        
            if (reply != null) {

                // Need to saveChanges 'cos we're going to use the
                // MimeHeaders to set HTTP response information. These
                // MimeHeaders are generated as part of the save.

                if (reply.saveRequired()) {
                    reply.saveChanges();
                }

                resp.setStatus(HttpServletResponse.SC_OK);

                putHeaders(reply.getMimeHeaders(), resp);

                // Write out the message on the response stream.
                OutputStream os = resp.getOutputStream();

                reply.writeTo(os);

                os.flush();

            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Exception ex) {
            throw new ServletException("SAAJ POST failed " + ex.getMessage());
        }
    }
    
    public abstract SOAPMessage onMessage(SOAPMessage message);
    
}
