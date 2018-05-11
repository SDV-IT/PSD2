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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 */
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "InformationConsentRequest", subTypes = {ConsentAccessAccount.class})
public class InformationConsentRequest {
	
	@ApiModelProperty(value = "List of accounts to be accessed", required = true)
	List<ConsentAccessAccount> access_accounts;
	@ApiModelProperty(value = "\"true\" if the consent is for requring access", required = true, example = "true")
	boolean recurring_indicator;
	@ApiModelProperty(value = "Valid until date for the requested consent", required = true, example = "2017-10-30")
	String valid_until;
	@ApiModelProperty(value = "Maximum frequency for an access per day", required = true, example = "4")
	int frequency_per_day;
	@ApiModelProperty(value = "\"true\": a payment initiation service will be address in the same \"session\"", required = true, example = "false")
	boolean combined_service_indicator;
	
	public List<ConsentAccessAccount> getAccess_accounts() {
		return access_accounts;
	}
	public void setAccess_accounts(List<ConsentAccessAccount> access_accounts) {
		this.access_accounts = access_accounts;
	}
	public boolean isRecurring_indicator() {
		return recurring_indicator;
	}
	public void setRecurring_indicator(boolean recurring_indicator) {
		this.recurring_indicator = recurring_indicator;
	}
	public String getValid_until() {
		return valid_until;
	}
	public void setValid_until(String valid_until) {
		this.valid_until = valid_until;
	}
	public int getFrequency_per_day() {
		return frequency_per_day;
	}
	public void setFrequency_per_day(int frequency_per_day) {
		this.frequency_per_day = frequency_per_day;
	}
	public boolean getCombined_service_indicator() {
		return combined_service_indicator;
	}
	public void setCombined_service_indicator(boolean combined_service_indicator) {
		this.combined_service_indicator = combined_service_indicator;
	}
	

}
