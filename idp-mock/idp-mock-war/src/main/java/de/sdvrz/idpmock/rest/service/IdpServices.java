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
package de.sdvrz.idpmock.rest.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sdvrz.idpmock.model.ClientModel;
import de.sdvrz.idpmock.model.DataModel;
import de.sdvrz.idpmock.token.model.TokenModel;
import de.sdvrz.idpmock.util.Helper;

/**
 * RestFul Services for IdP Mock
 *
 */
@Path("")
public class IdpServices {
	
	private static final Logger LOG = LoggerFactory.getLogger(IdpServices.class);	
	
	@Inject
	DataModel dataModel;
	
	/**
	 * RestFul Service to obtain an Authorization Code. First it will be redirect to login page. 
	 * see LoginController
	 * 
	 * @param scope Scope
	 * @param responseType Must be 'code'
	 * @param redirectUri Provided Url to be redirected to
	 * @param clientId Provided clientId
	 * @param bic Bank Identifier Code
	 * @param state Random value for checking approach
	 * @param request Request Context
	 * @return Response (redirect)
	 */
	@GET	
	@Path("/authorize/")
	public Response authorize(
			@QueryParam("scope") String scope,
			@QueryParam("response_type") String responseType,
			@QueryParam("redirect_uri") String redirectUri,
			@QueryParam("client_id") String clientId,
			@QueryParam("bic") String bic,
			@QueryParam("state") String state,
			@Context HttpServletRequest request) {
		
		// Parameter validation
		if (scope == null
				|| scope.length() <= 0) {
			scope = "default";
		}		
		
		if (clientId == null) {
			return redirectToErrorPage("Invalid+Request", "Missing+parameter:+client_id");
		}
		if (clientId.length() <= 0) {
			return redirectToErrorPage("Invalid+Request", "Parameter:+client_id+is+empty");
		}
		
		if (responseType == null) {
			return redirectToErrorPage("Invalid+Request", "Missing+parameter:+response_type");
		}
		if (responseType.length() <= 0) {
			return redirectToErrorPage("Invalid+Request", "Parameter:+response_type+is+empty");
		}
		if (!responseType.equals("code")) {
			// 'token' for Implicit Grant is not supported
			return redirectToErrorPage("Invalid+Request", "Parameter:+response_type+has+an+invalid+value");
		}
		
		if (redirectUri == null) {
			return redirectToErrorPage("Invalid+Request", "Missing+parameter:+redirect_uri");
		}
		if (redirectUri.length() <= 0) {
			return redirectToErrorPage("Invalid+Request", "Parameter:+redirect_uri+is+empty");
		}
		try
		{
			// Should be checked if redirect_uri is registered by IdP, but here only syntax check
			new URL(redirectUri);
		}
		catch (MalformedURLException e) {
			return redirectToErrorPage("Invalid+Request", "Parameter:+redirect_uri+does+not+match+with+the+provided+url");
		}	
		
		// No existing ClientModel must be initialized first 
		if (!dataModel.getScopeClientMap().containsKey(scope)) {
			dataModel.getScopeClientMap().put(scope, new HashMap<>());
		}
		if (!dataModel.getScopeClientMap().get(scope).containsKey(clientId)) {
			dataModel.getScopeClientMap().get(scope).put(clientId, new ClientModel());
		}	
		
		// Get ClientModel 
		ClientModel clientModel = dataModel.getScopeClientMap().get(scope).get(clientId);
		LOG.debug("authorize() clientIModel: {}", clientModel);
		
		// Save client data 
		clientModel.setScope(scope);
		clientModel.setClientId(clientId);
		clientModel.setRedirectUri(redirectUri);
		clientModel.setResponseType(responseType);
		clientModel.setBic(bic);
		clientModel.setState(state);
		
		URI location = null;
		try {
			// Redirect URL for login page
			location = new URI(String.format("/authorize/login.xhtml?scope=%s&client_id=%s", scope, clientId));
		}
		catch (URISyntaxException e) {
			return Response.status(Status.BAD_REQUEST).entity(String.format("authorize() %s: %s", e.getClass().getSimpleName(), e.getMessage())).build();
		}
		
		// Redirect to login page
		return Response.temporaryRedirect(location).build();		
	}
	
