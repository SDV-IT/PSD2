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
@ApiModel(value = "Account", description = "Account details", subTypes = {Links.class})
public class Account {
	
	@ApiModelProperty(value = "Id of account", required = true, example = "3dc3d5b3-7023-4848-9853-f5400a64e80f")
	private String id;
	@ApiModelProperty(value = "IBAN", required = false, example = "DE2310010010123456789")
	private String iban;
	@ApiModelProperty(value = "id for payment account without IBAN", required = false)
	private String bban;
	@ApiModelProperty(value = "Primary Account Number of a card", required = false, example = "1234567890-12345")
	private String pan;
	@ApiModelProperty(value = "An alias to access a payment account via a registered mobile phone number", required = false)
	private String msisdn;
	@ApiModelProperty(value = "Name given by the bank or the PSU in online banking", required = false)
	private String name;
	@ApiModelProperty(value = "Type of account", required = false, example = "US dollar account")
	private String account_type;
	@ApiModelProperty(value = "The BIC associated to the account", required = false)
	private String bic;
	@ApiModelProperty(value = "Balances", required = false)
	private String balances;
	@ApiModelProperty(value = "account currency", required = true, example = "EUR")
	private String currency;
	@ApiModelProperty(value = "Links to balances or transactions", required = false, name = "_links", example = "/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e81g/balances")
	private Links _links;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public String getBban() {
		return bban;
	}
	
	public void setBban(String bban) {
		this.bban = bban;
	}
	
	public String getPan() {
		return pan;
	}
	
	public void setPan(String pan) {
		this.pan = pan;
	}
	
	public String getMsisdn() {
		return msisdn;
	}
	
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}	

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}	

	public String getBalances() {
		return balances;
	}

	public void setBalances(String balances) {
		this.balances = balances;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Links get_links() {
		return _links;
	}

	public void set_links(Links _links) {
		this._links = _links;
	}
	
}
