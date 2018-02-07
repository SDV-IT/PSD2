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

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * Model to interact with home.xhtml
 * Contains only data reporting Http data flow for last request 
 *
 */
@Named
@SessionScoped
public class ViewModel implements Serializable {	
	
	private static final long serialVersionUID = -1221587915160384321L;
	
	private StringBuilder httpDataFlow;
	private String lastResponse;
	
	public StringBuilder getHttpDataFlow() {
		return httpDataFlow;
	}
	public void setHttpDataFlow(StringBuilder httpDataFlow) {
		this.httpDataFlow = httpDataFlow;
	}
	public String getLastResponse() {
		return lastResponse;
	}
	public void setLastResponse(String lastResponse) {
		this.lastResponse = lastResponse;
	}
	
	
	
	
}
