/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package bugfixes;

import java.net.URL;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import junit.framework.TestCase;


/*
 * CR :7013971
 */ 
public class SAAJConnectionTest extends TestCase {
    private static util.TestHelper th = util.TestHelper.getInstance();

    public SAAJConnectionTest(String name) {
        super(name);
    }

    public void testSAAJ65() throws Exception {
        
        for (int i = 0; i < 2; i++) {
            try {
                //Runtime.getRuntime().exec("ulimit -a");
                //TODO, need to add an assert.
                SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
                SOAPConnection con = scf.createConnection();
                SOAPMessage reply = MessageFactory.newInstance().createMessage();
                reply.writeTo(System.out);
                System.out.println("\n");
                reply = con.call(reply, new URL("http://www.oracle.com"));
            } catch (Exception ex) {
            }
        }
        
    }
    
    public void testBug7013971() throws Exception {
         try {
         SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
         SOAPConnection con = scf.createConnection();

         SOAPMessage reply = MessageFactory.newInstance().createMessage();
         reply.writeTo(System.out);
         System.out.println("\n");
         Thread.sleep(1000);
         reply = con.call(reply, new URL("http://www.oracle.com"));
         assertTrue(true);
        } catch (java.security.AccessControlException e) {
            assertTrue(false);
        }catch(SOAPException ex) {
            assertTrue(true);
        } 
    }

    public void testBug12308187() throws Exception {
         try {
         SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
         SOAPConnection con = scf.createConnection();
         SOAPMessage reply = con.get(new URL("http://www.oracle.com"));
         reply.writeTo(System.out);
         assertTrue(true);
        } catch (java.security.AccessControlException e) {
            assertTrue(false);
        }catch(SOAPException ex) {
            assertTrue(true);
        } 
    }
    
    public void testSAAJ56() throws Exception {
		try {
			SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
			SOAPConnection con = scf.createConnection();
			SOAPMessage msg = MessageFactory.newInstance().createMessage();
			con.call(msg, new URL("http://username:pass%25word@www.oracle.com"));
			// IPv6 - note this is the address from the bug, not www.oracle.com
			//con.call(msg, new URL("http://username:pass%25word@[fe80:0:0:0:2e0:81ff:fe33:1874]:9992"));
			assertTrue(true);
		} catch (java.security.AccessControlException e) {
			assertTrue(false);
		} catch (SOAPException ex) {
			assertTrue(true);
		}
   }


    public static void main(String argv[]) {
        junit.textui.TestRunner.run(SAAJConnectionTest.class);        
    }

}
