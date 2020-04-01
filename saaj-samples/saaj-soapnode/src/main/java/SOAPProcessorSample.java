/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import com.sun.xml.soap.SOAPAnnotator;
import com.sun.xml.soap.SOAPProcessor;
import com.sun.xml.soap.SOAPProcessorFactory;
import com.sun.xml.soap.SOAPRecipient;

import javax.xml.namespace.QName;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

public class SOAPProcessorSample {

    public static void main(String[] args) throws Exception {
        process(Collections.emptyList());
    }

    public static SOAPMessage process(List<SOAPAnnotator> customAnnotators) throws Exception {

        // Create a sample SOAP msg
        String doc = 
            "<?xml version=\"1.0\" ?><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'><env:Header><abc:Extension1 xmlns:abc='http://example.org/2001/06/ext' env:actor='http://schemas.xmlsoap.org/soap/actor/next' env:mustUnderstand='1'/><def:Extension2 xmlns:def='http://example.com/stuff' env:mustUnderstand=\"1\"/></env:Header><env:Body/></env:Envelope>";
       
        byte[] testDocBytes = doc.getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(testDocBytes);
        StreamSource strSource = new StreamSource(bais);
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage sm = mf.createMessage();
        SOAPPart sp = sm.getSOAPPart();
        sp.setContent(strSource);
        sm.saveChanges();
        
        // Create a SOAPProcessor
        SOAPProcessorFactory factory = new SOAPProcessorFactory(); 
        final SOAPProcessor processor = factory.createSOAPProcessor();

        // Initialize recipient1
        SOAPRecipient recipient1 = new SampleRecipient();
        recipient1.addRole(
            "http://schemas.xmlsoap.org/soap/actor/next");
        recipient1.addRole(
            "http://schemas.xmlsoap.org/soap/actor/ultimateReceiver");
        QName name1 = new QName("http://example.org/2001/06/ext",
                                "Extension1",
                                "abc");
        QName name2 = new QName("http://example.com/stuff",
                                "Extension2",
                                "def");
        recipient1.addHeader(name1);
        recipient1.addHeader(name2);

        // Add recipient1 to SOAPProcessor
        processor.addRecipient(recipient1);

        // Initialize recipient2
        SOAPRecipient recipient2 = new SampleRecipient();
        recipient2.addRole(
            "http://schemas.xmlsoap.org/soap/actor/next");
        recipient2.addHeader(name1);
        recipient2.addHeader(name2);

        // Add recipient2 to SOAPProcessor
        processor.addRecipient(recipient2);

        // SOAPProcessor processing the msg
        processor.acceptMessage(sm);

        // Initialize annotator
        SOAPAnnotator annotator = new SampleAnnotator();

        // Annotate headers
        processor.addAnnotator(annotator);
        customAnnotators.forEach(processor::addAnnotator);

        // Prepare the msg before it can be sent out
        processor.prepareMessage(sm);

        // Output the final msg
        System.out.println("final message : ");
        sm.writeTo(System.out);
        System.out.println();
        System.out.println("final message ends here.");
        return sm;
    }
}
