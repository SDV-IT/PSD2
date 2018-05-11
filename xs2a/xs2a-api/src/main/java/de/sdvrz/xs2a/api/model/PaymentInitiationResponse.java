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
@ApiModel(value = "PaymentInitiationResponse", subTypes = {TppMessage.class})
@JsonInclude(Include.NON_NULL)
public class PaymentInitiationResponse {
	
	@ApiModelProperty(value = "name or names of autheticated methods", required = false)
	List<AuthenticationMethod> sca_methods;
	@ApiModelProperty(value = "name of chosen method", required = false)
	AuthenticationMethod chosen_sca_method;
	@ApiModelProperty(value = "Extended information to chosen_sca_method", required = false)
	AuthenticationChallengeData sca_chalenge_data;
	@ApiModelProperty(value = "Status", required = true)
	String transaction_status;
	@ApiModelProperty(value = "Text to be displayed to the PSU", required = false)
	String psu_message;
	@ApiModelProperty(value = "Messages to TPP", required = false)
	TppMessage tpp_message;
	@ApiModelProperty(value = "Link to (PUT) Update PSU Ddata / (GET) Consent Request", name = "_links", required = true)
	Links _links;	
	
	public List<AuthenticationMethod> getSca_methods() {
		return sca_methods;
	}
	public void setSca_methods(List<AuthenticationMethod> sca_methods) {
		this.sca_methods = sca_methods;
	}	
	public AuthenticationMethod getChosen_sca_method() {
		return chosen_sca_method;
	}
	public void setChosen_sca_method(AuthenticationMethod chosen_sca_method) {
		this.chosen_sca_method = chosen_sca_method;
	}
	public AuthenticationChallengeData getSca_chalenge_data() {
		return sca_chalenge_data;
	}
	public void setSca_chalenge_data(AuthenticationChallengeData sca_chalenge_data) {
		this.sca_chalenge_data = sca_chalenge_data;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}	
	public String getPsu_message() {
		return psu_message;
	}
	public void setPsu_message(String psu_message) {
		this.psu_message = psu_message;
	}	
	public TppMessage getTpp_messages() {
		return tpp_message;
	}
	public void setTpp_messages(TppMessage tpp_message) {
		this.tpp_message = tpp_message;
	}
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}	

}
