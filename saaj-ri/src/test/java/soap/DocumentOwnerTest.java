package soap;

import java.io.InputStream;
import com.sun.xml.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.messaging.saaj.soap.impl.AttrImpl;
import com.sun.xml.messaging.saaj.soap.impl.SOAPTextImpl;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPPart;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.TestCase;
import org.w3c.dom.Node;

/**
 * Original source taken from https://github.com/coheigea/testcases/tree/master/misc/saaj
 *
 * Verifies issue #165
 */
public class DocumentOwnerTest extends TestCase {

    /**
     * Reproduces the issue identified in #165
     */
    public void testOwnerDocumentConsistency() throws Exception {
        Document signedDocument = read("signed-document.xml");

        Element refElement = (Element) signedDocument.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Reference").item(0);

        Attr uriAttr = refElement.getAttributeNodeNS(null, "URI");
        Document uriOwnerDocument = uriAttr.getOwnerDocument();
        Document refOwnerDocument = refElement.getOwnerDocument();

        assertSame("Inconsistent document",  refOwnerDocument, uriOwnerDocument);
        assertEquals("Unexpected document type", SOAPDocumentImpl.class, uriOwnerDocument.getClass());
    }

    /**
     * Tests further issues identified as part of the fix for #165. When SAAJ was decoupled from Xerces it seems to have
     * introduced several problems - notably, cloning of some SAAJ elements results in raw Xerces nodes. This results
     * in functions within Apache Santuario (canonicalization of documents) and within DSig to produce incorrect
     * outcomes or fail entirely.
     */
    public void testClonePreservesSoapElements() throws Exception {
        SOAPPart document = read("signed-document.xml");
        Element signature = (Element) document.getEnvelope().getChildNodes().item(1);
        Element signedInfo = getElement(signature, "ds:SignedInfo");
        Element canonicalizationMethod = getElement(signedInfo, "ds:CanonicalizationMethod");
        Node canonicalizationAttr = canonicalizationMethod.getAttributes().item(0);
        Node digestValue = getElement(getElement(signedInfo, "ds:Reference"), "ds:DigestValue")
                .getFirstChild();

        assertEquals(AttrImpl.class, canonicalizationAttr.getClass());
        assertEquals(SOAPTextImpl.class, digestValue.getClass());

        Document clonedDocument = (Document) document.getOwnerDocument().cloneNode(true);
        assertEquals(SOAPDocumentImpl.class, clonedDocument.getClass());

        Node clonedEnvelope = clonedDocument.getElementsByTagName("SOAP-ENV:Envelope").item(0);
        assertTrue(SOAPEnvelope.class.isAssignableFrom(clonedEnvelope.getClass()));

        Element clonedSignature = (Element) ((SOAPEnvelope)clonedEnvelope).getChildNodes().item(1);
        Element clonedSignedInfo = getElement(clonedSignature, "ds:SignedInfo");
        Element clonedCanonicalizationMethod = getElement(clonedSignedInfo, "ds:CanonicalizationMethod");
        Node clonedCanonicalizationAttr = clonedCanonicalizationMethod.getAttributes().item(0);
        Node clonedDigestValue = getElement(getElement(clonedSignedInfo, "ds:Reference"), "ds:DigestValue")
                .getFirstChild();

        assertEquals(AttrImpl.class, clonedCanonicalizationAttr.getClass());
        assertEquals(SOAPTextImpl.class, clonedDigestValue.getClass());
    }

    private Element getElement(Element parent, String name) {
        return (Element)parent.getElementsByTagName(name).item(0);
    }

    private static SOAPPart read(String file) throws Exception {
        try (InputStream in = DocumentOwnerTest.class.getResource(file).openStream()) {
            return MessageFactory.newInstance().createMessage(null, in).getSOAPPart();
        }
    }
}
