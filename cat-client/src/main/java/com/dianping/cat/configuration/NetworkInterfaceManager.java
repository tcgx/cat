package com.dianping.cat.configuration;

public enum NetworkInterfaceManager {
	INSTANCE;
	
	private NetworkInterfaceManager() {
	}

	public String getLocalHostAddress() {
		return InetsEx.IP4.getLocalHostAddress();
	}

	public String getLocalHostName() {
		return InetsEx.IP4.getLocalHostName();
	}
}
