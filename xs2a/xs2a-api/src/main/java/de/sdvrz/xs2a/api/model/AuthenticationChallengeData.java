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
 * Authentication Object (to clarify)
 *
 */
@ApiModel(value = "AuthenticationChallengeData")
@JsonInclude(Include.NON_NULL)
public class AuthenticationChallengeData {
	
	@ApiModelProperty(value = "PNG data to be displayed to the PSU", required = false)
	String image;
	@ApiModelProperty(value = "The maximal length for the OTP to be typed in by PSU", required = false, example = "6")
	String otp_max_length;
	@ApiModelProperty(value = "The format type of the OTP to be typed in", required = false, example = "integer")
	String otp_format;
	@ApiModelProperty(value = "Additional explanation for the PSU", required = false)
	String additional_information;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}	
	public String getOtp_max_length() {
		return otp_max_length;
	}
	public void setOtp_max_length(String otp_max_length) {
		this.otp_max_length = otp_max_length;
	}
	public String getOtp_format() {
		return otp_format;
	}
	public void setOtp_format(String otp_format) {
		this.otp_format = otp_format;
	}
	public String getAdditional_information() {
		return additional_information;
	}
	public void setAdditional_information(String additional_information) {
		this.additional_information = additional_information;
	}
	
}