	/**
	 * RestFul Service to obtain an Access Token
	 * 
	 * @param grantType Type of request: 'authorization_code' or 'refresh_token'
	 * @param redirectUri Redirect uri - only for check, not for redirect anything
	 * @param code Authorization Code (only if grantType: 'authorization_code')
	 * @param refreshToken Refresh Token (only if grant_type: 'refresh_token')
	 * @param clientId provided clientId
	 * @param clientSecret Password for provided clientId
	 * @param authorization Header parameter with clientId and clientSecret 
	 * @param request Context request
	 * @return Model with Access Token or error / error description if Access Token can not be returned
	 */
	@POST	
	@Path("/token/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(
			@QueryParam("grant_type") String grantType,
			@QueryParam("redirect_uri") String redirectUri,
			@QueryParam("code") String code,	
			@QueryParam("refresh_token") String refreshToken,	
			@QueryParam("client_id") String clientId,
			@QueryParam("client_secret") String clientSecret,
			@HeaderParam("Authorization") String authorization,
			@Context HttpServletRequest request) {
		
		// Obtain header parameter: authorization for clientId and clientSecret
		if (authorization != null
				&& authorization.length() > 0) {
			if (authorization.startsWith("Basic")) {
				authorization = authorization.replaceAll("Basic\\s*(\\S*)$", "$1");
				String clientIdAndSecret = new String(Base64.getDecoder().decode(authorization.getBytes()));				
				String[] clientIdAndSecretArray = clientIdAndSecret.split(":");
				if (clientIdAndSecretArray.length == 2) {
					clientId = clientIdAndSecretArray[0];
					clientSecret = clientIdAndSecretArray[1];
					LOG.debug("token() From header parameter 'authorization' - clientId: {}, clientSecret: {}", clientId, clientSecret);
				}
			}			
		}	
		
		// check parameter: client_id
		if (clientId == null
				|| clientId.length() <= 0) {			
			
			returnError(Status.BAD_REQUEST, "invalid_request","Missing parameter: client_id");
		}
		
		// check parameter: grant_type
		if (grantType == null
				|| !grantType.matches("(authorization_code)|(refresh_token)")) {			
			// 'password' for Resource Owner Password Credentials Grant is not supported
			// 'client_credentials' for Client Credentials Grant is not supported
			// absolute URI for Extension Grants is not supported
			returnError(Status.BAD_REQUEST, "invalid_request","Invalid parameter: grant_type");
		}
		
		// check parameter: redirect_uri
		if (redirectUri == null
				|| redirectUri.length() <= 0) {			
			
			returnError(Status.BAD_REQUEST, "invalid_request","Invalid parameter: redirect_uri");
		}
		
		// Looking for ClientModel - must be existing!
		for(Map<String, ClientModel> clientMap : dataModel.getScopeClientMap().values()) {
			ClientModel clientModel = clientMap.get(clientId);
			if (clientModel != null) {
				LOG.debug("token() clientIModel: {}", clientModel);
				
				if (grantType.equals("authorization_code")) {
					if (code == null) {
						
						returnError(Status.BAD_REQUEST, "invalid_request","Missing parameter: code");
					}
					if (code.equals(clientModel.getAuthorizationCode())) {	
						if(redirectUri.equals(clientModel.getRedirectUri())) {
						
							return returnAccessToken(clientModel);
						}
						return returnError(Status.BAD_REQUEST, "invalid_request"," Incorrect: redirect_uri");
					}
					return returnError(Status.BAD_REQUEST, "invalid_request"," Incorrect: authorization code");
				}
				
				if (grantType.equals("refresh_token")) {
					if (refreshToken == null) {
						
						returnError(Status.BAD_REQUEST, "invalid_request","Missing parameter: refresh_token");
					}
					if (refreshToken.equals(clientModel.getRefreshCode())) {
						if(redirectUri.equals(clientModel.getRedirectUri())) {
						
							return returnAccessToken(clientModel);
						}
						return returnError(Status.BAD_REQUEST, "invalid_request"," Incorrect: redirect_uri");
					}
					return returnError(Status.BAD_REQUEST, "invalid_request"," Incorrect: refresh code");
				}
			}
		}
		
		// No ClientModel found		
		return returnError(Status.BAD_REQUEST, "invalid_client","'client_id' not provided or authorization code must be requested first");		
	}
	
	/**
	 * Only if authorization code requested - redirect to error page
	 * 
	 * @param oauthErrorCode Error code
	 * @param oauthErrorMsg Error description
	 * @return Response with Error
	 */
	private Response redirectToErrorPage(String oauthErrorCode, String oauthErrorMsg) {
		
		URI location = null;
		try {
			location = new URI(String.format("/authorize/error.xhtml?oauthErrorCode=%s&oauthErrorMsg=%s", oauthErrorCode, oauthErrorMsg));
			return Response.temporaryRedirect(location).build();
		}
		catch (URISyntaxException e) {
			return Response.status(Status.BAD_REQUEST).entity(String.format("redirectToErrorPage() %s: %s", e.getClass().getSimpleName(), e.getMessage())).build();
		}
	}
	
	/**
	 * Generate response model with Access Token 
	 * 
	 * @param clientModel Model with Access Token
	 * @return Response with Access Token
	 */
	private Response returnAccessToken(ClientModel clientModel) {
		
		clientModel.setAuthorizationCode("");
		clientModel.setRefreshCode(Helper.generateCode());
		clientModel.setTokenType("Bearer");
		clientModel.setExpiresIn(Helper.generateExpiresIn());
		clientModel.setScope(clientModel.getScope());
		clientModel.setAccessToken(Helper.generateAccessToken());
		
		// Response model
		TokenModel tokenModel = new TokenModel();
		tokenModel.setScope(clientModel.getScope());
		tokenModel.setToken_type(clientModel.getTokenType());
		tokenModel.setExpires_in(clientModel.getExpiresIn());
		tokenModel.setRefresh_token(clientModel.getRefreshCode());
		tokenModel.setAccess_token(clientModel.getAccessToken());
		
		return Response.ok(tokenModel).build();
	}
	
	/**
	 * Only if Access Token requested - response with error / error description
	 * 
	 * @param error Error code
	 * @param errorDescription Error description
	 * @return Response with error
	 */
	private Response returnError(Status status, String error, String errorDescription) {
		
		// Response model
		TokenModel tokenModel = new TokenModel();
		tokenModel.setError(error);
		tokenModel.setError_description(errorDescription);
		return Response.status(status).entity(tokenModel).build();		
	}	

}
