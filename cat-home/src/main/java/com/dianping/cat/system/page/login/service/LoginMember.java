package com.dianping.cat.system.page.login.service;

public class LoginMember {

	private String m_userName;

	private String m_realName;

	private String m_role;
	
	public String getRealName() {
		return this.m_realName;
	}

	public String getUserName() {
		return this.m_userName;
	}

	public String getRole() {
		return this.m_role;
	}
	
	public void setRealName(String realName) {
		this.m_realName = realName;
	}

	public void setUserName(String userName) {
		this.m_userName = userName;
	}
	
	public void setRole(String role){
		this.m_role=role;
	}
}
