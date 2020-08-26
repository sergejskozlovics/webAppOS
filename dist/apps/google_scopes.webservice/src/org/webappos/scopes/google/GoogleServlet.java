package org.webappos.scopes.google;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.properties.WebServiceProperties;
import org.webappos.server.API;
import org.webappos.util.UTCDate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;


@SuppressWarnings( "serial" )
public class GoogleServlet extends HttpServlet
{
	private static final Logger logger =  LoggerFactory.getLogger(GoogleServlet.class);
	private static final Charset utf8_charset = StandardCharsets.UTF_8;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			String path = request.getPathInfo();
			if (path == null)
				path = "";

			if (request.getCharacterEncoding()!=null) {
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding())) {
					throw new RuntimeException("Bad/unsupported character encoding");
				}
			}
			if (!"application/x-www-form-urlencoded".equals(request.getContentType())) {
				throw new RuntimeException("Bad enctype");
			}

			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());

			String[] arr = formData.split("&");

			String idTokenString = arr[0].substring(arr[0].indexOf("id_token=") + 9);
			String accessTokenString = arr[1].substring(arr[1].indexOf("access_token=") + 13);


			// Google magical verification
			WebServiceProperties properties = API.propertiesManager
					.getWebServicePropertiesByFullName("google_scopes.webservice");
			String client_id = properties.properties
					.getProperty("oauth_client_id", null);

			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(getDefaultHttpTransport(), getDefaultJsonFactory())
					.setAudience(Collections.singletonList(client_id))
					.build();

			GoogleIdToken idToken = verifier.verify(idTokenString);
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();

				// Get profile information from payload
				String email = payload.getEmail();
				boolean emailVerified = payload.getEmailVerified();
				String expirationTime = UTCDate.stringify(new Date(payload.getExpirationTimeSeconds() * 1000));

				String wsToken;
				String userExpirationTime; // expiration time in UserData


				if (!emailVerified) {
					throw new RuntimeException("id token incorrect");
				}

				String login = UsersManager.getUserLogin(email);

				if (login != null) {
					ValidityChecker.checkLogin(email, true);
					JsonElement userData = API.registry.getValue("xusers/"+login);


					if(userData == null) {
						// Register user
						WebServiceProperties p = API.propertiesManager.getWebServicePropertiesByFullName("Login.webservice");
						p.properties.setProperty("signup_policy", "email");

						// Print to console Response status (OK; FAILED; PROCESSING)
						UsersManager.addUser(email, true);

						// Write to registry google drive token

						userData = API.registry.getValue("xusers/"+login);

						if (userData != null){
							// Set wsToken and write to registry wsToken and expiration time
							writeTokensToRegistry(login, expirationTime);
						}
					} else {

						// User already is registered
						wsToken = getWsToken(userData, login, expirationTime);

						userExpirationTime = getExpirationDate(userData, wsToken);

						if (UTCDate.expired(userExpirationTime)){
							// Token is expired
							userData.getAsJsonObject()
									.getAsJsonObject("tokens")
									.getAsJsonObject("ws")
									.remove(wsToken);

							API.registry.setValue("xusers/"+login, userData);
							writeTokensToRegistry(login, expirationTime);

						} else {
							// Token isn't expired, update it
							userData.getAsJsonObject()
									.getAsJsonObject("tokens")
									.getAsJsonObject("ws")
									.addProperty(wsToken, expirationTime);

							API.registry.setValue("xusers/"+login, userData);

						}
					}
					API.registry.setValue("xusers/"+login+"/google_scopes/google_drive_token", accessTokenString);


					userData = API.registry.getValue("xusers/"+login);

					String redirect = request.getQueryString(); // not encoded

					if ((redirect == null) || (redirect.isEmpty())) {
						// redirecting to desktop app
						redirect = "Desktop"; // default desktop app
						JsonElement element = API.registry.getValue("users/"+login+"/desktop_app");
						if ((element!=null) && (!element.toString().isEmpty())) {
							redirect = element.toString();
						}

						redirect = API.config.domain_or_ip+"/apps/"+redirect.toLowerCase();
					}

					// deleting previous ws_token=value (if any) from the redirect string
					int i = redirect.indexOf("?ws_token=");
					if (i<0)
						i = redirect.indexOf("&ws_token=");
					if (i>=0) {
						int j = redirect.indexOf("&", i+1);
						if (j<0)
							j=redirect.length()-1;

						// we will have to delete a substring from i+1 to j inclusive
						redirect = redirect.substring(0, i+1) + redirect.substring(j+1);
					}
					logger.trace("redirect was "+redirect);

					wsToken = getWsToken(userData, login, expirationTime);

					// attaching ws_token
					if (redirect.indexOf('?')<0)
						redirect += "?ws_token="+wsToken;
					else {
						if (redirect.endsWith("&"))
							redirect += "ws_token="+wsToken;
						else
							redirect += "&ws_token="+wsToken;
					}

					redirect += "&login="+login;
					logger.trace("redirect now "+redirect);


					if (!redirect.startsWith("http://") && !redirect.startsWith("https://")) {
						if (request.isSecure()) {
							redirect = "https://"+redirect;
						}
						else {
							redirect = "http://"+redirect;
						}
					}
					userExpirationTime = getExpirationDate(userData, wsToken);
					response.getOutputStream().print("{\"login\":\""+login+"\",\"ws_token\":\""+wsToken+"\",\"expires\":\""+userExpirationTime+"\",\"redirect\":\""+redirect+"\"}");

				} else {
					throw new RuntimeException("Login or email incorrect");
				}

			} else {
				throw new RuntimeException("Invalid ID token.");
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
			response.getOutputStream().print("{\"error\":\""+t.getMessage().replace('\"', ' ')+"\"}");
		}
	}

	/**
	 * Returns only WS Token, without expiration time, from registry.
	 * If registry contains more than one WS Token, it returns the first one
	 * @param userData Json element which contains user data from registry
	 * @return WS Token from registry
	 */
	private static String getWsToken (JsonElement userData, String login, String expirationTime){
		String ws = userData.getAsJsonObject()
				.getAsJsonObject("tokens")
				.getAsJsonObject("ws")
				.toString();
		if (ws.equals("{}")){
			writeTokensToRegistry(login, expirationTime);
			return getWsToken(API.registry.getValue("xusers/"+login), login, expirationTime);
		}
		return ws.substring(2, ws.indexOf(":") - 1);
	}

	/**
	 * Returns expiration date of WS Token from registry
	 * @param userData Json element which contains user data from registry
	 * @param wsToken String which contains WS Token
	 * @return Expiration date in Unix Time format
	 */
	private static String getExpirationDate (JsonElement userData, String wsToken){
		try {
			return userData.getAsJsonObject()
				.getAsJsonObject("tokens")
				.getAsJsonObject("ws")
				.get(wsToken)
				.getAsString();
		}
		catch(Throwable t) {
			return null;
		}
	}

	private static void writeTokensToRegistry(String login, String expirationDate){
		UsersManager.generateToken(login, false, expirationDate);
	}

	private static JsonFactory getDefaultJsonFactory() {
		return JacksonFactory.getDefaultInstance();
	}
	private static HttpTransport getDefaultHttpTransport() {
		return new NetHttpTransport();
	}
	public static Credential createCredentialWithAccessTokenOnly(String token) {
		return new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(token);
	}
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, String login) throws IOException {
		// Load client secrets.
		WebServiceProperties properties = API.propertiesManager
				.getWebServicePropertiesByFullName("google_scopes.webservice");
		String client_id = properties.properties
				.getProperty("drive_client_id", null);
		String client_secret = properties.properties
				.getProperty("drive_client_secret", null);
		JsonElement el = API.registry.getValue("xusers/"+login+"/google_scopes/google_drive_token");
		if (el==null)
			return null;
		String token = el.getAsString();

		GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();

		web.setClientId(client_id);
		web.setClientSecret(client_secret);

		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();

		clientSecrets.setWeb(web);

		return createCredentialWithAccessTokenOnly(token);
	}
		@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String path = request.getPathInfo();
		if (path == null)
			path = "";

		if (path.startsWith("/"))
			path = path.substring(1);
		if (path.endsWith("/"))
			path = path.substring(0, path.length()-1);

		if (path.equals("oauth_client_id")) {
			response.setContentType("text/html");

			WebServiceProperties properties = API.propertiesManager
					.getWebServicePropertiesByFullName("google_scopes.webservice");
			String client_id = properties.properties
					.getProperty("oauth_client_id", null);

			response.getOutputStream().print(client_id);
		}

	}

}