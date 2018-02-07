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
package de.sdvrz.tpp.xs2a.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container for consent data
 *
 */
@ApplicationScoped
public class Xs2aModel {

	private static final Logger LOG = LoggerFactory.getLogger(Xs2aModel.class);

	UUID processUUID;

	String consentId;
	boolean recuringIndicator;
	LocalDate validUntil;
	int frequenyPerDay;
	LocalDate today;
	int counterForFrequentyPerDate;

	@PostConstruct
	void init() {
		consentId = "";
	}

	public UUID getProcessUUID() {
		return processUUID;
	}

	public void setProcessUUID(UUID processUUID) {
		this.processUUID = processUUID;
	}

	public String getConsentId() {
		return consentId;
	}

	void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public boolean isRecuringIndicator() {
		return recuringIndicator;
	}

	void setRecuringIndicator(boolean recuringIndicator) {
		this.recuringIndicator = recuringIndicator;
	}

	public LocalDate getValidUntil() {
		return validUntil;
	}

	void setValidUntil(LocalDate validUntil) {
		this.validUntil = validUntil;
	}

	public int getFrequenyPerDay() {
		return frequenyPerDay;
	}

	public void setFrequenyPerDay(int frequenyPerDay) {
		this.frequenyPerDay = frequenyPerDay;
	}

	/**
	 * Obtain if consent is still valid
	 * 
	 * @return true if consent is valid (no consent transaction needs to be started)
	 */
	public boolean isConsentValid() {
		
		LocalDate currentDate = LocalDate.now();
		if (validUntil == null || validUntil.isBefore(currentDate)) {
			return false;

		}
		if (!recuringIndicator) {
			return false;
		}
		if (today == null || !today.isEqual(currentDate)) {
			today = currentDate;
			counterForFrequentyPerDate = 0;
		}
		return ++counterForFrequentyPerDate <= frequenyPerDay;
	}

	/**
	 * Only the one way to set consent data 
	 * 
	 * @param consentId Consent ID
	 * @param validUntil Valid until
	 * @param recuringIndicator Recuring indicator
	 * @param frequentyPerDay Frequency per day
	 */
	public void setConsentData(String consentId, String validUntil, boolean recuringIndicator, int frequentyPerDay) {
		try {
			this.consentId = consentId;
			this.validUntil = LocalDate.parse(validUntil);
			this.recuringIndicator = recuringIndicator;
			this.frequenyPerDay = frequentyPerDay;
			this.counterForFrequentyPerDate = 1;
			this.today = LocalDate.now();
			
		} catch (Exception e) {
			LOG.error("Error", e);
		}
	}

}
