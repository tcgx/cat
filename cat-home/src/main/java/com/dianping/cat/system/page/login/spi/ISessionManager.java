package com.dianping.cat.system.page.login.spi;

import com.dianping.cat.system.page.login.service.Session;
import com.dianping.cat.system.page.login.service.Token;

public interface ISessionManager<S extends ISession, T extends IToken, C extends ICredential> {
	public T authenticate(C credential);

	public S validate(T token);

	Session validate(Token token, String signinModuleName);
}