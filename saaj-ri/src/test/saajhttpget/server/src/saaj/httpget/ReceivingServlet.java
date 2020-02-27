/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package saaj.httpget;

import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import jakarta.xml.soap.*;


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
public  class ReceivingServlet extends HttpServlet {


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
    @Override
    public void init(ServletConfig servletConfig)
            throws ServletException {
        super.init(servletConfig);
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
    @Override
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
    
    public SOAPMessage onMessage(SOAPMessage message) {
        return message;
    }

    static int i=1;
    @Override
    public void doGet(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (i % 2 == 0) {
            doGet2(req, resp); 
        } else if (i % 3 == 0) {
            doGet3(req, resp);
        } else {
            doGet1(req, resp);
        }
    }

    public void doGet1(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        i++;
        try {
        System.out.println("HTTP GET1 ................");
        MessageFactory factory = MessageFactory.newInstance();
        MimeHeaders headers = new MimeHeaders();
        headers.setHeader("Content-Type","text/xml");

        ByteArrayInputStream strStream= new ByteArrayInputStream("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><abc:find_service xmlns:abc=\"urn:uddi-org:api_v2\"><name/></abc:find_service></SOAP-ENV:Body></SOAP-ENV:Envelope>".getBytes("utf-8"));

        SOAPMessage msg = factory.createMessage(headers, strStream);
        resp.setStatus(HttpServletResponse.SC_OK);
        putHeaders(msg.getMimeHeaders(), resp);
        // Write out the message on the response stream.
        OutputStream os = resp.getOutputStream();
        msg.writeTo(os);
        os.flush();
        } catch (Exception e) {

        }
    }

    public void doGet2(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        i++;
        try {

          System.out.println("HTTP GET2 ................");
          PrintWriter out = resp.getWriter();

          out.println( "<html><head><title>Native Hello </title></head>" );
          out.println( "<body>" );
          out.println( "<h1>Hello World</h1>" );
          out.println( "</body></html>" );
          resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {

        }
     }

     
    public void doGet3(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        i++;
        try {
        System.out.println("HTTP GET3 ................");
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        MimeHeaders headers = new MimeHeaders();
        headers.setHeader("Content-Type",SOAPConstants.SOAP_1_2_CONTENT_TYPE);

        ByteArrayInputStream strStream= new ByteArrayInputStream("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"><SOAP-ENV:Header/><SOAP-ENV:Body><abc:find_service xmlns:abc=\"urn:uddi-org:api_v2\"><name/></abc:find_service></SOAP-ENV:Body></SOAP-ENV:Envelope>".getBytes("utf-8"));

        SOAPMessage msg = factory.createMessage(headers, strStream);
        resp.setStatus(HttpServletResponse.SC_OK);
        putHeaders(msg.getMimeHeaders(), resp);
        // Write out the message on the response stream.
        OutputStream os = resp.getOutputStream();
        msg.writeTo(os);
        os.flush();
        } catch (Exception e) {

        }
    }
}
