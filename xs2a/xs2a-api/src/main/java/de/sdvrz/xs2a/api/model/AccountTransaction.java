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
package de.sdvrz.xs2a.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 */
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "AccountTransaction", subTypes = {AccountAccount.class, AccountAmount.class})
public class AccountTransaction {
	
	@ApiModelProperty(value = "ID for transaction", required = true, example = "1234567")
	String transaction_id;
	@ApiModelProperty(value = "Creditor name", required = false, example = "John Miles")
	String creditor_name;
	@ApiModelProperty(value = "Creditor account", required = false)
	AccountAccount creditor_account;
	@ApiModelProperty(value = "Debtor name", required = false, example = "Paul Simpson")
	String debtor_name;
	@ApiModelProperty(value = "Debtor account", required = false)
	AccountAccount debtor_account;
	@ApiModelProperty(value = "Amount", required = true)
	AccountAmount amount;
	@ApiModelProperty(value = "Booking date", required = true, example = "2017-10-25")
	String booking_date;
	@ApiModelProperty(value = "Value date", required = true, example = "2017-10-26")
	String value_date;
	@ApiModelProperty(value = "Remittance information", required = true, example = "Example for Remittance Information")
	String remittance_information_unstructured;
	
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getCreditor_name() {
		return creditor_name;
	}
	public void setCreditor_name(String creditor_name) {
		this.creditor_name = creditor_name;
	}
	public AccountAccount getCreditor_account() {
		return creditor_account;
	}
	public void setCreditor_account(AccountAccount creditor_account) {
		this.creditor_account = creditor_account;
	}
	public String getDebtor_name() {
		return debtor_name;
	}
	public void setDebtor_name(String debtor_name) {
		this.debtor_name = debtor_name;
	}
	public AccountAccount getDebtor_account() {
		return debtor_account;
	}
	public void setDebtor_account(AccountAccount debtor_account) {
		this.debtor_account = debtor_account;
	}
	public AccountAmount getAmount() {
		return amount;
	}
	public void setAmount(AccountAmount amount) {
		this.amount = amount;
	}
	public String getBooking_date() {
		return booking_date;
	}
	public void setBookingDate(String booking_date) {
		this.booking_date = booking_date;
	}
	public String getValue_date() {
		return value_date;
	}
	public void setValue_date(String value_date) {
		this.value_date = value_date;
	}
	public String getRemittance_information_unstructured() {
		return remittance_information_unstructured;
	}
	public void setRemittance_information_unstructured(String remittance_information_unstructured) {
		this.remittance_information_unstructured = remittance_information_unstructured;
	}	

}
