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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sdvrz.xs2a.api.model.ConfirmationOfFundsRequest;
import de.sdvrz.xs2a.api.model.ConfirmationOfFundsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * RESTful Services for Confirmation Of Funds
 *
 */
@Path("/{version}/confirmation-of-funds")
@Api(value = "/{version}/accounts}", tags = { "Confirmation"}, consumes = "application/json", produces = "application/json")
public interface Confirmation {

	/**
	 * For /v1/confirmation-of-funds requests
	 * 	 
	 * @param version Version
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @param confirmationOfFundsRequest Body data
	 * @return Response
	 */
	@POST
	@Path("/{provider-id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Confirmation of Funds (page 84) - creates a confirmation of funds request at the ASPSP")
	@ApiResponses(
		{@ApiResponse(code = 201, message = "OK, funds request was correctly performe", response = ConfirmationOfFundsResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid"),
			@ApiResponse(code = 406, message = "(Not Acceptable) ? - not documented but necessary hier") })
	Response confirmationOfFunds(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "Provider Identification") @PathParam("provider-id") String providerId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date,
			@ApiParam(value = "Request body", required = true)
					ConfirmationOfFundsRequest confirmationOfFundsRequest);

	
}
