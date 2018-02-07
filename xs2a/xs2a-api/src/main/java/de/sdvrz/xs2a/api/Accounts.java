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
package de.sdvrz.xs2a.api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sdvrz.xs2a.api.model.AccountBalanceResponse;
import de.sdvrz.xs2a.api.model.AccountResponse;
import de.sdvrz.xs2a.api.model.AccountTransactionsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * RESTful Services for accounts
 *
 */
@Path("/{version}/accounts")
@Api(value = "/{version}/accounts}", tags = { "Accounts"}, consumes = "application/json", produces = "application/json")
public interface Accounts {

	/**
	 * For /v1/accounts requests
	 * 
	 * @param version Version
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param consentId Shall be contained if Consent Trasaction was performed via this API before
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @return AccountResponse
	 */	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Read Account List (page 59) - reads all accounts belonging to PSU identified by OAuth2 Access Token")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, accounts returned", response = AccountResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response readAccountList(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "Shall be contained if Consent Trasaction was performed via this API before", required = false)
			@HeaderParam("Consent-ID") String consentId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken);

	/**
	 * For /{account-id}/balances requests
	 * 
	 * @param version Version
	 * @param accountId Account ID
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param consentId Consent-ID as from Trasaction performed via this API before
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @return AccountBalanceResponse
	 */
	@GET
	@Path("/{account-id}/balances")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Read Balance (page 61) - reads all balances according to specified account")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, balances returned", response = AccountBalanceResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response readBalance(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Account ID") @PathParam("account-id") String accountId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "Consent-ID as from Trasaction performed via this API before", required = true)
			@HeaderParam("Consent-ID") String consentId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date);
	
	/**
	 * For /{account-id}/transactions requests
	 * 
	 * @param version Version
	 * @param accountId Account ID
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param consentId Consent-ID as from Trasaction performed via this API before
	 * @param psuId To be used if no OAuth Pre-Step was performed"
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @param dateFrom Date from
	 * @param dateTo date to
	 * @return AccountTransactionsResponse
	 */
	@GET
	@Path("/{account-id}/transactions")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Read Transaction List (page 64) - reads all transactions according to specified account")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, transactions returned", response = AccountTransactionsResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response readTransactions(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Account ID") @PathParam("account-id") String accountId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "Consent-ID as from Trasaction performed via this API before", required = false)
			@HeaderParam("Consent-ID") String consentId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date,
			@ApiParam(value = "Date from", required = true)
			@QueryParam("date_from") String dateFrom,
			@ApiParam(value = "Date to", required = true)
			@QueryParam("date_to") String dateTo);


	/**
	 * For /{account-id}/transactions/{page} requests
	 * 
	 * @param version Version
	 * @param accountId Account ID
	 * @param page Page
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param consentId Consent-ID as from Trasaction performed via this API before
	 * @param psuId To be used if no OAuth Pre-Step was performed"
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @param dateFrom Date from
	 * @param dateTo date to
	 * @return AccountTransactionsResponse
	 */
	@GET
	@Path("/{account-id}/transactions/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Read Transaction List (page 64) - reads all transactions according to specified account with pagination")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, transactions returned", response = AccountTransactionsResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response readTransactionsPage(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Account ID") @PathParam("account-id") String accountId,
			@ApiParam(value = "-- / first /second / current / last") @PathParam("page") String page,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "Consent-ID as from Trasaction performed via this API before", required = false)
			@HeaderParam("Consent-ID") String consentId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date,
			@ApiParam(value = "Date from", required = true)
			@QueryParam("date_from") String dateFrom,
			@ApiParam(value = "Date to", required = true)
			@QueryParam("date_to") String dateTo);

}
