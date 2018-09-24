/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.soap.*;

/**
 * Sample servlet that is used for sending the message.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 *
 */
public class SendingServlet extends HttpServlet {

    private static final String NS_PREFIX = "saaj";
    private static final String
    NS_URI = "http://java.sun.com/saaj/samples/translation";
    private static Logger logger = Logger.getLogger("Samples/Translator");

    // the translations in different languages.
    private String french = "";
    private String italian = "";
    private String german = "";

    SOAPConnection conn;

    public void init(ServletConfig servletConfig) throws
    ServletException {
        super.init( servletConfig );
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException {

        try {
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            conn = scf.createConnection();
        } catch(Exception e) {
            logger.log(
                Level.SEVERE,
                "Unable to open a SOAPConnection", 
                e);
        }

    try {

        // Extracting the text to be translated from the HTML form.
        String text = req.getParameter("translate");

        //Extracting proxy settings.
        String host = req.getParameter("host");
        String port = req.getParameter("port");
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
            envelope.createName("ProxyHost", NS_PREFIX, NS_URI))
            .addTextNode(host);

            header.addHeaderElement(
            envelope.createName("ProxyPort", NS_PREFIX, NS_URI))
            .addTextNode(port);

            header.addHeaderElement(
            envelope.createName("TranslationAs", NS_PREFIX, NS_URI))
            .addTextNode(trAs);

            body.addBodyElement(
            envelope.createName("Text", NS_PREFIX, NS_URI))
            .addTextNode(text);

            // Sending message.
            StringBuffer urlSB=new StringBuffer();
            urlSB.append(req.getScheme()).append("://").append(req.getServerName());
            urlSB.append( ":" ).append( req.getServerPort() ).append( req.getContextPath() );
            String reqBase=urlSB.toString();

            URL endPoint = new URL( reqBase + "/translationservice");

            SOAPMessage reply = conn.call(msg, endPoint);

            System.out.println("\n************** REPLY ***************\n");
            reply.writeTo(System.out);
            FileOutputStream os = new FileOutputStream("reply.msg");
            reply.writeTo(os);
            os.close();

            if (trAs.equals("body")) {
                extractFromBody(reply);
            } else {
                extractFromAttachments(reply);
            }

            // Printing out the translated text.
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<HTML>\n" +
            "<BODY BGCOLOR=\"pink\">\n" +
            "Translation of <FONT COLOR=\"black\"> <B>" + text +
            "</B></FONT> in different languages:<P>\n" +
            "<TABLE BORDER=1 CELLPADDING=10 CELLSPACING=2>\n" +
            "<TR BGCOLOR=\"white\">\n" +
            "<TD> French </TD>\n" +
            "<TD> German </TD>\n" +
            "<TD> Italian </TD>\n" +
            "</TR> \n <TR>" +
            "<TD>" + french +  "</TD>\n" +
            "<TD>" + german + " </TD>\n" +
            "<TD>" + italian + "</TD>\n" +
            "\n </TR> \n" +
            "</TABLE> ");

            out.println("<BR> Request logged in file <I> request.msg </I>"+
            "\n Reply logged in file <I> reply.msg </I> "+
            "\n</BODY> \n</HTML>");

            conn.close();
        }
    }catch(Exception e) {
        e.printStackTrace();
        logger.severe("Error in processing reply message " +
        e.getMessage());
    }
}

private void extractFromBody(SOAPMessage reply) {
    try {

        SOAPEnvelope envelope = reply.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();

        french = extract(envelope, body, "FrenchText");
        german = extract(envelope, body, "GermanText");
        italian = extract(envelope, body, "ItalianText");

    } catch(Exception e) {
        e.printStackTrace();
        logger.severe("Error in extracting text from the body");
    }
}

private void extractFromAttachments(SOAPMessage reply) {
    try {
        // Extracting the content from the message attachments.
        Iterator iterator = reply.getAttachments();

        if (iterator.hasNext())
            french  = (String)
            ((AttachmentPart) iterator.next()).getContent();

        if (iterator.hasNext())
            german  = (String)
            ((AttachmentPart) iterator.next()).getContent();

        if (iterator.hasNext())
            italian  = (String)
            ((AttachmentPart) iterator.next()).getContent();

    } catch(Exception e) {
        logger.severe("Error in extracting text from attachments " +
        e.getMessage());
    }
}

// extract the value of the first child element under element
// with this localname
private String extract(SOAPEnvelope envelope, SOAPElement element, String localname)
throws SOAPException {

    Iterator it = element.getChildElements(
    envelope.createName(localname, NS_PREFIX, NS_URI));

    if( it.hasNext()) {
        SOAPElement e = (SOAPElement) it.next();
        return e.getValue();
    }
    logger.severe("Could not extract " + localname + " from message");
    return null;
}

}




