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
 * $Id: SOAPListener.java,v 1.5 2009-01-17 00:39:46 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:46 $
 */



import java.io.IOException;
import java.nio.channels.ClosedChannelException;

public interface SOAPListener {
	public void initMsgLoop(int port, SOAPCallback callback) throws IOException;
	public void startMsgLoop() throws IOException, ClosedChannelException,
		InterruptedException;
	public void startMsgLoopInThread();
	public void stopMsgLoopInThread();
}
