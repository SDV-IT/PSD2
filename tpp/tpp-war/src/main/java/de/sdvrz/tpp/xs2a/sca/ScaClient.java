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
package de.sdvrz.tpp.xs2a.sca;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sdvrz.tpp.authorization.model.AuthorizationModel;
import de.sdvrz.tpp.util.Helper;
import de.sdvrz.tpp.xs2a.Xs2aRestCommunicator;
import de.sdvrz.tpp.xs2a.model.Xs2aModel;
import de.sdvrz.xs2a.api.model.AuthenticationMethod;
import de.sdvrz.xs2a.api.model.ConsentAccessAccount;
import de.sdvrz.xs2a.api.model.ConsentRequestResponse;
import de.sdvrz.xs2a.api.model.InformationConsentRequest;
import de.sdvrz.xs2a.api.model.InformationConsentResponse;
import de.sdvrz.xs2a.api.model.PSUData;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataRequest;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;

/**
 * Client to perform SCA (Strong Customer Authentication) data flow to XS2A server
 *
 */
@Stateless
public class ScaClient {

  private static final Logger LOG = LoggerFactory.getLogger(ScaClient.class);

  @Inject
  AuthorizationModel authorizationModel;

  @Inject
  Xs2aModel xs2aModel;

  @Inject
  Xs2aRestCommunicator xs2aRestCommunicator;

