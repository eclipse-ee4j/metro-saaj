/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package mime.custom;

import jakarta.activation.DataContentHandler;
import jakarta.activation.DataContentHandlerFactory;

/**
 * The data content handler factory.
 *
 * @author Jitendra Kotamraju (jitendra.kotamraju@sun.com)
 */
public class CustomDataContentHandlerFactory
    implements DataContentHandlerFactory {
    
    /**
     * Data handler for "custom/factory" MIME type
     */
    @Override
    public DataContentHandler createDataContentHandler(String mimeType) {
		if (mimeType.equals("custom/factory")) {
			return new FactoryDataContentHandler();
		}
		return null;
	}
}
