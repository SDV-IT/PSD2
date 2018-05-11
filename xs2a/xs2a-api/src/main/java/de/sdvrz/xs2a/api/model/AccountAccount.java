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
@ApiModel(value = "AccountAccount")
public class AccountAccount {

	@ApiModelProperty(value = "IBAN", required = false, example = "DE2310010010123456789")
	private String iban;
	@ApiModelProperty(value = "id for payment account without IBAN", required = false)
	private String bban;
	@ApiModelProperty(value = "Primary Account Number of a card", required = false, example = "1234567890-12345")
	private String pan;
	@ApiModelProperty(value = "An alias to access a payment account via a registered mobile phone number", required = false)
	private String msisdn;
	
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
}
