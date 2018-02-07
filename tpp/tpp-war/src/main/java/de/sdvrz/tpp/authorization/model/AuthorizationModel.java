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
package de.sdvrz.tpp.authorization.model;

import java.io.Serializable;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model to interact with home.xhtml page
 * Contains all data filled on view: home.xhtml and data coming from Identity Provider (IdP) as query parameter in redirected request from it.
 *
 */
@Named("authorizationModel")
@SessionScoped
public class AuthorizationModel implements Serializable {
	
	private static final long serialVersionUID = 2692517327908022720L;

	private String authorizationCode;
	private AccessTokenResponseModel accessTokenResponseModel;	
	private String httpCode;
	private String httpMessage;
	private String error;
	private String errorDescription;
	private String errorAuth;
	private String errorDescriptionAuth;	
	
	@PostConstruct
	void init() {
		accessTokenResponseModel = new AccessTokenResponseModel();
	}
	
	public Optional<String> getAuthorizationCode() {
		return Optional.ofNullable(authorizationCode);
	}

	public void setAuthorizationCode(String authorizationCode) {			
		this.authorizationCode = authorizationCode;		
	}	

	public AccessTokenResponseModel getAccessTokenResponseModel() {		
		return accessTokenResponseModel;
	}

	public void setAccessTokenResponseModel(AccessTokenResponseModel accessTokenResponseModel) {		
		this.accessTokenResponseModel = accessTokenResponseModel;
	}	

	public String getHttpCode() {			
		return httpCode;
	}

	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}

	public String getHttpMessage() {
		return httpMessage;
	}

	public void setHttpMessage(String httpMessage) {
		this.httpMessage = httpMessage;
	}

	public String getError() {
		return error == null || error.length() <= 0 ? errorAuth : error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return errorDescription == null || errorDescription.length() <= 0 ? errorDescriptionAuth : errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorAuth() {
		return errorAuth;
	}

	public void setErrorAuth(String errorAuth) {
		this.errorAuth = errorAuth;
	}

	public String getErrorDescriptionAuth() {
		return errorDescriptionAuth;
	}

	public void setErrorDescriptionAuth(String errorDescriptionAuth) {
		this.errorDescriptionAuth = errorDescriptionAuth;
	}
	
	
}
