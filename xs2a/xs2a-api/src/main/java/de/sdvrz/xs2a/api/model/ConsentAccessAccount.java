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
@ApiModel(value = "ConsentAccessAccount")
public class ConsentAccessAccount {

	@ApiModelProperty(value = "Iban (either Iban or pan must be sent)", required = true, example = "DE2310010010123456789")
	String iban;
	@ApiModelProperty(value = "Pan (either Iban or pan must be sent)", required = true, example = "1234567890-12345")
	String pan;
	@ApiModelProperty(value = "Access type", required = true, example = "balance")
	List<String> access;
	
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public List<String> getAccess() {
		return access;
	}
	public void setAccess(List<String> access) {
		this.access = access;
	}
	
}
