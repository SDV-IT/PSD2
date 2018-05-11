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
@ApiModel(value = "AccountBalanceInf", subTypes = {AccountAmount.class})
@JsonInclude(Include.NON_NULL)
public class AccountBalanceInf {
	
	@ApiModelProperty(value = "Amount and currency for balance for", required = true)
	AccountAmount amount;
	@ApiModelProperty(value = "", required = false)
	String last_action_date_time;	
	@ApiModelProperty(value = "Date for last actualization", required = false, example = "2017-11-01")
	String date;	
	
	public AccountAmount getAmount() {
		return amount;
	}
	public void setAmount(AccountAmount amount) {
		this.amount = amount;
	}	
	public String getLast_action_date_time() {
		return last_action_date_time;
	}
	public void setLast_action_date_time(String last_action_date_time) {
		this.last_action_date_time = last_action_date_time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}	

}
