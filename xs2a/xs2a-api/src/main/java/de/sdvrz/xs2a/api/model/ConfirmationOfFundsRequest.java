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
@ApiModel(value = "ConfirmationOfFundsRequest", subTypes = {AccountAmount.class})
public class ConfirmationOfFundsRequest {
	
	@ApiModelProperty(value = "Card Number of the card issued by the PIISP", required = false)
	String card_number;
	@ApiModelProperty(value = "PSU's account number", required = true)
	String psu_account;
	@ApiModelProperty(value = "The merchant where the card is accepted as an information to the PSU", required = false, example = "Merchant123")
	String payee;
	@ApiModelProperty(value = "Transaction amount to be checked within the funds check mechanism", required = true)
	AccountAmount instructed_amount;
	
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getPsu_account() {
		return psu_account;
	}
	public void setPsu_account(String psu_account) {
		this.psu_account = psu_account;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public AccountAmount getInstructed_amount() {
		return instructed_amount;
	}
	public void setInstructed_amount(AccountAmount instructed_amount) {
		this.instructed_amount = instructed_amount;
	}	
	
	
}
