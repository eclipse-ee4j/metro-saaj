/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package namespace;

import java.io.ByteArrayInputStream;

import jakarta.xml.soap.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.util.SAAJUtil;
import junit.framework.TestCase;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/*
 * Tests to check for namespace rules being followed in SOAP message creation.
 */

public class NSDeclTest extends TestCase {

    public static final String NamespaceSpecNS =
        "http://www.w3.org/2000/xmlns/";


    public NSDeclTest(String name) {
        super(name);
    }

   public void testAttributes() throws Exception {

        String testDoc =
            "<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:xsd='http://www.w3.org/2001/XMLSchema'\n"
                + " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
                + " xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/'\n"
                + " xmlns:ns0='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns0:sayHello>\n"
                + "      <String_1>to Duke!</String_1>\n"
                + "    </ns0:sayHello>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";

        byte[] testDocBytes = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage sm = mf.createMessage();
        SOAPPart sp = sm.getSOAPPart();
        sp.setContent(strSource);
        Document doc = sp;
        
        // Workaround for SAAJ bug 4871599
        //sp.getEnvelope();

        // Uncomment the following to enable viewing DOM Node.
        // dumpDomNode(doc);
        // This test fails
        doTest(doc);

        // The following code which does not use SAAJ works correctly...
        byte[] testDocBytes2 = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais2 = new ByteArrayInputStream(testDocBytes2);
        InputSource is = new InputSource(bais2);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc2 = dbf.newDocumentBuilder().parse(is);
        // Uncomment to enable dumping to see the DOM Node
        // dumpDomNode(doc2);
        doTest(doc2);
    }

    private static boolean doTest(Document doc) {
        Element documentElement = doc.getDocumentElement();

        // Set dumpAttrs to true to print out root element attributes
        boolean dumpAttrs = false;
        if (dumpAttrs) {
            NamedNodeMap nnm = documentElement.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node node = nnm.item(i);
                System.err.println(
                    "type="
                        + node.getNodeType()
                        + ", name="
                        + node.getNodeName()
                        + ", namespaceUri="
                        + node.getNamespaceURI()
                        + ", localName="
                        + node.getLocalName());
            }
        }

        // DOM Level 2 Core says that this should return the "xmlns:env"
        // namespace declaration but it does not.  See first "Note" under
        // http://www.w3.org/TR/DOM-Level-2-Core/core.html#Namespaces-Considerations
        Attr xmlnsAttr =
            documentElement.getAttributeNodeNS(NamespaceSpecNS, "env");

        if (xmlnsAttr == null) {
            fail("Error: a DOM Attr should have been returned");
            return false;
        } else {
            //System.err.println("Success: a DOM Attr was returned");
        }
        return true;
    }

    static void dumpDomNode(Node node) throws TransformerException {
        System.err.println("==== DebugUtil.dumpDomNode(...) Start ====");
        DOMSource domSource = new DOMSource(node);
        TransformerFactory tf = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SAAJUtil.getSystemClassLoader());
        Transformer xform = null;
        xform = tf.newTransformer();
        xform.transform(domSource, new StreamResult(System.err));
        System.err.println();
        System.err.println("==== DebugUtil.dumpDomNode(...) End ====");
    }
    
    // test for bug id 4871599
    public void testGetDocElement() throws Exception {

        String testDoc =
            "<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'\n"
                + " xmlns:xsd='http://www.w3.org/2001/XMLSchema'\n"
                + " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
                + " xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/'\n"
                + " xmlns:ns0='http://example.com/wsdl'>\n"
                + "  <env:Body>\n"
                + "    <ns0:sayHello>\n"
                + "      <String_1>to Duke!</String_1>\n"
                + "    </ns0:sayHello>\n"
                + "  </env:Body>\n"
                + "</env:Envelope>\n";

        byte[] testDocBytes = testDoc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage sm = mf.createMessage();
        SOAPPart sp = sm.getSOAPPart();
        sp.setContent(strSource);
        
        // Uncomment following line for a workaround
        //sp.getEnvelope();
        Element docElement = sp.getDocumentElement();
        if (docElement == null)
            fail("The following value should not be null: " + docElement);
    }
 
    public static void main(String argv[]) {

        junit.textui.TestRunner.run(NSDeclTest.class);

    }

}
