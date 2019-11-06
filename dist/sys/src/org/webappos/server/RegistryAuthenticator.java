package org.webappos.server;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Authentication.User;
import org.eclipse.jetty.util.security.Constraint;
import org.webappos.auth.UsersManager;

/**
 * Implements the Authenticator interface to be used in HTTP auth.
 * Checks for user's credentials (login+password) from the webAppOS Registry.
 * Checks for standalone user (if webappos.properties contains standalone_token).
 * Forwards requests to BasicAuthenticator instance, thus, additional users can be authenticated as well, if
 * ConstraintSecurityHandler is configured correctly. Configuration example:
 * 
 * ConstraintSecurityHandler security = new ConstraintSecurityHandler();			
 * UserStore userStore = new UserStore();
 * userStore.addUser("login", Credential.getCredential("password"), new String[] {"user", "role2"});
 * 
 * HashLoginService l = new HashLoginService();
 * l.setUserStore(userStore);
 * l.setName("myrealm");
 * 
 * Constraint constraint = new Constraint(); 
 * constraint.setName(Constraint.__BASIC_AUTH);
 * constraint.setRoles(new String[]{"user"});
 *   // or: constraint.setRoles(new String[]{"**"});  // double stars for any role 
 * constraint.setAuthenticate(true);
 * 
 * ConstraintMapping cm = new ConstraintMapping();
 * cm.setConstraint(constraint);
 * cm.setPathSpec("/*");
 * security.addConstraintMapping(cm);
 * 
 * Authenticator a = new RegistryAuthenticator(); // instead of DigestAuthenticator() or BasicAuthenticator()
 * security.setAuthenticator(a);
 * security.setRealmName("myrealm");
 * security.setLoginService(l);
 * ...
 * someContextHandler.setSecurityHandler(security);
 * 
 * @author Sergejs Kozlovics
 *
 */
public class RegistryAuthenticator implements Authenticator {
	
	private Authenticator delegate = new BasicAuthenticator();

	@Override
	public String getAuthMethod() {
		return delegate.getAuthMethod();
	}

	@Override
	public void prepareRequest(ServletRequest arg0) {
		delegate.prepareRequest(arg0);
	}

	@Override
	public boolean secureResponse(ServletRequest arg0, ServletResponse arg1, boolean arg2, User arg3)
			throws ServerAuthException {
		return delegate.secureResponse(arg0, arg1, arg2, arg3);
	}

	@Override
	public void setConfiguration(AuthConfiguration arg0) {
		delegate.setConfiguration(arg0);
	}

	@Override
	public Authentication validateRequest(ServletRequest req, ServletResponse arg1, boolean arg2)
			throws ServerAuthException {
		
		try {
			String authorization = "";
			if (req instanceof HttpServletRequest) { 
				authorization = ((HttpServletRequest)req).getHeader("Authorization");
			}
			
	//		String lang = ((HttpServletRequest)req).getHeader("Accept-Language");
			
		    if (authorization != null && authorization.startsWith("Basic")) {
		        // "Basic base64credentials"
		        String base64Credentials = authorization.substring("Basic".length()).trim();
		        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
		                Charset.forName("UTF-8"));
		        // "username:password"
		        final String[] values = credentials.split(":",2);
	
		        if (UsersManager.passwordOK(values[0], values[1], true)) {	        	
		        	return UsersManager.getUserAuthentication(values[0]);
		        }
		    }
		}
		catch(Throwable t) {
			; // do nothing
		}

		return delegate.validateRequest(req, arg1, arg2);
	}

}
