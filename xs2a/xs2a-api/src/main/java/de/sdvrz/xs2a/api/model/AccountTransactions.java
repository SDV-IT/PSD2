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
@ApiModel(value = "AccountTransactions", subTypes = {AccountTransaction.class, Links.class})
public class AccountTransactions {

	@ApiModelProperty(value = "transactions consider closed bookings", required = true)
	List<AccountTransaction> booked;
	@ApiModelProperty(value = "transactions consider planned bookings", required = false)
	List<AccountTransaction> pending;
	@ApiModelProperty(value = "account_link / first_page_link / ... / last_page_link", name = "_links", required = true)
	Links _links;
	
	public List<AccountTransaction> getBooked() {
		return booked;
	}
	public void setBooked(List<AccountTransaction> booked) {
		this.booked = booked;
	}
	public List<AccountTransaction> getPending() {
		return pending;
	}
	public void setPending(List<AccountTransaction> pending) {
		this.pending = pending;
	}
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}		
	
}
