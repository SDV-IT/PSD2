/*
 *
 * Copyright (C) 2017 Sparda-Datenverarbeitung eG Freiligrathstrasse 32, 90482 Nuernberg, Germany
 *
 * This software is the confidential and proprietary information of Sparda-Datenverarbeitung eG (
 * "Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * Sparda-Datenverarbeitung eG.
 */
package de.sdvrz.tpp.xs2a.accounts;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sdvrz.tpp.authorization.model.AuthorizationModel;
import de.sdvrz.tpp.util.Helper;
import de.sdvrz.tpp.xs2a.Xs2aRestCommunicator;
import de.sdvrz.tpp.xs2a.model.ViewModel;
import de.sdvrz.tpp.xs2a.model.Xs2aModel;
import de.sdvrz.tpp.xs2a.sca.ScaClient;
import de.sdvrz.xs2a.api.model.AccountBalanceResponse;
import de.sdvrz.xs2a.api.model.AccountResponse;
import de.sdvrz.xs2a.api.model.AccountTransactionsResponse;
import de.sdvrz.xs2a.api.model.AuthenticationMethod;
import de.sdvrz.xs2a.api.model.InformationConsentResponse;
import de.sdvrz.xs2a.api.model.Links;
import de.sdvrz.xs2a.api.model.StatusResponse;
import de.sdvrz.xs2a.api.model.UpdatePSUDataResponse;

/**
 * Client for /accounts Requests
 *
 */
@SessionScoped
public class AccountsClient implements Serializable {

  private static final long serialVersionUID = 5669254400258579652L;

  private static final Logger LOG = LoggerFactory.getLogger(AccountsClient.class);

  @Inject
  AuthorizationModel authorizationModel;

  @Inject
  Xs2aModel xs2aModel;

  @Inject
  ViewModel viewModel;

  @Inject
  Xs2aRestCommunicator xs2aRestCommunicator;

  @Inject
  ScaClient scaClient;

  /**
   * perform /v1/accounts
   */
  public void accounts() {
    viewModel.setHttpDataFlow(new StringBuilder());
    xs2aModel.setProcessUUID(UUID.randomUUID());
    
    performSca();

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
      } else {
        requestPropertyMap.put("Consent-ID", xs2aModel.getConsentId());        
      }

      xs2aRestCommunicator.sendAndReceive("Read Account List (page 59)", "/v1/accounts", "GET", requestPropertyMap, null, null,
          AccountResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
  }

  /**
   * perform /v1/accounts/{account-id}/balances
   */
  public void accountsBalances() {
    viewModel.setHttpDataFlow(new StringBuilder());
    xs2aModel.setProcessUUID(UUID.randomUUID());

    performSca();

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
      } else {
        requestPropertyMap.put("Consent-ID", xs2aModel.getConsentId());        
      }
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      xs2aRestCommunicator.sendAndReceive("Read Balance (page 61)", "/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/balances",
          "GET", requestPropertyMap, null, null, AccountBalanceResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
  }

  /**
   * perform /v1/accounts/{account-id}/transactions
   */
  public void accountsTransactions() {
    viewModel.setHttpDataFlow(new StringBuilder());
    xs2aModel.setProcessUUID(UUID.randomUUID());

    performSca();
    
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
      } else {
        requestPropertyMap.put("Consent-ID", xs2aModel.getConsentId());
      }
      requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());

      Map<String, String> queryStringMap = new HashMap<>();
      queryStringMap.put("date_from", "2017-07-01");
      queryStringMap.put("date_to", "2017-07-30");

      xs2aRestCommunicator.sendAndReceive("Read Transaction List (page 64)",
          "/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/", "GET", requestPropertyMap, queryStringMap, null,
          AccountTransactionsResponse.class);

    } catch (Exception e) {
      LOG.error("Error during communication", e);
    }
  }

  /**
   * perform Strong Customer Authentication
   */
  private void performSca() {
    if (authorizationModel.getAccessTokenResponseModel().getToken_type() != null
        && authorizationModel.getAccessTokenResponseModel().getToken_type().length() > 0
        && authorizationModel.getAccessTokenResponseModel().getAccess_token() != null
        && authorizationModel.getAccessTokenResponseModel().getAccess_token().length() > 0) {
      // Access Token - SCA not needed
      return;
    }
    
    if (xs2aModel.isConsentValid()) {
      // Consent valid - SCA not needed
      return;
    }
    
    boolean recurringIndicator = true;
    String validUntil = Helper.getValidUntil(2);
    int frequencyPerDay = 4;

    Map<String, Object> response = scaClient.accountInformationConsentRequest(recurringIndicator, validUntil, frequencyPerDay);

    if ((int) response.get(Xs2aRestCommunicator.HTTP_CODE_KEY) != HttpURLConnection.HTTP_CREATED) {
      LOG.error("Error: Payment Initiation Request. Expected return code 201, got {}", response.get(Xs2aRestCommunicator.HTTP_CODE_KEY));
      return;
    }

    List<AuthenticationMethod> authenticationMethodList =
        ((InformationConsentResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).getSca_methods();
    Links links = ((InformationConsentResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).get_links();
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
        response = scaClient.updatePSUData(pathToAuthenticationMethod, "AUTHENTICATION_METHOD", authenticationMethodList);
      } else if (pathToUpdatePSUIdentification != null && pathToUpdatePSUIdentification.length() > 0) {
        response = scaClient.updatePSUData(pathToUpdatePSUIdentification, "PSU_IDENTIFICATION", authenticationMethodList);
      } else if (pathToUpdatePSUAuthentication != null && pathToUpdatePSUAuthentication.length() > 0) {
        response = scaClient.updatePSUData(pathToUpdatePSUAuthentication, "PSU_AUTHENTIFICATION", authenticationMethodList);
      } else if (pathToAuthoriseTransaction != null && pathToAuthoriseTransaction.length() > 0) {
        response = scaClient.updatePSUData(pathToAuthoriseTransaction, "AUTHORISATION", authenticationMethodList);
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

    response = scaClient.statusRequest(pathToStatus);
    if ((int) response.get(Xs2aRestCommunicator.HTTP_CODE_KEY) == HttpURLConnection.HTTP_OK
        && ((StatusResponse) response.get(Xs2aRestCommunicator.OUTPUT_KEY)).getTransaction_status()
            .equals("AcceptedCustomerProfile")) {

      xs2aModel.setConsentData(pathToStatus.replaceAll("^.*/([^/]+)/status", "$1"), // consent-ID
          validUntil, // valid until
          recurringIndicator, // recurring indicator
          frequencyPerDay); // frequency per day

      // Optionally a Get Consent Request can be sent to get all consent data for consent-ID
    }
  }

}
