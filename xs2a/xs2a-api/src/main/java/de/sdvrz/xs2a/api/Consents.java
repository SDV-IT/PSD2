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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sdvrz.xs2a.api.model.ConsentRequestResponse;
import de.sdvrz.xs2a.api.model.InformationConsentRequest;
import de.sdvrz.xs2a.api.model.InformationConsentResponse;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataRequest;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * RESTful Services for consent
 *
 */
@Path("/{version}/consents")
@Api(value = "/{version}/consents}", tags = {"Consents"}, consumes = "application/json", produces = "application/json")
public interface Consents {

	/**
	 * For /v1/consents request
	 * 
	 * @param version Version
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param psuCorporateId Only used in a corporate context
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @param informationConsentRequest Request data
	 * @return InformationConsentResponse Response model
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Account Information Consent Request (page 48) - creates an account information consent resource at the ASPSP regarding access to specified accounts")
	@ApiResponses(
		{@ApiResponse(code = 201, message = "OK, request received", response = InformationConsentResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response informationConsent(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Only used in a corporate context", required = false)
			@HeaderParam("PSU-Corporate-ID") String psuCorporateId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date,
			@ApiParam(value = "Request body", required = true)
					InformationConsentRequest informationConsentRequest);
	
	/**
	 * Updates data on the consent resource
	 * 
	 * @param version Version
	 * @param consentId Consent ID as received from account information consent
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param psuCorporateId Only in corporate context
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @param updatePSUDataRequest Request data
	 * @return UpdatePSUDataResponse Response model
	 */
	@PUT
	@Path("/{consent-id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update PSU Data (page 70) - updates the account information consent data by PSU data")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, request received", response = UpdatePSUDataResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response updatePSUData(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Consent ID as received from account information consent")
			@PathParam("consent-id") String consentId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Only in corporate context", required = false)
			@HeaderParam("PSU-Corporate-ID") String psuCorporateId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date,
			@ApiParam(value = "Request body", required = true)
					UpdatePSUDataRequest updatePSUDataRequest);

	/**
	 * Reads the exact definition of the given consent resource
	 * 
	 * @param version Version
	 * @param consentId Consent ID as received from account information consent
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param psuCorporateId Only used in a corporate context
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @return ConsentRequestResponse
	 */
	@GET
	@Path("/{consent-id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Consent Request (page 56) - returns the content of of an account information consent object")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, request received", response = ConsentRequestResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response consentRequest(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Consent ID as received from account information consent") @PathParam("consent-id") String consentId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Only used in a corporate context", required = false)
			@HeaderParam("PSU-Corporate-ID") String psuCorporateId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date);
	
	/**
	 * @param version Version
	 * @param consentId Consent ID as received from account information consent
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @return Response - no body
	 */
	@DELETE
	@Path("/{consent-id}")
	@ApiOperation(value = "Delete an Account Information Consent Object (page 58) - deletes the consent object")
	@ApiResponses(
		{@ApiResponse(code = 204, message = "OK, request received"),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response deleteConsent(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Consent ID as received from account information consent") @PathParam("consent-id") String consentId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken);
	
	/**
	 * @param version Version
	 * @param consentId Consent ID as received from account information consent
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param psuCorporateId Only used in a corporate context
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @return Response - no body
	 */
	@GET
	@Path("/{consent-id}/status")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Status Request (page 55) - reads the transaction status of the addressed consent resource")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, request received", response = StatusResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response statusRequest(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Consent ID as received from account information consent") @PathParam("consent-id") String consentId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "To be used if no OAuth Pre-Step was performed", required = false)
			@HeaderParam("PSU-ID") String psuId,
			@ApiParam(value = "Only used in a corporate context", required = false)
			@HeaderParam("PSU-Corporate-ID") String psuCorporateId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date);

}
