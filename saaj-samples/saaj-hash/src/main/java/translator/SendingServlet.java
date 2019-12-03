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
 * $Id: SendingServlet.java,v 1.5 2009-01-17 00:39:49 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:49 $
 */

package translator;

import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Sample servlet that is used for sending the message.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 * @author Daniel Kec
 */
public class SendingServlet extends HttpServlet {

    private static final String NS_PREFIX = "saaj";
    private static final String
            NS_URI = "http://java.sun.com/saaj/samples/hash-service";
    private static Logger logger = Logger.getLogger("Samples/HashService");

    // the hashes in different algorithms
    private Map<String, String> hashesMap = new HashMap<>();

    SOAPConnection conn;

    public void init(ServletConfig servletConfig) throws
            ServletException {
        super.init(servletConfig);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            conn = scf.createConnection();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to open a SOAPConnection", e);
        }

        try {

            // Extracting the text to be translated from the HTML form.
            String text = req.getParameter("translate");

            //Extracting proxy settings.
            String trAs = req.getParameter("trAs");

            if ((text == null) || (text.equals(""))) {
                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println("<HTML><BODY BGCOLOR=\"pink\">\n");
                out.println("<H4>No text was entered.");
                out.println("Please try again.</H4>\n</BODY></HTML>");

            } else {

                MessageFactory mf = MessageFactory.newInstance();
                SOAPMessage msg = mf.createMessage();

                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope envelope = sp.getEnvelope();

                SOAPHeader header = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                header.addHeaderElement(
                        envelope.createName("HashesAs", NS_PREFIX, NS_URI))
                        .addTextNode(trAs);

                body.addBodyElement(
                        envelope.createName("Text", NS_PREFIX, NS_URI))
                        .addTextNode(text);

                // Sending message.
                StringBuffer urlSB = new StringBuffer();
                urlSB.append(req.getScheme()).append("://").append(req.getServerName());
                urlSB.append(":").append(req.getServerPort()).append(req.getContextPath());
                String reqBase = urlSB.toString();

                URL endPoint = new URL(reqBase + "/hashingservice");

                SOAPMessage reply = conn.call(msg, endPoint);

                System.out.println("\n************** REPLY ***************\n");
                reply.writeTo(System.out);

                hashesMap.clear();
                if (trAs.equals("body")) {
                    extractFromBody(reply);
                } else {
                    extractFromAttachments(reply);
                }

                // Printing out the translated text.
                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println("<html><head><title>Hashing Service SAAJ Sample Application</title><style>\n" +
                        "html {font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif, Apple Color Emoji;}\n" +
                        "xmp {background-color: black;color: whitesmoke;border-radius: 4px;padding: 20px;}\n" +
                        ".lang {border-radius: 4px;padding: 20px;}\n" +
                        ".title {font-weight: bold;}\n" +
                        ".panel {border-radius: 4px; padding: 20px; margin: auto; width: min-content; border: 3px solid #87cefa;white-space: nowrap;}\n" +
                        "</style></head><body>" +
                        "<div  class='panel'>Hashes of <B>" + text + "</B> in different angorithms:<P>\n" +
                        "<div>");
                for (Map.Entry<String, String> e : hashesMap.entrySet()) {
                    out.println(MessageFormat.format("<div class=''lang''><div class=''title''> {0}: </div><div digest-type=''{0}''>{1}</div></div>\n", e.getKey(), e.getValue()));
                }
                out.println("</div></div> ");

                out.println("Request:<xmp>" + prettyXml(msg) + "</xmp>");
                if (trAs.equals("body")) {
                    out.println("Response:<xmp>" + prettyXml(reply) + "</xmp>");
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    reply.writeTo(baos);
                    out.println("Response:<xmp>" + baos.toString(StandardCharsets.UTF_8.toString()) + "</xmp>");
                }
                out.println("\n</body> \n</html>");

                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error in processing reply message " +
                    e.getMessage());
        }
    }

    private void extractFromBody(SOAPMessage reply) {
        try {

            SOAPEnvelope envelope = reply.getSOAPPart().getEnvelope();
            SOAPBody body = envelope.getBody();

            Iterator<Node> childremIt = body.getChildElements();
            while (childremIt.hasNext()) {
                SOAPElement childEl = (SOAPElement) childremIt.next();
                hashesMap.put(childEl.getAttributeValue(new QName(NS_URI, "digestType")), childEl.getValue());

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error in extracting text from the body");
        }
    }

    private void extractFromAttachments(SOAPMessage reply) {
        try {
            // Extracting the content from the message attachments.
            Iterator iterator = reply.getAttachments();
            while (iterator.hasNext()) {
                AttachmentPart attachmentPart = (AttachmentPart) iterator.next();
                ContentType contentType = new ContentType(attachmentPart.getContentType());
                if("text/plain".equals(contentType.getBaseType())){
                    hashesMap.put(contentType.getParameter("digestType"), (String) attachmentPart.getContent());
                }
            }
        } catch (Exception e) {
            logger.severe("Error in extracting text from attachments " + e.getMessage());
        }
    }

    private static String prettyXml(SOAPMessage msg) throws SOAPException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        transformer.transform(msg.getSOAPPart().getContent(), streamResult);
        return writer.toString();
    }

}




