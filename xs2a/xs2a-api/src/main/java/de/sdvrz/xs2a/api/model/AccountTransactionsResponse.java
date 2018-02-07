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

import io.swagger.annotations.ApiModel;

/**
 *
 */
@ApiModel(value = "AccountTransactionsResponse", description = "Response for /{version}/accounts/{account-id}/transactions request", subTypes = {AccountTransactions.class})
public class AccountTransactionsResponse {
	
	AccountTransactions transactions;

	public AccountTransactions getTransactions() {
		return transactions;
	}

	public void setTransactions(AccountTransactions transactions) {
		this.transactions = transactions;
	}	

}
