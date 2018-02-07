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
@ApiModel(value = "PaymentInitiationRequest", subTypes = {AccountAmount.class, AccountAccount.class, AccountName.class})
public class PaymentInitiationRequest {
	
	@ApiModelProperty(value = "currency and amount", required = true)
	AccountAmount instructed_amount;
	@ApiModelProperty(value = "Debtor account", required = true)
	AccountAccount debtor_account;
	@ApiModelProperty(value = "Creditor name", required = true, example = "Merchant123")
	AccountName creditor;
	@ApiModelProperty(value = "Creditor account", required = true)
	AccountAccount creditor_account;
	@ApiModelProperty(value = "\"true\": a payment initiation service will be address in the same \"session\"", required = false, example = "false")
	String remittance_information_structured;
	
	public AccountAmount getInstructed_amount() {
		return instructed_amount;
	}
	public void setInstructed_amount(AccountAmount instructed_amount) {
		this.instructed_amount = instructed_amount;
	}
	public AccountAccount getDebtor_account() {
		return debtor_account;
	}
	public void setDebtor_account(AccountAccount debtor_account) {
		this.debtor_account = debtor_account;
	}
	public AccountName getCreditor() {
		return creditor;
	}
	public void setCreditor(AccountName creditor) {
		this.creditor = creditor;
	}
	public AccountAccount getCreditor_account() {
		return creditor_account;
	}
	public void setCreditor_account(AccountAccount creditor_account) {
		this.creditor_account = creditor_account;
	}
	public String getRemittance_information_structured() {
		return remittance_information_structured;
	}
	public void setRemittance_information_structured(String remittance_information_structured) {
		this.remittance_information_structured = remittance_information_structured;
	}	

}
