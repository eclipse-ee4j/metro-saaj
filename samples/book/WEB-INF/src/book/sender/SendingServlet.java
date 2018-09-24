/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package book.sender;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;

import org.w3c.dom.Document;

/**
 * Sample servlet that is used for sending the message.
 *
 * @author Krishna Meduri (krishna.meduri@sun.com)
 */

public class SendingServlet extends HttpServlet {

    String to = null;
    String data = null;
    ServletContext servletContext;

    private static Logger logger = Logger.getLogger("Samples/Book");

    // Connection to send messages.
    private SOAPConnection con;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init( servletConfig );
        servletContext = servletConfig.getServletContext();

        try {
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            con = scf.createConnection();
        } catch(Exception e) {
            logger.log(
                Level.SEVERE,
                "Unable to open a SOAPConnection", 
                e);
        }

        InputStream in
        = servletContext.getResourceAsStream("/WEB-INF/address.properties");

        if (in != null) {
            Properties props = new Properties();

            try {
                props.load(in);

                to = props.getProperty("to");
                data = props.getProperty("data");
            } catch (IOException ex) {
                // Ignore
            }
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException {

        String retval ="<html> <H4>";

        try {
            // Create a message factory.
            MessageFactory mf = MessageFactory.newInstance();

            // Create a message from the message factory.
            SOAPMessage msg = mf.createMessage();

            // Message creation takes care of creating the SOAPPart - a
            // required part of the message as per the SOAP 1.1
            // specification.
            SOAPPart sp = msg.getSOAPPart();

            // Retrieve the envelope from the soap part to start building
            // the soap message.
            SOAPEnvelope envelope = sp.getEnvelope();

            // Get the soap SOAP Header from the message
            SOAPHeader header = msg.getSOAPHeader();


            Name book = envelope.createName("book", "b",
                                        "http://saaj.sample/book");
            SOAPHeaderElement bookHeaderElem =
                                header.addHeaderElement(book);
            bookHeaderElem.setActor("http://saaj.sample/receiver");
            bookHeaderElem.setMustUnderstand(true);

            Name isbn = envelope.createName("isbn", "b",
                                        "http://saaj.sample/book");
            SOAPElement isbnElem = bookHeaderElem.addChildElement(isbn);
            isbnElem.addTextNode("9-999-99999-9");

	    SOAPFactory soapFactory = SOAPFactory.newInstance();
	    Name edition = soapFactory.createName("edition", "b",
						  "http://saaj.sample/book");
	    SOAPElement editionElem = bookHeaderElem.addChildElement(edition);
	    editionElem.addTextNode("2");

            // Get the soap SOAP Body from the message
            SOAPBody body = msg.getSOAPBody();

            StringBuffer urlSB=new StringBuffer("http://");
            urlSB.append(req.getServerName());
            urlSB.append( ":" ).append( req.getServerPort() );
	    urlSB.append( req.getContextPath() );
            String reqBase=urlSB.toString();

            DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document bookInfo = docBuilder.parse(reqBase + "/bookinfo.xml");

            body.addDocument(bookInfo);


            Name author = envelope.createName("author", "a",
                                        "http://saaj.sample/author");
            SOAPBodyElement authorElem = body.addBodyElement(author);
            SOAPElement authorName = authorElem.addChildElement("name", "a",
                                        "http://saaj.sample/author");
            authorName.addTextNode("John Rhodes");

            if(data==null) {
                data=reqBase + "/index.html";
            }

            // Want to set an attachment from the following url.
            //Get context
            URL url = new URL(data);

            AttachmentPart ap = msg.createAttachmentPart(new DataHandler(url));

            ap.setContentType("text/html");

            // Add the attachment part to the message.
            msg.addAttachmentPart(ap);

            // Create an endpoint for the recipient of the message.
            if(to==null) {
                to=reqBase + "/receiver";
            }

            URL urlEndpoint = new URL(to);

            System.err.println("Sending message to URL: "+urlEndpoint);
            System.err.println("Sent message is logged in \"sent.msg\"");

            retval += " Sent message (check \"sent.msg\") and ";

            FileOutputStream sentFile = new FileOutputStream("sent.msg");
            msg.writeTo(sentFile);
            sentFile.close();

            // Send the message to the provider using the connection.
            SOAPMessage reply = con.call(msg, urlEndpoint);

            if (reply != null) {
                FileOutputStream replyFile = new FileOutputStream("reply.msg");
                reply.writeTo(replyFile);
                replyFile.close();
                System.err.println("Reply logged in \"reply.msg\"");
                retval += " received reply (check \"reply.msg\").</H4> </html>";

            } else {
                System.err.println("No reply");
                retval += " no reply was received. </H4> </html>";
            }

        } catch(Throwable e) {
            e.printStackTrace();
            logger.severe("Error in constructing or sending message "
            +e.getMessage());
            retval += " There was an error " +
            "in constructing or sending message. </H4> </html>";
        }

        try {
            OutputStream os = resp.getOutputStream();
            os.write(retval.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe( "Error in outputting servlet response "
            + e.getMessage());
        }
    }

}
