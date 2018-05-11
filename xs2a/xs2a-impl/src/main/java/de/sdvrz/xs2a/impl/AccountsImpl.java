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
package de.sdvrz.xs2a.impl;

import java.util.LinkedList;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sdvrz.xs2a.api.Accounts;
import de.sdvrz.xs2a.api.model.Account;
import de.sdvrz.xs2a.api.model.AccountAccount;
import de.sdvrz.xs2a.api.model.AccountAmount;
import de.sdvrz.xs2a.api.model.AccountBalance;
import de.sdvrz.xs2a.api.model.AccountBalanceInf;
import de.sdvrz.xs2a.api.model.AccountBalanceResponse;
import de.sdvrz.xs2a.api.model.AccountResponse;
import de.sdvrz.xs2a.api.model.AccountTransaction;
import de.sdvrz.xs2a.api.model.AccountTransactions;
import de.sdvrz.xs2a.api.model.AccountTransactionsResponse;
import de.sdvrz.xs2a.api.model.Links;
import de.sdvrz.xs2a.util.Util;

/**
 * Implement class for Accounts Interface
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class AccountsImpl implements Accounts {

	@Inject
	Util util;

	@Override
	public Response readAccountList(String version, String processId, String requestId, String consentId, String psuId, String accessToken) {
		if (!util.checkValidation()) {
			// HTTP Code: 400
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (!util.checkAuthorization()) {
			// HTTP Code: 401
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		AccountResponse accountResponse = new AccountResponse();
		accountResponse.setAccounts(new LinkedList<>());		
		
		Account account = new Account();
		account.setId("3dc3d5b3-7023-4848-9853-f5400a64e80f");
		account.setIban("DE2310010010123456789");
		account.setAccount_type("Main Account");
		account.setCurrency("EUR");
		account.set_links(new Links());
		account.get_links().setBalances("/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/balances");
		account.get_links().setTransaction("/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions");
		accountResponse.getAccounts().add(account);
		
		account = new Account();
		account.setId("3dc3d5b3-7023-4848-9853-f5400a64e81g");
		account.setIban("DE2310010010123456788");
		account.setAccount_type("US Dollar Account");
		account.setCurrency("USD");
		account.set_links(new Links());
		account.get_links().setBalances("/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e81g/balances");			
		accountResponse.getAccounts().add(account);
		
		account = new Account();
		account.setId("1234567890-12345");
		account.setPan("1234567890-12345");
		account.setAccount_type("Credit Card");
		account.setCurrency("EUR");
		account.set_links(new Links());
		account.get_links().setTransaction("/v1/accounts/1234567890-12345/transactions");		
		accountResponse.getAccounts().add(account);

		return Response.ok(accountResponse).build();
	}

	@Override
	public Response readBalance(String version, String accountId, String processId, String requestId, String consentId, String psuId, 
			String accessToken, String signature, String certificate, String date) {

		if (!util.checkValidation()) {
			// HTTP Code: 400
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (!util.checkAuthorization()) {
			// HTTP Code: 401
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		AccountBalanceResponse accountBalanceResponse = new AccountBalanceResponse();
		accountBalanceResponse.setBalances(new LinkedList<>());
		AccountBalance accountBalance = new AccountBalance();
		AccountBalanceInf accountBalanceInf = new AccountBalanceInf();
		AccountAmount accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("500.00");
		accountBalanceInf.setAmount(accountAmount);
		accountBalanceInf.setDate("2017-10-25");
		accountBalance.setClosing_booked(accountBalanceInf);
		accountBalanceResponse.getBalances().add(accountBalance);
		accountBalance = new AccountBalance();
		accountBalanceInf = new AccountBalanceInf();
		accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("900.00");
		accountBalanceInf.setAmount(accountAmount);
		accountBalanceInf.setDate("2017-10-25");
		accountBalance.setExpected(accountBalanceInf);
		accountBalanceResponse.getBalances().add(accountBalance);
		
		return Response.ok(accountBalanceResponse).build();
	}

	@Override
	public Response readTransactions(String version, String accountId, String processId, String requestId,
			String consentId, String psuId, String accessToken, String signature, String certificate, String date, String dateFrom, String dateTo) {

		if (!util.checkValidation()) {
			// HTTP Code: 400
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (!util.checkAuthorization()) {
			// HTTP Code: 401
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		AccountTransactionsResponse accountTransactionsResponse = new AccountTransactionsResponse();
		AccountTransactions accountTransactions = new AccountTransactions();
		accountTransactions.setBooked(new LinkedList<>());
		
		AccountTransaction accountTransaction = new AccountTransaction();
		accountTransaction.setTransaction_id("1234567");
		accountTransaction.setCreditor_name("John Miles");
		AccountAccount accountAccount = new AccountAccount();
		accountAccount.setIban("DE43533700240123456900");
		accountTransaction.setCreditor_account(accountAccount);
		AccountAmount accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("-256,67");
		accountTransaction.setAmount(accountAmount);
		accountTransaction.setBookingDate("2017-10-25");
		accountTransaction.setValue_date("2017-10-26");
		accountTransaction.setRemittance_information_unstructured("Example for Remittance Information");
		accountTransactions.getBooked().add(accountTransaction);
		
		accountTransaction = new AccountTransaction();
		accountTransaction.setTransaction_id("1234568");
		accountTransaction.setDebtor_name("Paul Simpson");
		accountAccount = new AccountAccount();
		accountAccount.setIban("NL354543123456900");
		accountTransaction.setDebtor_account(accountAccount);
		accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("343,01");
		accountTransaction.setAmount(accountAmount);
		accountTransaction.setBookingDate("2017-10-25");
		accountTransaction.setValue_date("2017-10-26");
		accountTransaction.setRemittance_information_unstructured("Another example for Remittance Information");
		accountTransactions.getBooked().add(accountTransaction);		
		
		accountTransactions.setPending(new LinkedList<>());
		
		accountTransaction = new AccountTransaction();
		accountTransaction.setTransaction_id("1234569");
		accountTransaction.setCreditor_name("Claude Renault");
		accountAccount = new AccountAccount();
		accountAccount.setIban("FR33554543123456900");
		accountTransaction.setCreditor_account(accountAccount);
		accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("-100,03");
		accountTransaction.setAmount(accountAmount);		
		accountTransaction.setValue_date("2017-10-26");
		accountTransaction.setRemittance_information_unstructured("Third example for Remittance Information");
		accountTransactions.getPending().add(accountTransaction);
		accountTransactions.set_links(new Links());
		accountTransactions.get_links().setAccount_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions");
		accountTransactions.get_links().setFirst_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/first");
		accountTransactions.get_links().setSecond_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/second");
		accountTransactions.get_links().setCurrent_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/current");
		accountTransactions.get_links().setLast_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/last");
		
		accountTransactionsResponse.setTransactions(accountTransactions);
		
		return Response.ok(accountTransactionsResponse).build();
	}

	@Override
	public Response readTransactionsPage(String version, String accountId, String page, String processId,
			String requestId, String consentId, String psuId, String accessToken, String signature, String certificate,
			String date, String dateFrom, String dateTo) {
		
		if (!util.checkValidation()) {
			// HTTP Code: 400
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (!util.checkAuthorization()) {
			// HTTP Code: 401
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		AccountTransactionsResponse accountTransactionsResponse = new AccountTransactionsResponse();
		AccountTransactions accountTransactions = new AccountTransactions();
		accountTransactions.setBooked(new LinkedList<>());
		
		AccountTransaction accountTransaction = new AccountTransaction();
		accountTransaction.setTransaction_id("1234567");
		accountTransaction.setCreditor_name("John Miles");
		AccountAccount accountAccount = new AccountAccount();
		accountAccount.setIban("DE43533700240123456900");
		accountTransaction.setCreditor_account(accountAccount);
		AccountAmount accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("-256,67");
		accountTransaction.setAmount(accountAmount);
		accountTransaction.setBookingDate("2017-10-25");
		accountTransaction.setValue_date("2017-10-26");
		accountTransaction.setRemittance_information_unstructured("Example for Remittance Information");
		accountTransactions.getBooked().add(accountTransaction);
		
		accountTransaction = new AccountTransaction();
		accountTransaction.setTransaction_id("1234568");
		accountTransaction.setDebtor_name("Paul Simpson");
		accountAccount = new AccountAccount();
		accountAccount.setIban("NL354543123456900");
		accountTransaction.setDebtor_account(accountAccount);
		accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("343,01");
		accountTransaction.setAmount(accountAmount);
		accountTransaction.setBookingDate("2017-10-25");
		accountTransaction.setValue_date("2017-10-26");
		accountTransaction.setRemittance_information_unstructured("Another example for Remittance Information");
		accountTransactions.getBooked().add(accountTransaction);		
		
		accountTransactions.setPending(new LinkedList<>());
		
		accountTransaction = new AccountTransaction();
		accountTransaction.setTransaction_id("1234569");
		accountTransaction.setCreditor_name("Claude Renault");
		accountAccount = new AccountAccount();
		accountAccount.setIban("FR33554543123456900");
		accountTransaction.setCreditor_account(accountAccount);
		accountAmount = new AccountAmount();
		accountAmount.setCurrency("EUR");
		accountAmount.setContent("-100,03");
		accountTransaction.setAmount(accountAmount);		
		accountTransaction.setValue_date("2017-10-26");
		accountTransaction.setRemittance_information_unstructured("Third example for Remittance Information");
		accountTransactions.getPending().add(accountTransaction);
		accountTransactions.set_links(new Links());
		accountTransactions.get_links().setAccount_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions");
		accountTransactions.get_links().setFirst_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/first");
		accountTransactions.get_links().setSecond_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/second");
		accountTransactions.get_links().setCurrent_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/current");
		accountTransactions.get_links().setLast_page_link("v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions/last");
		
		accountTransactionsResponse.setTransactions(accountTransactions);
		
		return Response.ok(accountTransactionsResponse).build();
	}
	
	

}
