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
package de.sdvrz.idpmock.authorize;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sdvrz.idpmock.authorize.model.LoginModel;
import de.sdvrz.idpmock.model.ClientModel;
import de.sdvrz.idpmock.model.DataModel;
import de.sdvrz.idpmock.util.Helper;

/**
 * Controller to interact with login.xhtml
 * Performs all actions started on the view: login.xhtml
 *
 */
@Named("controller")
@RequestScoped
public class LoginController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);	
	
	@Inject
	LoginModel loginModel;
	
	@Inject
	DataModel dataModel;
	
	/**
	 * Action if PSU logged in. If success - authorization code will be redirected to TPP
	 */
	public void login() {
		if (loginModel.getScope() == null
				|| loginModel.getClientId() == null
				|| dataModel.getScopeClientMap().get(loginModel.getScope()) == null
				|| dataModel.getScopeClientMap().get(loginModel.getScope()).get(loginModel.getClientId()) == null) {
			// ClientModel not found
			redirectToErrorPage("Access denied", "scope+or+client_id+not+found+or+client_id+is+not+provided");
			return;
		}
		
		ClientModel clientModel = dataModel.getScopeClientMap().get(loginModel.getScope()).get(loginModel.getClientId());	
		LOG.debug("login() clientModel: {}", clientModel);	
		
		if (loginModel.getUser() == null
				|| !loginModel.getUser().equals("MyName")
				|| loginModel.getPwd() == null
				|| !loginModel.getPwd().equals("MyPassword")) {
			// username or password incorrect
			redirectWithError(clientModel, "access_denied", "username or password incorrect");
			return;
		}
		
		// redirect to TPP with authorization code		
		redirectWithCode(clientModel); 
	}
	
	/**
	 * PSU canceled - redirect to PSU with error
	 */
	public void cancel() {
		
		if (loginModel.getScope() == null
				|| loginModel.getClientId() == null
				|| dataModel.getScopeClientMap().get(loginModel.getScope()) == null
				|| dataModel.getScopeClientMap().get(loginModel.getScope()).get(loginModel.getClientId()) == null) {
			// ClientModel not found
			redirectToErrorPage("access_denied", "scope or client_id not found or client_id is not provided");
		}
		
		ClientModel clientModel = dataModel.getScopeClientMap().get(loginModel.getScope()).get(loginModel.getClientId());
		
		redirectWithError(clientModel, "Access denied", "user cancelled");		
	}
	
	/**
	 * Redirect with error
	 * 
	 * @param clientModel ClientModel
	 * @param error Error code
	 * @param errorDescription Error description
	 */
	private void redirectWithError(ClientModel clientModel, String error, String errorDescription) {
		
		StringBuilder url = new StringBuilder().append(clientModel.getRedirectUri())
				.append('?').append("error=").append(error);
		if (errorDescription != null) {
			url.append('&').append("error_description=").append(errorDescription);
		}
		if (clientModel.getState() != null
				&& clientModel.getState().length() > 0) {
			url.append("&state=").append(clientModel.getState());
		}
		LOG.debug("redirectWithError() Redirect URL: {}", url);
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {			
			ec.redirect(url.toString());
		} catch (Exception e) {
			LOG.error("Error during redirect to '{}'", url, e);
		}		
	}
	
	/**
	 * Redirect to error page
	 * 
	 * @param error Error code
	 * @param errorDescription Error description
	 */
	private void redirectToErrorPage(String error, String errorDescription) {
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		
		StringBuilder url = new StringBuilder().append(ec.getRequestScheme())
															.append("://")
															.append(ec.getRequestServerName())
															.append(':')
															.append(ec.getRequestServerPort())
															.append(ec.getRequestContextPath())
															.append(ec.getRequestServletPath().replaceAll("login\\.xhtml", "error.xhtml"))
															.append('?')
															.append("oauthErrorCode=")
															.append(error);		
		if (errorDescription != null) {
			url.append("&oauthErrorMsg=").append(errorDescription);
		}	
		LOG.debug("redirectToErrorPage() Redirect URL: {}", url);
		
		try {			
			ec.redirect(url.toString());
		} catch (Exception e) {
			LOG.error("Error during redirect to '{}'", url, e);
		}		
	}
	
	/**
	 * Redirect with authorization code
	 * 
	 * @param clientModel ClientModel
	 */
	private void redirectWithCode(ClientModel clientModel) {
		
		clientModel.setAuthorizationCode(Helper.generateCode());
		
		StringBuilder url = new StringBuilder().append(clientModel.getRedirectUri())
				.append('?').append("code=").append(clientModel.getAuthorizationCode());	
		if (clientModel.getState() != null
				&& clientModel.getState().length() > 0) {
			url.append("&state=").append(clientModel.getState());
		}
		LOG.debug("redirectWithCode() Redirect URL: {}", url);
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		
		try {			
			ec.redirect(url.toString());
		} catch (Exception e) {
			LOG.error("Error during redirect to '{}'", url, e);
		}		
	}

}
