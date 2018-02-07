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
package de.sdvrz.tpp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To obtain system property. If no system property were found then property from config.properties file will be obtained
 *
 */
@ApplicationScoped
public class PropertyManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(PropertyManager.class);
	
	Properties properties;
	
	/**
	 * reads config.properties file and set read properties to memory
	 */
	@PostConstruct
	void init() {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			LOG.error("IOException: {}", e.getMessage());
		}		
	}
	
	/**
	 * @param key the value to be obtained for
	 * @return the value from system property or from config.properties file
	 */
	public String getProperty(String key) {
		
		String value = System.getProperty(key);
		return value == null ? properties.getProperty(key) : value;
	}

}
