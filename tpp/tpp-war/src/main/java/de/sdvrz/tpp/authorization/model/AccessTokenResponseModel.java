/*
  Copyright 2018 SDV-IT, Sparda Datenverarbeitung eG

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package de.sdvrz.tpp.authorization.model;

import java.time.LocalDateTime;

/**
 * To hold all data from JSON response from Authorization Server
 *
 */
public class AccessTokenResponseModel {
	
	private String access_token;
	private String refresh_token;
	private String scope;
	private String token_type;
	private String expires_in;
	private LocalDateTime localDateTime;
	
	
	public AccessTokenResponseModel() {
		access_token = "";
		refresh_token = "";
		scope = "";
		token_type = "";
		expires_in = "";
		localDateTime = LocalDateTime.now();
	}
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	@Override
	public String toString() {
		return "AccessTokenResponseModel{" +
				"access_token='" + access_token + '\'' +
				", refresh_token='" + refresh_token + '\'' +
				", scope='" + scope + '\'' +
				", token_type='" + token_type + '\'' +
				", expires_in='" + expires_in + '\'' +
				", localDateTime=" + localDateTime +
				'}';
	}
}