  /**
   * POST Account Information Consent Request
   * 
   * @param recurringIndicator Recurring indicator for consent
   * @param validUntil Valid until for consent
   * @param frequencyPerDay Frequency pre day for consent
   * @return Map with HTTP Code / body data
   */
  public Map<String, Object> accountInformationConsentRequest(boolean recurringIndicator, String validUntil, int frequencyPerDay) {
    Map<String, Object> response = null;
    try {
      Map<String, String> requestPropertyMap = new HashMap<>();
      requestPropertyMap.put("Content-Type", "application/json");
      requestPropertyMap.put("Accept", "application/json");
      requestPropertyMap.put("Process-ID", xs2aModel.getProcessUUID().toString());
      requestPropertyMap.put("Request-ID", UUID.randomUUID().toString());
      if (authorizationModel.getAccessTokenResponseModel().getToken_type() != null
          && authorizationModel.getAccessTokenResponseModel().getToken_type().length() > 0
          && authorizationModel.getAccessTokenResponseModel().getAccess_token() != null
          && authorizationModel.getAccessTokenResponseModel().getAccess_token().length() > 0) {
        requestPropertyMap.put("Authorization", authorizationModel.getAccessTokenResponseModel().getToken_type() + " "
          + authorizationModel.getAccessTokenResponseModel().getAccess_token());
      }
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      InformationConsentRequest informationConsentRequest = new InformationConsentRequest();
      informationConsentRequest.setAccess_accounts(new LinkedList<>());
      ConsentAccessAccount informationConsentAccessAccount = new ConsentAccessAccount();
      informationConsentAccessAccount.setIban("DE2310010010123456789");
      informationConsentAccessAccount.setAccess(new LinkedList<>());
      informationConsentAccessAccount.getAccess().add("balance");
      informationConsentAccessAccount.getAccess().add("transactions");
      informationConsentRequest.getAccess_accounts().add(informationConsentAccessAccount);
      informationConsentAccessAccount = new ConsentAccessAccount();
      informationConsentAccessAccount.setIban("DE2310010010123456788");
      informationConsentAccessAccount.setAccess(new LinkedList<>());
      informationConsentAccessAccount.getAccess().add("balance");
      informationConsentRequest.getAccess_accounts().add(informationConsentAccessAccount);
      informationConsentAccessAccount = new ConsentAccessAccount();
      informationConsentAccessAccount.setPan("1234567890-12345");
      informationConsentAccessAccount.setAccess(new LinkedList<>());
      informationConsentAccessAccount.getAccess().add("transactions");
      informationConsentRequest.getAccess_accounts().add(informationConsentAccessAccount);
      informationConsentRequest.setRecurring_indicator(true);
      informationConsentRequest.setValid_until(Helper.getValidUntil(2));
      informationConsentRequest.setFrequency_per_day(4);
      informationConsentRequest.setCombined_service_indicator(false);

      response = xs2aRestCommunicator.sendAndReceive("Account Information Consent Request (page 48)", "/v1/consents", "POST",
          requestPropertyMap, null, informationConsentRequest, InformationConsentResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
    return response;
  }

  /**
   * PUT Update PSU Data
   * 
   * @param path from _links in response of Account Information Consent Request
   * @param type Type of Update PSU Request
   * @param authenticationMethodList List of authentication methods - only with type =
   *        'AUTHENTICATION_METHOD' relevant
   * @return Map with HTTP Code / body data
   */
  public Map<String, Object> updatePSUData(String path, String type, List<AuthenticationMethod> authenticationMethodList) {
    Map<String, Object> response = null;
    try {
      Map<String, String> requestPropertyMap = new HashMap<>();
      requestPropertyMap.put("Content-Type", "application/json");
      requestPropertyMap.put("Accept", "application/json");
      requestPropertyMap.put("Process-ID", xs2aModel.getProcessUUID().toString());
      requestPropertyMap.put("Request-ID", UUID.randomUUID().toString());
      if (authorizationModel.getAccessTokenResponseModel().getToken_type() != null
          && authorizationModel.getAccessTokenResponseModel().getToken_type().length() > 0
          && authorizationModel.getAccessTokenResponseModel().getAccess_token() != null
          && authorizationModel.getAccessTokenResponseModel().getAccess_token().length() > 0) {
        requestPropertyMap.put("Authorization", authorizationModel.getAccessTokenResponseModel().getToken_type() + " "
          + authorizationModel.getAccessTokenResponseModel().getAccess_token());
      }
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      UpdatePSUDataRequest updatePSUDataRequest = new UpdatePSUDataRequest();
      String updatePsuDataType;
      if (type.equals("AUTHENTICATION_METHOD")) {
        
        for (AuthenticationMethod authenticationMethod : authenticationMethodList) {
          updatePSUDataRequest.setAuthentication_method_id(authenticationMethod.getAuthentication_method_id());
        }
        updatePsuDataType = " - Authentication Method (page 76)";
      } else if (type.equals("PSU_IDENTIFICATION")) {
        
        requestPropertyMap.put("PSU-ID", "id of PSU");
        updatePsuDataType = " - PSU Identification (page 70)";
      } else if (type.equals("PSU_AUTHENTIFICATION")) {

        updatePSUDataRequest.setPsu_data(new PSUData());
        updatePSUDataRequest.getPsu_data().setPassword("********");
        updatePsuDataType = " - PSU Authentication (page 72)";
      } else if (type.equals("AUTHORISATION")) {
        
        updatePSUDataRequest.setSca_authentication_data("TAN123");
        updatePsuDataType = " - PSU Authorisation (page 79)";
      } else {
        LOG.error("type specialisation of Update PSU Data not recognized");
        return null;
      }

      response = xs2aRestCommunicator.sendAndReceive(String.format("Update PSU Data%s", updatePsuDataType), path, "PUT",
          requestPropertyMap, null, updatePSUDataRequest, UpdatePSUDataResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
    return response;
  }

  /**
   * GET Status Request
   * 
   * @param path from _links in response of Account Information Consent Request
   * @return Map with HTTP Code / body data
   */
  public Map<String, Object> statusRequest(String path) {
    Map<String, Object> response = null;
    try {
      Map<String, String> requestPropertyMap = new HashMap<>();
      requestPropertyMap.put("Accept", "application/json");
      requestPropertyMap.put("Process-ID", xs2aModel.getProcessUUID().toString());
      requestPropertyMap.put("Request-ID", UUID.randomUUID().toString());
      if (authorizationModel.getAccessTokenResponseModel().getToken_type() != null
          && authorizationModel.getAccessTokenResponseModel().getToken_type().length() > 0
          && authorizationModel.getAccessTokenResponseModel().getAccess_token() != null
          && authorizationModel.getAccessTokenResponseModel().getAccess_token().length() > 0) {
        requestPropertyMap.put("Authorization", authorizationModel.getAccessTokenResponseModel().getToken_type() + " "
          + authorizationModel.getAccessTokenResponseModel().getAccess_token());
      }
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      response = xs2aRestCommunicator.sendAndReceive("Get Status Request (page 55)", path, "GET", requestPropertyMap, null, null,
          StatusResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
    return response;
  }


  /**
   * GET Consent Request
   * 
   * @param path from _links in response of Account Information Consent Request
   * @return Map with HTTP Code / body data
   */
  public Map<String, Object> consentRequest(String path) {
    Map<String, Object> response = null;
    try {
      Map<String, String> requestPropertyMap = new HashMap<>();
      requestPropertyMap.put("Accept", "application/json");
      requestPropertyMap.put("Process-ID", xs2aModel.getProcessUUID().toString());
      requestPropertyMap.put("Request-ID", UUID.randomUUID().toString());
      if (authorizationModel.getAccessTokenResponseModel().getToken_type() != null
          && authorizationModel.getAccessTokenResponseModel().getToken_type().length() > 0
          && authorizationModel.getAccessTokenResponseModel().getAccess_token() != null
          && authorizationModel.getAccessTokenResponseModel().getAccess_token().length() > 0) {
        requestPropertyMap.put("Authorization", authorizationModel.getAccessTokenResponseModel().getToken_type() + " "
          + authorizationModel.getAccessTokenResponseModel().getAccess_token());
      }
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      response = xs2aRestCommunicator.sendAndReceive("Get Consent Request (page 56)", path, "GET", requestPropertyMap, null, null,
          ConsentRequestResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
    return response;
  }

}
