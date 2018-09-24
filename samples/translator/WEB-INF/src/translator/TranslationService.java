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
 * $Id: TranslationService.java,v 1.5 2009-01-17 00:39:49 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:49 $
 */


package translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Properties;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * Translation Service that talks to Babelfish.altavista.com and gets back translations.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 *
 */

public class TranslationService {

    private String translation = "";
    private String proxyHost = "";
    private String proxyPort = "";

    public static final String ENGLISH_TO_GERMAN = "en_de";
    public static final String ENGLISH_TO_FRENCH = "en_fr";
    public static final String ENGLISH_TO_ITALIAN = "en_it";
    public static final String ENGLISH_TO_SPANISH = "en_es";

    public TranslationService() {

    }

    public TranslationService(String host, String port) {
        this.proxyHost = host;
        this.proxyPort = port;
        Properties props = System.getProperties();
        props.put("http.proxyHost", host);
        props.put("http.proxyPort", port);
    }

    public String translate(String translate_text, String toLanguage) {

        // Open a connection with the BabelFish URL.
        // Get the encoded String back.
        try {

            String text = URLEncoder.encode(translate_text);

            //HTTP Get.
            URL url = new URL("http://babelfish.altavista.com/babelfish/tr"
            +"?urltext=" + text
            + "&lp="+ toLanguage);

            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new
            InputStreamReader(con.getInputStream()));

            ParserDelegator parser = new ParserDelegator();

            HTMLEditorKit.ParserCallback callback =
            new HTMLEditorKit.ParserCallback() {

                // the translation will be in the div tag
                private boolean end_search = false;
                private boolean found_first_textarea = false;

                public void handleText(char[] data, int pos) {
                    if (found_first_textarea) {
                        translation = new String(data);
                    }
                }

                public void handleStartTag(HTML.Tag tag,
                MutableAttributeSet attrSet, int pos) {
                    if (tag == HTML.Tag.DIV  && end_search != true) {
                        found_first_textarea = true;
                    }
                }

                public void handleEndTag(HTML.Tag t, int pos) {
                    if (t == HTML.Tag.DIV && end_search != true) {
                        end_search = true;
                        found_first_textarea = false;
                    }
                }
            };

            parser.parse(in, callback , true);
            in.close();

        } catch (UnknownHostException uhe) {
            System.out.println("Exception: " + uhe.getMessage());
            System.out.println("If using proxy, please make sure " +
            "your proxy settings are correct." );
            System.out.println("Proxy Host = " + proxyHost +
            "\nProxy Port = " + proxyPort);
            uhe.printStackTrace();
        } catch (Exception me) {
            System.out.println("Exception: " + me.getMessage());
            me.printStackTrace();
        }
        return translation;
    }
}


