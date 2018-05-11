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
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sdvrz.xs2a.api.model.PaymentInitiationRequest;
import de.sdvrz.xs2a.api.model.PaymentInitiationResponse;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataRequest;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * RESTful Services for payments
 *
 */
@Path("/{version}/payments")
@Api(value = "/{version}/payments}", tags = {"Payments"}, consumes = "application/json", produces = "application/json")
public interface Payments {

	/**
	 * For /v1/payments/{payment-product}
	 * 
	 * @param version Version
	 * @param paymentProduct sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers") @PathParam("payment-product
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param psuCorporateId Only used in a corporate context
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param consentId May be contained, if the payment initiation transaction is part of a combined AIS/PIS service
	 * @param psuAgent The forwarded Agent header field of the http request between PSU and TPP
	 * @param psuIpAddress The forwarded IP Address header field consists of the corresponding http request IP Address between PSU and TPP
	 * @param psuGeoLocation The forwarded Geo Location header field of the corresponding http request between PSU and TPP if available
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Date
	 * @param paymentInitiationRequest Body data
	 * @return Response
	 */
	@POST
	@Path("/{payment-product}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Payment Initiation Request (page 25) - creates a payment initiation request at the ASPSP")
	@ApiResponses(
		{@ApiResponse(code = 201, message = "OK, request received", response = PaymentInitiationResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response paymentInitiationRequest(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers") @PathParam("payment-product") String paymentProduct,
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
			@ApiParam(value = "May be contained, if the payment initiation transaction is part of a combined AIS/PIS service", required = false)
			@HeaderParam("Consent-ID") String consentId,
			@ApiParam(value = "The forwarded Agent header field of the http request between PSU and TPP", required = false)
			@HeaderParam("PSU-Agent") String psuAgent,
			@ApiParam(value = "The forwarded IP Address header field consists of the corresponding http request IP Address between PSU and TPP", required = true)
			@HeaderParam("PSU-IP-Address") String psuIpAddress,
			@ApiParam(value = "The forwarded Geo Location header field of the corresponding http request between PSU and TPP if available", required = false)
			@HeaderParam("PSU-Geo-Location") String psuGeoLocation,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date,
			@ApiParam(value = "Request body", required = true)
					PaymentInitiationRequest paymentInitiationRequest);
	
	/**
	 * Updates the payment initiation data by PSU data
	 * 
	 * @param version Version
	 * @param paymentProduct sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers
	 * @param paymentId Payment ID as received from the Payment Initiation
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param psuCorporateId Only in corporate context
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @param updatePSUDataRequest Request data
	 * @return UpdatePSUDataResponse
	 */
	@PUT
	@Path("/{payment-product}/{payment-id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update PSU data (page 70) - updates the payment initiation data by PSU data")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, request received", response = UpdatePSUDataResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response updatePSUData(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers")
			@PathParam("payment-product") String paymentProduct,
			@ApiParam(value = "Payment ID as received from the Payment Initiation")
			@PathParam("payment-id") String paymentId,
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
	 * Reads the details of the initiated payment
	 * 
	 * @param version Version
	 * @param paymentProduct sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers
	 * @param paymentId Payment ID as received from the Payment Initiation
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @return PaymentInitiationResponse
	 */
	@GET
	@Path("/{payment-product}/{payment-id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Read Payment Data (page ?) - reads the details of an initiated payment")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, request received", response = PaymentInitiationResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response readData(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers")
			@PathParam("payment-product") String paymentProduct,
			@ApiParam(value = "Payment ID as received from the Payment Initiation")
			@PathParam("payment-id") String paymentId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
			@ApiParam(value = "Access Token as obtained from Authorization Server", required = true)
			@HeaderParam("Authorization") String accessToken,
			@ApiParam(value = "A signature of the request by the TPP on application level", required = false)
			@HeaderParam("signature") String signature,
			@ApiParam(value = "The certificate used for signing the request", required = false)
			@HeaderParam("certificate") String certificate,
			@ApiParam(value = "Standard https header element for Date and Time", required = true)
			@HeaderParam("Date") String date);

	/**
	 * Reads the transaction status of the payment
	 * 
	 * @param version Version
	 * @param paymentProduct sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers
	 * @param paymentId Payment ID as received from the Payment Initiation
	 * @param processId ID of the transaction as determined by the initiating party (UUID)
	 * @param requestId ID of the request (UUID)
	 * @param psuId To be used if no OAuth Pre-Step was performed
	 * @param accessToken Access Token as obtained from Authorization Server
	 * @param signature A signature of the request by the TPP on application level
	 * @param certificate The certificate used for signing the request
	 * @param date Standard https header element for Date and Time
	 * @return StatusResponse
	 */
	@GET
	@Path("/{payment-product}/{payment-id}/status")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Status Request (page 33) - check the status of the payment initiation")
	@ApiResponses(
		{@ApiResponse(code = 200, message = "OK, request received", response = StatusResponse.class),
			@ApiResponse(code = 400, message = "(Bad Request) Validation error occured"),
			@ApiResponse(code = 401, message = "(Unauthorized) Access Token is not valid") })
	Response statusRequest(
			@ApiParam(value = "Version") @PathParam("version") String version,
			@ApiParam(value = "sepa-credit-transfers / instant-sepa-credit-transfers / target-2-payments / cross-border-credit-transfers")
			@PathParam("payment-product") String paymentProduct,
			@ApiParam(value = "Payment ID as received from the Payment Initiation")
			@PathParam("payment-id") String paymentId,
			@ApiParam(value = "ID of the transaction as determined by the initiating party (UUID)", required = true)
			@HeaderParam("Process-ID") String processId,
			@ApiParam(value = "ID of the request (UUID)", required = true)
			@HeaderParam("Request-ID") String requestId,
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
	
	

}
