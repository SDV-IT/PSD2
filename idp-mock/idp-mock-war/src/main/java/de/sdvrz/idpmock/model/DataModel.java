/**
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
package de.sdvrz.idpmock.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 * Container for ClientModel for all clientId requested to IdP - Mock
 *
 */
@ApplicationScoped
public class DataModel {
		
	private Map<String, Map<String, ClientModel>> scopeClientMap;
	
	@PostConstruct
	void init() {
		scopeClientMap = new HashMap<>();
	}

	public Map<String, Map<String, ClientModel>> getScopeClientMap() {
		return scopeClientMap;
	}

	public void setScopeClientMap(Map<String, Map<String, ClientModel>> scopeClientMap) {
		this.scopeClientMap = scopeClientMap;
	}	
	

}
