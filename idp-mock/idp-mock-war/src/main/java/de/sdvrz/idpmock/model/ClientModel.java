/**
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
package de.sdvrz.idpmock.model;

/**
 * Model for data belonging to one client-id
 * Contains all data used to perform requests started by client (identified with client-id)
 *
 */
public class ClientModel {	
	
	private String scope;
	private String clientId;	
	private String responseType;
	private String redirectUri;
	private String bic;
	private String state;
	private String authorizationCode;
	private String refreshCode;
	private String accessToken;
	private String tokenType;
	private String expiresIn;
	
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}	
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getRefreshCode() {
		return refreshCode;
	}
	public void setRefreshCode(String refreshCode) {
		this.refreshCode = refreshCode;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	@Override
	public String toString() {
		
		return new StringBuilder().append("scope: ").append(scope).append("\n")
				.append("clientId: ").append(clientId).append("\n")
				.append("responseType: ").append(responseType).append("\n")
				.append("redirectUri: ").append(redirectUri).append("\n")
				.append("bic: ").append(bic).append("\n")
				.append("state: ").append(state).append("\n")
				.append("authorizationCode: ").append(authorizationCode).append("\n")
				.append("refreshCode: ").append(refreshCode).append("\n")
				.append("accessToken: ").append(accessToken).append("\n")
				.append("tokenType: ").append(tokenType).append("\n")
				.append("expiresIn: ").append(expiresIn).append("\n").toString();
	}	
	
	

}
