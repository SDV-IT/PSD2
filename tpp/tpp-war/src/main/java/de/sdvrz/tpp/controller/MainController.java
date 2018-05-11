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
package de.sdvrz.tpp.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sdvrz.tpp.PropertyManager;
import de.sdvrz.tpp.authorization.AccessToken;
import de.sdvrz.tpp.authorization.model.AuthorizationModel;
import de.sdvrz.tpp.util.Helper;
import de.sdvrz.tpp.xs2a.accounts.AccountsClient;
import de.sdvrz.tpp.xs2a.confirmation.ConfirmationClient;
import de.sdvrz.tpp.xs2a.payment.PaymentsClient;

/**
 * Controller to interact with home.xhtml page
 * Performs all action started on the view: home.xhtml
 *
 */
@Named("controller")
@ApplicationScoped
public class MainController {

	@Inject
	AuthorizationModel model;

	@Inject
	PropertyManager propertyManager;
	
	@Inject
	AccessToken accessToken;
	
	@Inject
	AccountsClient accountsClient;
	
	@Inject
	PaymentsClient paymentsClient;
	
	@Inject
	ConfirmationClient confirmationClient;

	@PostConstruct
	void init() {
		if (propertyManager.getProperty("trust.all.certificates").equals("true")) {
			Helper.trustAllCertificatesCheck();
		}
	}	
	
	/**
	 * Request Method for Access Token 
	 * 
	 */
	public void getAccessToken() {
		accessToken.getAccessToken();
	}
	
	/**
	 * Request against XS2A Server: /accounts
	 */
	public void accounts() {
		accountsClient.accounts();
	}
	
	/**
	 * Request against XS2A Server: /accounts/{account-id}/balances
	 */
	public void accountsBalances() {
		accountsClient.accountsBalances();
	}
	
	/**
	 * Request against XS2A Server: /accounts/{account-id}/transactions
	 */
	public void accountsTransactions() {
		accountsClient.accountsTransactions();;
	}	
	
	/**
	 * Request against XS2A Server: /accounts/{account-id}/transactions
	 */
	public void paymentInitiation() {
		paymentsClient.paymentInitiation();
	}	
	
	/**
	 * Request against XS2A Server: /confirmation-of-funds
	 */
	public void confirmationOfFunds() {
		confirmationClient.confirmationOfFunds();
	}	

}
