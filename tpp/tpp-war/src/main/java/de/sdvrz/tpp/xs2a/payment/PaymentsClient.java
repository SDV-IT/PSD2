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
package de.sdvrz.tpp.xs2a.payment;

import java.net.HttpURLConnection;
import java.util.HashMap;
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
import de.sdvrz.tpp.xs2a.model.ViewModel;
import de.sdvrz.tpp.xs2a.model.Xs2aModel;
import de.sdvrz.xs2a.api.model.AccountAccount;
import de.sdvrz.xs2a.api.model.AccountAmount;
import de.sdvrz.xs2a.api.model.AccountName;
import de.sdvrz.xs2a.api.model.AuthenticationMethod;
import de.sdvrz.xs2a.api.model.Links;
import de.sdvrz.xs2a.api.model.PSUData;
import de.sdvrz.xs2a.api.model.PaymentInitiationRequest;
import de.sdvrz.xs2a.api.model.PaymentInitiationResponse;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataRequest;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;

/**
 * Client to perform Payment Initiations data flow
 *
 */
@Stateless
public class PaymentsClient {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentsClient.class);

  @Inject
  AuthorizationModel authorizationModel;

  @Inject
  Xs2aModel xs2aModel;

  @Inject
  ViewModel viewModel;

  @Inject
  Xs2aRestCommunicator xs2aRestCommunicator;

  /**
   * perform Payment Initiation
   */
  public void paymentInitiation() {
    viewModel.setHttpDataFlow(new StringBuilder());
    xs2aModel.setProcessUUID(UUID.randomUUID());

    Map<String, Object> response = paymentInitiationRequest();

    if ((int) response.get(Xs2aRestCommunicator.HTTP_CODE_KEY) != HttpURLConnection.HTTP_CREATED) {
      LOG.error("Error: Payment Initiation Request");
      return;
    }

    List<AuthenticationMethod> authenticationMethodList =
        ((PaymentInitiationResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).getSca_methods();
    Links links = ((PaymentInitiationResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).get_links();
    String pathToStatus = links.getStatus();

    while (true) {

      /*
       * In this loop an Embedded SCA Approach where PSU can set his password should be placed
       *
       */

      String pathToAuthenticationMethod = links.getSelect_authentication_method();
      String pathToUpdatePSUIdentification = links.getUpdate_psu_identification();
      String pathToUpdatePSUAuthentication = links.getUpdate_psu_authentication();
      String pathToAuthoriseTransaction = links.getAuthorise_transaction();

      if (pathToStatus != null && pathToStatus.length() > 0) {
        // Now go to Read Status
        break;
      } else if (pathToAuthenticationMethod != null && pathToAuthenticationMethod.length() > 0) {
        response = updatePSUData(pathToAuthenticationMethod, "AUTHENTICATION_METHOD", authenticationMethodList);
      } else if (pathToUpdatePSUIdentification != null && pathToUpdatePSUIdentification.length() > 0) {
        response = updatePSUData(pathToUpdatePSUIdentification, "PSU_IDENTIFICATION", authenticationMethodList);
      } else if (pathToUpdatePSUAuthentication != null && pathToUpdatePSUAuthentication.length() > 0) {
        response = updatePSUData(pathToUpdatePSUAuthentication, "PSU_AUTHENTICATION", authenticationMethodList);
      } else if (pathToAuthoriseTransaction != null && pathToAuthoriseTransaction.length() > 0) {
        response = updatePSUData(pathToAuthoriseTransaction, "AUTHORISATION", authenticationMethodList);        
      } else {
        LOG.error("Error: No one of expeted links received ");
        return;
      }

      if (response == null) {
        return;
      }

      if ((int) response.get(Xs2aRestCommunicator.HTTP_CODE_KEY) != HttpURLConnection.HTTP_OK
          || !((UpdatePSUDataResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).getTransaction_status()
              .equals("AcceptedTechnicalValidation")) {
        LOG.error("Error: HTTP code is not 200 or Transaction_status is not 'AcceptedTechnicalValidation'");
        return;
      }

      links = ((UpdatePSUDataResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).get_links();
      pathToStatus = links.getStatus();
      authenticationMethodList = ((UpdatePSUDataResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).getSca_methods();
    }

    response = statusRequest(pathToStatus);
    if ((int) response.get(Xs2aRestCommunicator.HTTP_CODE_KEY) == HttpURLConnection.HTTP_OK
        && ((StatusResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).getTransaction_status()
            .equals("AcceptedCustomerProfile")) {
      // Do anything for last response in Payment Initiation chain
    }
  }

  /**
   * POST Account Information Consent Request
   * 
   * @return Map with HTTP Code / body data
   */
  private Map<String, Object> paymentInitiationRequest() {
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
      requestPropertyMap.put("PSU-IP-Address", "192.168.1.1");
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();
      paymentInitiationRequest.setInstructed_amount(new AccountAmount());
      paymentInitiationRequest.getInstructed_amount().setCurrency("EUR");
      paymentInitiationRequest.getInstructed_amount().setContent("123");
      paymentInitiationRequest.setDebtor_account(new AccountAccount());
      paymentInitiationRequest.getDebtor_account().setIban("DE2310010010123456789");
      paymentInitiationRequest.setCreditor(new AccountName());
      paymentInitiationRequest.getCreditor().setName("Merchant123");
      paymentInitiationRequest.setCreditor_account(new AccountAccount());
      paymentInitiationRequest.getCreditor_account().setIban("DE23100120020123456789");
      paymentInitiationRequest.setRemittance_information_structured("Ref Number Merchant-123456");

      response = xs2aRestCommunicator.sendAndReceive("Payment Initiation Request (page 25)", "/v1/payments/sepa-credit-transfers",
          "POST", requestPropertyMap, null, paymentInitiationRequest, PaymentInitiationResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
    return response;
  }

  /**
   * PUT Update PSU Data
   * 
   * @param path from _links in response of Account Information Consent Request or Update PSU
   *        Request
   * @param type Type of Update PSU Request
   * @param authenticationMethodList List of authentication methods - only with type =
   *        'AUTHENTICATION_METHOD' relevant
   * @return Map with HTTP Code / body data
   */
  private Map<String, Object> updatePSUData(String path, String type, List<AuthenticationMethod> authenticationMethodList) {
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
      String updatePsuDataType = "";
      if (type.equals("AUTHENTICATION_METHOD")) {
        
        for (AuthenticationMethod authenticationMethod : authenticationMethodList) {
          updatePSUDataRequest.setAuthentication_method_id(authenticationMethod.getAuthentication_method_id());
        }
        updatePsuDataType = " - Authentication Method (page 76)";
      } else if (type.equals("PSU_IDENTIFICATION")) {
        
        requestPropertyMap.put("PSU-ID", "id of PSU");
        updatePsuDataType = " - PSU Identification (page 70)";
      } else if (type.equals("PSU_AUTHENTICATION")) {

        updatePSUDataRequest.setPsu_data(new PSUData());
        updatePSUDataRequest.getPsu_data().setPassword("********");
        updatePsuDataType = " - PSU Authentication (page 72)";
      } else if (type.equals("AUTHORISATION")) {

        updatePSUDataRequest.setSca_authentication_data("TAN123");;
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
   * GET payment Status Request
   * 
   * @param path from _links in response of Account Information Consent Request
   * @return Map with HTTP Code / body data
   */
  private Map<String, Object> statusRequest(String path) {
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

      response = xs2aRestCommunicator.sendAndReceive("Get Status Request (page 33)", path, "GET", requestPropertyMap, null, null,
          StatusResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
    return response;
  }

}
