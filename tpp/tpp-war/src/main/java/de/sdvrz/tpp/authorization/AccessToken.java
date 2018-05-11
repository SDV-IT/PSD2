/*
 * Copyright 2018 SDV-IT, Sparda Datenverarbeitung eG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.sdvrz.tpp.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.sdvrz.tpp.PropertyManager;
import de.sdvrz.tpp.authorization.model.AccessTokenResponseErrorModel;
import de.sdvrz.tpp.authorization.model.AccessTokenResponseModel;
import de.sdvrz.tpp.authorization.model.AuthorizationModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to obtain Access Token from Authorization Server
 * First it will be checked if refresh token is present. If it is it will be used to get access token, 
 * otherwise authorization code must be get first. With authorization code an access token can be get.
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AccessToken {

	private static final Logger LOG = LoggerFactory.getLogger(AccessToken.class);

	@Inject
	AuthorizationModel model;

	@Inject
	PropertyManager propertyManager;

	@Inject
	ManagedScheduledExecutorService managedScheduledExecutorService;

	/**
	 * Access Token will be obtained
	 */
	public void getAccessToken() {
		resetErrorMessages();

		if ("".equals(model.getAccessTokenResponseModel().getRefresh_token())) {
			// getAuthorizationCode only if no Refresh Token
			getAuthorizationCode();
			// redirection for Authorization Code can take any time and
			// redirection is asynchronous
			try {
				managedScheduledExecutorService
						.schedule(this::waitForAuthorizationCode, 250, TimeUnit.MILLISECONDS)
						.get(1, TimeUnit.MINUTES);
			} catch (ExecutionException e) {
				LOG.error("Uncaught exception in scheduled execution", e.getCause());
			} catch (TimeoutException e) {
				model.setError("Timeout");
				model.setErrorDescription("60 sec. to authorize by identify provider exceeded");
				LOG.error("Timeout while waiting - Access Token can not be obtained");
				return;
			} catch (InterruptedException e) {
				LOG.info("Waiting for Access Token has been canceled");
			}

			if (!model.getAuthorizationCode().isPresent()) {
				// error from Authorization Server - no more actions to do
				LOG.error("getAccessToken() no Authorization Code received - Access Token can not be obtained");
				return;
			}
		}

		LOG.debug("Authorization Code: {}, Access Token Model: {}", model.getAuthorizationCode().orElse(""),
				model.getAccessTokenResponseModel());

		String url = buildUrlForAccessToken();

		// client_id:client_secret
		String clientIdAndSecret = propertyManager.getProperty("client.id") + ':' + propertyManager.getProperty("client.secret");
		// client_id:client_secret Base64 encoded
		String clientIdAndSecretBase64Encoded = Base64.getEncoder().encodeToString(clientIdAndSecret.getBytes());

		try {
			// RESTFul Request for Access Token with Authorization Code or
			// Refresh Token
			HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Authorization", "Basic " + clientIdAndSecretBase64Encoded);
			httpURLConnection.setRequestProperty("Accept", "application/json");
			httpURLConnection.setRequestMethod("POST");

			StringBuilder sb = new StringBuilder();
			int httpResponseCode = httpURLConnection.getResponseCode();
			if (httpResponseCode == HttpURLConnection.HTTP_OK) {
				// OK: Access Token, Refresh Token, Scope, Token Type, Expires
				// in received
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				LOG.debug("getAccessToken() HTTP ResponseCode: {}", httpResponseCode);
				LOG.debug("getAccessToken() content: {}", sb.toString());

				ObjectMapper mapper = new ObjectMapper();
				AccessTokenResponseModel accessTokenResponseModel = mapper.readValue(sb.toString(),
						AccessTokenResponseModel.class);
				model.setAccessTokenResponseModel(accessTokenResponseModel);
			} else {
				// Error from Authorization Server, get Error and Error
				// Description
				if (httpURLConnection.getErrorStream() != null) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(httpURLConnection.getErrorStream(), "UTF-8"));
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
				}
				ObjectMapper mapper = new ObjectMapper();
				AccessTokenResponseErrorModel accessTokenResponseErrorModel = mapper.readValue(sb.toString(),
						AccessTokenResponseErrorModel.class);
				model.setHttpCode(String.valueOf(httpResponseCode));
				model.setHttpMessage(httpURLConnection.getResponseMessage());
				model.setError(accessTokenResponseErrorModel.getError());
				model.setErrorDescription(accessTokenResponseErrorModel.getError_description());
				// Access Token, if any, is no longer valid - next step should
				// be get Authorization Code
				model.setAccessTokenResponseModel(new AccessTokenResponseModel());
				LOG.debug("getAccessToken() HTTP ResponseCode: {}/{}, Message: {}", httpResponseCode,
						httpURLConnection.getResponseMessage(), sb.toString());
			}

		} catch (MalformedURLException e) {
			LOG.error("Error", e);
		} catch (IOException e) {
			model.setError("IOException");
			model.setErrorDescription(e.getMessage());
			// Access Token, if any, is no longer valid - next step should be
			// get Authorization Code
			model.setAccessTokenResponseModel(new AccessTokenResponseModel());
			LOG.error("IOException: {}", e);
		}
	}

	/**
	 * Reset all displayed Errors
	 */
	private void resetErrorMessages() {
		model.setHttpCode("");
		model.setHttpMessage("");
		model.setError("");
		model.setErrorDescription("");
		model.setErrorAuth("");
		model.setErrorDescriptionAuth("");
	}

	/**
	 * Authorization Code will be get
	 */
	private void getAuthorizationCode() {
		// old Authorization Code, if any, must be discarded
		model.setAuthorizationCode(null);

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String url = buildUrlForAuthorizationCode();

		try {
			// Authorization Server needs to be redirected to
			// - user should perform his action (authentication) direct on
			// Authorization Server
			ec.redirect(url);
		} catch (Exception e) {
			LOG.error("Error during redirect to '{}'", url, e);
		}
	}

	/**
	 * Build URL for get Authorization Code request
	 * 
	 * @return URL for get Authorization Code request
	 */
	private String buildUrlForAuthorizationCode() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String url = new StringBuilder().append(propertyManager.getProperty("authorization.server.schema"))
				.append("://").append(propertyManager.getProperty("authorization.server.address")).append(':')
				.append(propertyManager.getProperty("authorization.server.port"))
				.append(propertyManager.getProperty("authorization.server.authorization.code.path")).append('?')
				.append("scope=").append("default").append('&').append("response_type=").append("code").append('&')
				.append("redirect_uri=").append(ec.getRequestScheme()).append("://").append(ec.getRequestServerName())
				.append(':').append(ec.getRequestServerPort()).append(ec.getRequestContextPath()).append('&')
				.append("client_id=").append(propertyManager.getProperty("client.id")).append('&')
				.append("bic=").append(propertyManager.getProperty("authorization.server.bic")).append('&')
				.append("state=").append(new Random().nextInt()).toString();
		return url;
	}

	/**
	 * Wait until Authorization Code is get or Error is received
	 * Managed by ManagedScheduledExecutorService
	 */
	private boolean waitForAuthorizationCode() {
		while(true) {
			if (model.getAuthorizationCode().isPresent()) {
				return true;
			}
			if (model.getError() != null && model.getError().length() > 0) {
				return false;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Build URL for get Access Token request
	 * 
	 * @return URL for get Access Token request
	 */
	private String buildUrlForAccessToken() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String url = propertyManager.getProperty("authorization.server.schema")
				+ "://" + propertyManager.getProperty("authorization.server.address") + ':'
				+ propertyManager.getProperty("authorization.server.port")
				+ propertyManager.getProperty("authorization.server.authorization.token.path") + '?'
				+ '&' + "grant_type="
				+ ("".equals(model.getAccessTokenResponseModel().getRefresh_token()) ? "authorization_code"
				: "refresh_token")
				+ '&' + "redirect_uri=" + ec.getRequestScheme() + "://"
				+ ec.getRequestServerName() + ':' + ec.getRequestServerPort()
				+ ec.getRequestContextPath() + '&'
				+ ("".equals(model.getAccessTokenResponseModel().getRefresh_token()) ? "code=" : "refresh_token=")
				+ ("".equals(model.getAccessTokenResponseModel().getRefresh_token()) ? model.getAuthorizationCode().orElse("")
				: model.getAccessTokenResponseModel().getRefresh_token());
		return url;
	}

}
