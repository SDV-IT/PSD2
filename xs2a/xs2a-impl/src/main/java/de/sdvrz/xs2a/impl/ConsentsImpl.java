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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sdvrz.xs2a.api.Consents;
import de.sdvrz.xs2a.api.model.AuthenticationMethod;
import de.sdvrz.xs2a.api.model.ConsentAccessAccount;
import de.sdvrz.xs2a.api.model.ConsentRequestResponse;
import de.sdvrz.xs2a.api.model.Links;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.InformationConsentRequest;
import de.sdvrz.xs2a.api.model.InformationConsentResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataRequest;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;
import de.sdvrz.xs2a.util.Util;

/**
 * Implement class for Consent interface
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class ConsentsImpl implements Consents {

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
  public Response informationConsent(String version, String processId, String requestId, String psuId, String psuCorporatedId,
      String accessToken, String signature, String certificate, String date, InformationConsentRequest informationConsentRequest) {

    // initialize all user data
    authenticationMethodId = "*"; // only accounts uses consent and accounts does not need authorization (TAN, etc)    
    userId = util.checkAccessToken(accessToken) ? "*" : null;  // if Access Token userId is not needed
    userPassword = util.checkAccessToken(accessToken) ? "*" : null; // if Access Token userPassword is not needed
    authenticationData = "*"; // only accounts uses consent and accounts does not need authorization (TAN, etc)

    InformationConsentResponse informationConsentResponse = new InformationConsentResponse();    
    informationConsentResponse.setTransaction_status("Received");
    informationConsentResponse.set_links(new Links());
    
    informationConsentResponse.set_links(setLinks());
    if (informationConsentResponse.get_links().getSelect_authentication_method() != null) {
      informationConsentResponse.setSca_methods(authenticationMethodList);
    } 
    
    return Response.status(Status.CREATED).entity(informationConsentResponse).build();
  }

  @Override
  public Response updatePSUData(String version, String consentId, String processId, String requestId, String psuId,
      String psuCorporatedId, String accessToken, String signature, String certificate, String date,
      UpdatePSUDataRequest updatePSUDataRequest) {

    UpdatePSUDataResponse updatePSUDataResponse = new UpdatePSUDataResponse();
    updatePSUDataResponse.set_links(new Links());

    if (updatePSUDataRequest.getAuthentication_method_id() != null
        && updatePSUDataRequest.getAuthentication_method_id().length() > 0) {
      this.authenticationMethodId = updatePSUDataRequest.getAuthentication_method_id();
    }
    if (psuId != null && psuId.length() > 0) {
      this.userId = psuId;
    }
    if (updatePSUDataRequest.getPsu_data() != null && updatePSUDataRequest.getPsu_data() != null
        && updatePSUDataRequest.getPsu_data().getPassword() != null
        && updatePSUDataRequest.getPsu_data().getPassword().length() > 0) {
      this.userPassword = updatePSUDataRequest.getPsu_data().getPassword();
    }
    if (updatePSUDataRequest.getSca_authentication_data() != null
        && updatePSUDataRequest.getSca_authentication_data().length() > 0) {
      this.authenticationData = updatePSUDataRequest.getSca_authentication_data();
    }
    
    updatePSUDataResponse.set_links(setLinks());
    if (updatePSUDataResponse.get_links().getSelect_authentication_method() != null) {
      updatePSUDataResponse.setSca_methods(authenticationMethodList);
    }    

    updatePSUDataResponse.setTransaction_status("AcceptedTechnicalValidation");

    return Response.ok(updatePSUDataResponse).build();
  }

  @Override
  public Response consentRequest(String version, String consentId, String processId, String requestId, String psuId,
      String psuCorporatedId, String accessToken, String signature, String certificate, String date) {

    ConsentRequestResponse consentRequestResponse = new ConsentRequestResponse();
    consentRequestResponse.setAccess_accounts(new LinkedList<>());
    ConsentAccessAccount consentAccessAccount = new ConsentAccessAccount();
    consentAccessAccount.setIban("DE2310010010123456789");
    consentAccessAccount.setAccess(new LinkedList<>());
    consentAccessAccount.getAccess().add("balance");
    consentAccessAccount.getAccess().add("transactions");
    consentRequestResponse.getAccess_accounts().add(consentAccessAccount);
    consentAccessAccount = new ConsentAccessAccount();
    consentAccessAccount.setIban("DE2310010010123456788");
    consentAccessAccount.setAccess(new LinkedList<>());
    consentAccessAccount.getAccess().add("balance");
    consentRequestResponse.getAccess_accounts().add(consentAccessAccount);
    consentAccessAccount = new ConsentAccessAccount();
    consentAccessAccount.setPan("1234567890-12345");
    consentAccessAccount.setAccess(new LinkedList<>());
    consentAccessAccount.getAccess().add("transactions");
    consentRequestResponse.getAccess_accounts().add(consentAccessAccount);
    consentRequestResponse.setRecurring_indicator(true);

    Calendar calendar = Calendar.getInstance();
    calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    consentRequestResponse.setValid_until(dateFormat.format(calendar.getTime()));
    consentRequestResponse.setFrequency_per_day(4);
    consentRequestResponse.setTransaction_status("AcceptedCustomerProfile");
    consentRequestResponse.setConsent_status("valid");

    return Response.ok(consentRequestResponse).build();
  }

  @Override
  public Response deleteConsent(String version, String consentId, String processId, String requestId, String accessToken) {

    return Response.status(Status.NO_CONTENT).build();
  }

  @Override
  public Response statusRequest(String version, String consentId, String processId, String requestId, String psuId,
      String psuCorporateId, String accessToken, String signature, String certificate, String date) {

    StatusResponse statusResponse = new StatusResponse();
    statusResponse.setTransaction_status("AcceptedCustomerProfile");

    return Response.ok(statusResponse).build();
  }

  /**
   * Set all links needed
   * 
   * @return Links
   */
  private Links setLinks() {
    
    Links links = new Links();
    boolean allUserData = true;
    if (this.authenticationMethodId == null) {      
      links.setSelect_authentication_method("/v1/consents/1234-wertiq-983");
      allUserData = false;
    }
    if (this.userId == null) {
      links.setUpdate_psu_identification("/v1/consents/1234-wertiq-983");
      allUserData = false;
    }
    if (this.userPassword == null) {
      links.setUpdate_psu_authentication("/v1/consents/1234-wertiq-983");
      allUserData = false;
    } 
    if (this.authenticationData == null) {
      links.setAuthorise_transaction("/v1/consents/1234-wertiq-983");
      allUserData = false;
    } 
    if (allUserData) {
      links.setStatus("/v1/consents/1234-wertiq-983/status");
    }
    return links;
  }

}
