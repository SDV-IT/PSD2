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
package de.sdvrz.tpp.xs2a.confirmation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sdvrz.tpp.authorization.model.AuthorizationModel;
import de.sdvrz.tpp.util.Helper;
import de.sdvrz.tpp.xs2a.Xs2aRestCommunicator;
import de.sdvrz.tpp.xs2a.model.ViewModel;
import de.sdvrz.tpp.xs2a.model.Xs2aModel;
import de.sdvrz.xs2a.api.model.AccountAmount;
import de.sdvrz.xs2a.api.model.ConfirmationOfFundsRequest;
import de.sdvrz.xs2a.api.model.ConfirmationOfFundsResponse;

/**
 * Client for /confirmation-of-funds Request
 *
 */
@SessionScoped
public class ConfirmationClient implements Serializable {
	
	private static final long serialVersionUID = 5669254400258579652L;

	private static final Logger LOG = LoggerFactory.getLogger(ConfirmationClient.class);

	@Inject
	AuthorizationModel authorizationModel;

	@Inject
	Xs2aModel xs2aModel;

	@Inject
	ViewModel viewModel;

	@Inject
	Xs2aRestCommunicator xs2aRestCommunicator;	

	/**
	 * perform /v1/confirmation-of-funds
	 */
	public void confirmationOfFunds() {
		viewModel.setHttpDataFlow(new StringBuilder());
		xs2aModel.setProcessUUID(UUID.randomUUID());

		try {
			Map<String, String> requestPropertyMap = new HashMap<>();
			requestPropertyMap.put("Content-Type", "application/json");
			requestPropertyMap.put("Accept", "application/json");
			requestPropertyMap.put("Process-ID", xs2aModel.getProcessUUID().toString());
			requestPropertyMap.put("Request-ID", UUID.randomUUID().toString());			
			requestPropertyMap.put("Date", Helper.GetHttpHeaderDate());
			
			ConfirmationOfFundsRequest confirmationOfFundsRequest = new ConfirmationOfFundsRequest();
			confirmationOfFundsRequest.setCard_number("342778133618775");
			confirmationOfFundsRequest.setPsu_account("3dc3d5b3-7023-4848-9853-f5400a64e80f");
			confirmationOfFundsRequest.setPayee("Merchant_123");
			confirmationOfFundsRequest.setInstructed_amount(new AccountAmount());
			confirmationOfFundsRequest.getInstructed_amount().setCurrency("EUR");
			confirmationOfFundsRequest.getInstructed_amount().setContent("100.00");

			xs2aRestCommunicator.sendAndReceive("Confirmation Of Funds Request (page 84)", "/v1/confirmation-of-funds/SDV-TPP", "POST", requestPropertyMap, null, confirmationOfFundsRequest, ConfirmationOfFundsResponse.class);

		} catch (Exception e) {
			LOG.error("Error during communication", e);
		}
	}

}
