/*
 * Copyright 2018 SDV-IT, Sparda Datenverarbeitung eG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.sdvrz.xs2a.impl;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sdvrz.xs2a.api.Payments;
import de.sdvrz.xs2a.api.model.AuthenticationMethod;
import de.sdvrz.xs2a.api.model.Links;
import de.sdvrz.xs2a.api.model.PaymentInitiationRequest;
import de.sdvrz.xs2a.api.model.PaymentInitiationResponse;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataRequest;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;
import de.sdvrz.xs2a.util.Util;

/**
 * Implement class for Payments Interface
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class PaymentsImpl implements Payments {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentsImpl.class);

  List<AuthenticationMethod> authenticationMethodList;
  String authenticationMethodId;
  String userId;
  String userPassword;
  String authenticationData;

  @Inject
  Util util;

  @PostConstruct
  void init() {
    authenticationMethodList = new LinkedList<>();
    AuthenticationMethod authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setAuthentication_type("SMS_OTP");
    authenticationMethod.setAuthentication_method_id("myAuthenticationId_1");
    authenticationMethodList.add(authenticationMethod);
    authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setAuthentication_type("CHIP_OTP");
    authenticationMethod.setAuthentication_method_id("myAuthenticationId_2");
    authenticationMethodList.add(authenticationMethod);
    authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setAuthentication_type("PHOTO_OTP");
    authenticationMethod.setAuthentication_method_id("myAuthenticationId_3");
    authenticationMethodList.add(authenticationMethod);
    authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setAuthentication_type("PUSH_OTP");
    authenticationMethod.setAuthentication_method_id("myAuthenticationId_4");
    authenticationMethodList.add(authenticationMethod);
  }

  @Override
  public Response paymentInitiationRequest(String version, String paymentProduct, String processId, String requestId, String psuId,
      String psuCorporateId, String accessToken, String consentId, String psuAgent, String psuIpAddress, String psuGeoLocation,
      String signature, String certificate, String date, PaymentInitiationRequest paymentInitiationRequest) {

    if (!util.checkValidation()) {
      // HTTP Code: 400
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (!util.checkAuthorization()) {
      // HTTP Code: 401
      return Response.status(Status.UNAUTHORIZED).build();
    }

    // Delete all user data
    authenticationMethodId = null;
    userId = util.checkAccessToken(accessToken) ? "*" : null;  // if Access Token userId is not needed
    userPassword = util.checkAccessToken(accessToken) ? "*" : null; // if Access Token userPassword is not needed
    authenticationData = null;

    PaymentInitiationResponse paymentInitiationResponse = new PaymentInitiationResponse();
    paymentInitiationResponse.set_links(new Links());      
    paymentInitiationResponse.setTransaction_status("Received");      
    
    paymentInitiationResponse.set_links(setLinks(paymentProduct));
    if (paymentInitiationResponse.get_links().getSelect_authentication_method() != null) {
      paymentInitiationResponse.setSca_methods(authenticationMethodList);
    } 

    return Response.status(Status.CREATED).entity(paymentInitiationResponse).build();
  }

  @Override
  public Response updatePSUData(String version, String paymentProduct, String paymentId, String processId, String requestId,
      String psuId, String psuCorporatedId, String accessToken, String signature, String certificate, String date,
      UpdatePSUDataRequest updatePSUDataRequest) {

    if (!util.checkValidation()) {
      // HTTP Code: 400
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (!util.checkAuthorization()) {
      // HTTP Code: 401
      return Response.status(Status.UNAUTHORIZED).build();
    }

    UpdatePSUDataResponse updatePSUDataResponse = new UpdatePSUDataResponse();
    updatePSUDataResponse.set_links(new Links());

    if (updatePSUDataRequest.getAuthentication_method_id() != null
        && updatePSUDataRequest.getAuthentication_method_id().length() > 0) {
      this.authenticationMethodId = updatePSUDataRequest.getAuthentication_method_id();
    }    
    if (psuId != null && psuId.length() > 0) {
      this.userId = psuId;
    }
    if (updatePSUDataRequest.getPsu_data() != null 
        && updatePSUDataRequest.getPsu_data() != null
        && updatePSUDataRequest.getPsu_data().getPassword() != null
        && updatePSUDataRequest.getPsu_data().getPassword().length() > 0) {
      this.userPassword = updatePSUDataRequest.getPsu_data().getPassword();
    }
    if (updatePSUDataRequest.getSca_authentication_data() != null        
        && updatePSUDataRequest.getSca_authentication_data().length() > 0) {
      this.authenticationData = updatePSUDataRequest.getSca_authentication_data();
    }

    updatePSUDataResponse.set_links(setLinks(paymentProduct));
    if (updatePSUDataResponse.get_links().getSelect_authentication_method() != null) {
      updatePSUDataResponse.setSca_methods(authenticationMethodList);
    } 

    updatePSUDataResponse.setTransaction_status("AcceptedTechnicalValidation");

    return Response.ok(updatePSUDataResponse).build();
  }

  @Override
  public Response statusRequest(String version, String paymentProduct, String paymentId, String processId, String requestId,
      String psuId, String accessToken, String signature, String certificate, String date) {

    if (!util.checkValidation()) {
      // HTTP Code: 400
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (!util.checkAuthorization()) {
      // HTTP Code: 401
      return Response.status(Status.UNAUTHORIZED).build();
    }

    StatusResponse paymentStatusResponse = new StatusResponse();
    paymentStatusResponse.setTransaction_status("AcceptedCustomerProfile");

    return Response.ok(paymentStatusResponse).build();
  }

  @Override
  public Response readData(String version, String paymentProduct, String paymentId, String processId, String requestId,
      String accessToken, String signature, String certificate, String date) {

    if (!util.checkValidation()) {
      // HTTP Code: 400
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (!util.checkAuthorization()) {
      // HTTP Code: 401
      return Response.status(Status.UNAUTHORIZED).build();
    }

    // readData Request is not described in the Berlin Group document, so can not be properly
    // implemented here
    PaymentInitiationResponse paymentInitiationResponse = new PaymentInitiationResponse();
    paymentInitiationResponse.setTransaction_status("Received");

    return Response.ok(paymentInitiationResponse).build();
  }

  /**
   * Set all links needed
   * 
   * @return Links
   */
  private Links setLinks(String paymentProduct) {
    
    Links links = new Links();
    boolean allUserData = true;
    if (this.authenticationMethodId == null) {      
      links.setSelect_authentication_method(String.format("/v1/payments/%s/1234-wertiq-983", paymentProduct));
      allUserData = false;
    }
    if (this.userId == null) {
      links.setUpdate_psu_identification(String.format("/v1/payments/%s/1234-wertiq-983", paymentProduct));
      allUserData = false;
    }
    if (this.userPassword == null) {
      links.setUpdate_psu_authentication(String.format("/v1/payments/%s/1234-wertiq-983", paymentProduct));
      allUserData = false;
    } 
    if (this.authenticationData == null) {
      links.setAuthorise_transaction(String.format("/v1/payments/%s/1234-wertiq-983", paymentProduct));
      allUserData = false;
    } 
    if (allUserData) {
      links.setStatus(String.format("/v1/payments/%s/1234-wertiq-983/status", paymentProduct));
    }
    return links;
  }
}
