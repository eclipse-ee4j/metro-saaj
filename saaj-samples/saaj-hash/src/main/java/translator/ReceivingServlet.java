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
 * $Id: ReceivingServlet.java,v 1.5 2009-01-17 00:39:49 ramapulavarthi Exp $
 * $Revision: 1.5 $s
 * $Date: 2009-01-17 00:39:49 $
 */


package translator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample servlet that receives messages containing text to be translated,
 * does the translation, and sends back a message with translations as
 * attachments -or- in SOAPBody of the reply message.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 * @author Daniel Kec
 *
 */

public class ReceivingServlet extends HttpServlet {

    private static String NS_PREFIX = "saaj";
    private static String
    NS_URI = "http://java.sun.com/saaj/samples/hash-service";
    private static Logger logger = Logger.getLogger("Samples/HashService");

    private Map<String,String> hashesMap = new HashMap<>();

    static MessageFactory msgFactory = null;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            // Initialize it to the default.
            msgFactory = MessageFactory.newInstance();
        } catch (SOAPException ex) {
            throw new ServletException("Unable to create message factory"
                + ex.getMessage());
        }
    }

    public void doPost( HttpServletRequest req,
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

        } else
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

    } catch (Exception ex) {
        throw new ServletException("POST failed "+ex.getMessage());
    }
    }

    static MimeHeaders getHeaders(HttpServletRequest req) {

    Enumeration enumeration = req.getHeaderNames();
    MimeHeaders headers = new MimeHeaders();

    while (enumeration.hasMoreElements()) {
        String headerName = (String)enumeration.nextElement();
        String headerValue = req.getHeader(headerName);

        StringTokenizer values = new StringTokenizer(headerValue, ",");
        while (values.hasMoreTokens())
        headers.addHeader(headerName, values.nextToken().trim());
    }

    return headers;
    }

    static void putHeaders(MimeHeaders headers, HttpServletResponse res) {

    Iterator it = headers.getAllHeaders();
    while (it.hasNext()) {
        MimeHeader header = (MimeHeader)it.next();

        String[] values = headers.getHeader(header.getName());
        if (values.length == 1)
        res.setHeader(header.getName(), header.getValue());
        else {
        StringBuffer concat = new StringBuffer();
        int i = 0;
        while (i < values.length) {
            if (i != 0)
            concat.append(',');
            concat.append(values[i++]);
        }

        res.setHeader(header.getName(),
        concat.toString());
            }
        }
    }

    // This is the application code for handling the message.. Once the
    // message is received the application can retrieve the soap part, the
    // attachment part if there are any, or any other information from the
    // message.

    public SOAPMessage onMessage(SOAPMessage message) {
        SOAPMessage msg = null;

        try {

            System.out.println("\n************** REQUEST ***************\n");

            message.writeTo(System.out);

            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            String hashesAs = extract(envelope, header, "HashesAs");

            // Extracting text to be hashed from SOAPBody.
            String text = extract(envelope, body, "Text");

            HashingService ts = new HashingService();
            hashesMap.clear();
            Security.getAlgorithms(MessageDigest.class.getSimpleName())
                    .forEach(a -> hashesMap.put(a, ts.makeHash(text, a)));

            // Create reply message
            msg = msgFactory.createMessage();

            if ("body".equals(hashesAs)) {
                addInSOAPBody(msg);
            } else {
                addAsAttachments(msg);
            }

            if (msg.saveRequired())
                msg.saveChanges();

        } catch(Exception e) {
            logger.log(
                Level.SEVERE,
                "Error in processing or replying to a message",
                e);
        }

        return msg;
    }

    private void addInSOAPBody(SOAPMessage msg) {
        try {
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            SOAPBody body = envelope.getBody();

            // Adding the hashes of the text to SOAPBody.
            for (Map.Entry<String, String> e : hashesMap.entrySet()) {
                body.addBodyElement(envelope
                        .createName("digest", NS_PREFIX, NS_URI))
                        .addTextNode(e.getValue())
                        .addAttribute(new QName(NS_URI, "digestType", NS_PREFIX), e.getKey());
            }

        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error in adding hashes to the body", e);
        }
    }

    private void addAsAttachments(SOAPMessage msg) {
        // Adding the hashes as attachments.s
        try {

            for (Map.Entry<String, String> e : hashesMap.entrySet()) {
                // Remove forbidden chars specified in https://www.w3.org/Protocols/rfc1341/4_Content-Type.html
                String digestType = e.getKey().replaceAll("[\\(\\)<>@,;:\\\"/\\[\\]?.= \\u0000-\\u001f]", "_");

                AttachmentPart part = msg.createAttachmentPart(e.getValue(),
                        String.format("text/plain; charset=ISO-8859-1; digestType=%s;", digestType));
                msg.addAttachmentPart(part);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in adding hashes as attachments", e);
        }
    }

    // extract the value of the first child element under element
    // with this localname
    private String extract(SOAPEnvelope envelope, SOAPElement element, String localname)
            throws SOAPException {

        Iterator it = element.getChildElements(
                envelope.createName(localname, NS_PREFIX, NS_URI));

        if (it.hasNext()) {
            SOAPElement e = (SOAPElement) it.next();
            return e.getValue();
        }
        logger.severe("Could not extract " + localname + " from message");
        return null;
    }

}



