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
package de.sdvrz.xs2a.impl;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sdvrz.xs2a.api.Confirmation;
import de.sdvrz.xs2a.api.model.ConfirmationOfFundsRequest;
import de.sdvrz.xs2a.api.model.ConfirmationOfFundsResponse;
import de.sdvrz.xs2a.api.model.TppMessage;
import de.sdvrz.xs2a.util.Util;

/**
 * Implement class for Confirmation Interface
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class ConfirmationImpl implements Confirmation {
	
	@Inject
	Util util;

	@Override
	public Response confirmationOfFunds(String version, String providerId, String processId, String requestId, 
			String signature, String certificate, String date, ConfirmationOfFundsRequest confirmationOfFundsRequest) {
		
		if (!util.checkValidation()) {
			// HTTP Code: 400
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (!util.checkAuthorization()) {
			// HTTP Code: 401
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		ConfirmationOfFundsResponse confirmationOfFundsResponse = new ConfirmationOfFundsResponse();
		confirmationOfFundsResponse.setTpp_message(new TppMessage());
		confirmationOfFundsResponse.getTpp_message().setCategory("ACCEPTED");
		confirmationOfFundsResponse.getTpp_message().setCode("1234567890");
		
		return Response.status(Status.CREATED).entity(confirmationOfFundsResponse).build();
	}

}
