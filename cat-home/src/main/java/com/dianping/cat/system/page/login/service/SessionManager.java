package com.dianping.cat.system.page.login.service;

import org.apache.commons.lang.StringUtils;

import com.dianping.cat.system.page.login.spi.ISessionManager;

public class SessionManager implements
		ISessionManager<Session, Token, Credential> {

	private String lanxin = "lanxin";
	private String guest = "guest";

	// private Set<String> accountSet=Sets.newHashSet(lanxin,guest);

	@Override
	public Token authenticate(Credential credential) {
		String account = credential.getAccount();
		String password = credential.getPassword();

		if (account != null && password != null) {
			// default no authenticate
			if (account.equals(lanxin) && password.equals(lanxin)) {
				return new Token("蓝信", account);
			}
			if (account.equals(guest) && password.equals(guest)) {
				return new Token("访客", account);
			}
		}
		return null;

	}

	@Override
	public Session validate(Token token) {
		LoginMember member = new LoginMember();

		member.setUserName(token.getUserName());
		member.setRealName(token.getRealName());

		return new Session(member);
	}

	@Override
	public Session validate(Token token, String signinModuleName) {
		LoginMember member = new LoginMember();
		if (StringUtils.isBlank(signinModuleName)) {
			return null;
		}
		if (signinModuleName.equals("reportModule")) {
			member.setRole(guest);
		} else if (signinModuleName.equals("systemModule")) {
			member.setRole(lanxin);
		}

		if (access(member, token)) {
			member.setUserName(token.getUserName());
			member.setRealName(token.getRealName());
			return new Session(member);
		}

		return null;
	}

	private boolean access(LoginMember member, Token token) {
		if (token.getUserName().equals(lanxin)) {
			return true;
		} else if (member.getRole().equals(guest)
				&& token.getUserName().equals(guest)) {
			return true;
		}
		return false;
	}
}
