/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.mz.videorec.net;

import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.message.Message;

/**
 * KeepAliveSip thread, for keeping the connection up toward a target SIP node
 * (e.g. toward the serving proxy/gw or a remote UA). It periodically sends
 * keep-alive tokens in order to refresh TCP connection timeouts and/or NAT
 * TCP/UDP session timeouts.
 */
public class KeepAliveSip extends KeepAliveUdp {
	/** SipProvider */
	SipProvider sip_provider;

	/** Sip message */
	Message message = null;

	/** Creates a new SIP KeepAliveSip daemon */
	public KeepAliveSip(SipProvider sip_provider,
			long delta_time) {
		super(null, delta_time);
		init(sip_provider, null);
		start();
	}

	/** Creates a new SIP KeepAliveSip daemon */
	public KeepAliveSip(SipProvider sip_provider, 
			Message message, long delta_time) {
		super(null, delta_time);
		init(sip_provider, message);
		start();
	}

	/** Inits the KeepAliveSip in SIP mode */
	private void init(SipProvider sip_provider, Message message) {
		this.sip_provider = sip_provider;
		if (message == null) {
			message = new Message("\r\n");
		}
		this.message = message;
	}

	/** Sends the kepp-alive packet now. */
	public void sendToken() throws java.io.IOException { // do send?
		if (!stop && sip_provider != null) {
			sip_provider.sendMessage(message);
		}
	}

	/** Main thread. */
	public void run() {
		super.run();
		sip_provider = null;
	}

	/** Gets a String representation of the Object */
	public String toString() {
		String str = null;
		if (sip_provider != null) {
			str = "sip:" + sip_provider.getViaAddress() + ":"
					+ sip_provider.getPort();
		}
		return str + " (" + delta_time + "ms)";
	}

}