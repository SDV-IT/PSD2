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
 * Authentication Object
 *
 */
@ApiModel(value = "AuthenticationMethod")
@JsonInclude(Include.NON_NULL)
public class AuthenticationMethod {
	
	@ApiModelProperty(value = "Authentication Type", required = true, example = "SMS_OTP")
	String authentication_type;
	@ApiModelProperty(value = "Authentication Method Id", required = true, example = "myAuthenticationID")
	String authentication_method_id;
	@ApiModelProperty(value = "Name of the Authentication Method", required = false)
	String name;
	@ApiModelProperty(value = "Detailed information about Authentication Method", required = false)
	String explanation;

	public String getAuthentication_type() {
		return authentication_type;
	}
	public void setAuthentication_type(String authentication_type) {
		this.authentication_type = authentication_type;
	}
	public String getAuthentication_method_id() {
		return authentication_method_id;
	}
	public void setAuthentication_method_id(String authentication_method_id) {
		this.authentication_method_id = authentication_method_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	

}
