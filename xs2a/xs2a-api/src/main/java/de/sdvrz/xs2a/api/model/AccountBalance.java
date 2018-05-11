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
@ApiModel(value = "AccountBalance", subTypes = {AccountBalanceInf.class})
public class AccountBalance {
	
	@ApiModelProperty(value = "Last known book balance of the account", required = false)
	AccountBalanceInf booked;
	@ApiModelProperty(value = "Balanced composed of booked entries and pending items", required = false)
	AccountBalanceInf expected;
	@ApiModelProperty(value = "The expected balance together with the value of a pre-approved permanently available credit line", required = false)
	AccountBalanceInf authorised;
	@ApiModelProperty(value = "Balance at the beginning of the reporting period", required = false)
	AccountBalanceInf opening_booked;
	@ApiModelProperty(value = "Balance at the end of the pre-agreed reporting period", required = false)
	AccountBalanceInf closing_booked;
	@ApiModelProperty(value = "Calculated on the basis of booked credit and debit items", required = false)
	AccountBalanceInf interim_available;
	
	public AccountBalanceInf getBooked() {
		return booked;
	}
	public void setBooked(AccountBalanceInf booked) {
		this.booked = booked;
	}
	public AccountBalanceInf getExpected() {
		return expected;
	}
	public void setExpected(AccountBalanceInf expected) {
		this.expected = expected;
	}
	public AccountBalanceInf getAuthorised() {
		return authorised;
	}
	public void setAuthorised(AccountBalanceInf authorised) {
		this.authorised = authorised;
	}
	public AccountBalanceInf getOpening_booked() {
		return opening_booked;
	}
	public void setOpening_booked(AccountBalanceInf opening_booked) {
		this.opening_booked = opening_booked;
	}
	public AccountBalanceInf getClosing_booked() {
		return closing_booked;
	}
	public void setClosing_booked(AccountBalanceInf closing_booked) {
		this.closing_booked = closing_booked;
	}
	public AccountBalanceInf getInterim_available() {
		return interim_available;
	}
	public void setInterim_available(AccountBalanceInf interim_available) {
		this.interim_available = interim_available;
	}
}
