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

import io.swagger.annotations.ApiModel;

/**
 *
 */
@ApiModel(value = "AccountBalanceResponse", description = "Response for /{version}/accounts/{account-id}/balances request", subTypes = {AccountBalance.class})
public class AccountBalanceResponse {

	List<AccountBalance> balances;

	public List<AccountBalance> getBalances() {
		return balances;
	}

	public void setBalances(List<AccountBalance> balances) {
		this.balances = balances;
	}	
}
