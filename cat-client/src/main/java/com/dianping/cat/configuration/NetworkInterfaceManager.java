package com.dianping.cat.configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

public enum NetworkInterfaceManager {
	INSTANCE;

	private InetAddress address;
	
	private AtomicBoolean m_initialized = new AtomicBoolean();
	
	private NetworkInterfaceManager() {
	}

	public String getLocalHostAddress() {
		initialize();
		return address.getHostAddress();
//		return Inets.IP4.getLocalHostAddress();
	}

	public String getLocalHostName() {
		initialize();
		return address.getHostName();
//		return Inets.IP4.getLocalHostName();
	}
	
	private synchronized void initialize() {
		if (!m_initialized.get()) {
			if (address == null) {
				try {
					address = Inet4Address.getLocalHost();
					m_initialized.set(true);
				} catch (UnknownHostException e) {
					String message = "No network configured!";
					throw new IllegalStateException(message);
				}
			}
		}
	}
}
