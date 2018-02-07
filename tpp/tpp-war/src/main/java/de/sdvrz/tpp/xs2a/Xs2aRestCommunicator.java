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
package de.sdvrz.tpp.xs2a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.sdvrz.tpp.PropertyManager;
import de.sdvrz.tpp.xs2a.model.ViewModel;

/**
 * Class to perform HTTP communication to XS2A server
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class Xs2aRestCommunicator {

	private static final Logger LOG = LoggerFactory.getLogger(Xs2aRestCommunicator.class);

	public static final String HTTP_CODE_KEY = "httpCode";
	public static final String OUTPUT_KEY = "output";

	@Inject
	PropertyManager propertyManager;

	@Inject
	ViewModel viewModel;
	
	static private String[] headerParamToLog = new String[] {"PSU-ID", "Consent-ID", "Authorization"};

	/**
	 * HTTP send / receive 
	 * 
	 * @param requestTitle title of the request
	 * @param requestPath path to be requested e.g. /v1/accounts
	 * @param httpMethod GET / POST PUT
	 * @param requestPropertyMap List (key/value) of Header parameter
	 * @param queryStringMap List (key/value) of Query parameter
	 * @param request (for POST/PUT) body element
	 * @param responseClazz Class the response to be convert to
	 * @param <T> Response Class
	 * @return Map, 1. pair: HTTP response code, 2. pair: class with response body data (only if HTTP code = 200/201)
	 * @throws IOException IOException can be thrown
	 */
	public <T> Map<String, Object> sendAndReceive(String requestTitle, String requestPath, String httpMethod,
			Map<String, String> requestPropertyMap, Map<String, String> queryStringMap, Object request, Class<T> responseClazz) throws IOException {

		Map<String, Object> responseMap = new HashMap<>();

		// Build url for request (with query parameter if any)
		StringBuilder urlstr = new StringBuilder().append(getUrl()).append(requestPath);
		if (queryStringMap != null) {
			char ch = '?';
			for (String key : queryStringMap.keySet()) {
				urlstr.append(ch).append(key).append('=').append(queryStringMap.get(key));
				ch = '&';
			}
		}	
		LOG.debug("sendAndReceive() url: {}", urlstr.toString());	
		
		// Build Header property for the request
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(urlstr.toString()).openConnection();
		httpURLConnection.setRequestMethod(httpMethod);
		for (String name : requestPropertyMap.keySet()) {
			httpURLConnection.setRequestProperty(name, requestPropertyMap.get(name));
		}
		httpURLConnection.setDoInput(true);
		if (httpMethod.equals("POST")
				|| httpMethod.equals("PUT")) {
			httpURLConnection.setDoOutput(true);

			// convert java class to JSON
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(request);
			OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
			wr.write(jsonData);
			wr.flush();
		}
		logRequest(requestTitle, httpURLConnection, requestPropertyMap, request);

		int httpResponseCode = httpURLConnection.getResponseCode();
		responseMap.put(HTTP_CODE_KEY, httpResponseCode);
		StringBuilder sb = new StringBuilder();
		if (httpResponseCode == HttpURLConnection.HTTP_OK
				|| httpResponseCode == HttpURLConnection.HTTP_CREATED) {
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			LOG.debug("sendAndReceive() HTTP ResponseCode: {}, Data: {}", httpResponseCode, sb.toString());

			// Only for formatted output
			ObjectMapper mapper = new ObjectMapper();
			T response = mapper.readValue(sb.toString(), responseClazz);
			responseMap.put(OUTPUT_KEY, response);
			logResponse(httpResponseCode, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		} else {
			if (httpURLConnection.getErrorStream() != null) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getErrorStream(), "UTF-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
			}
			LOG.debug("sendAndReceive() HTTP ResponseCode: {}/{}, Message: {}", httpResponseCode,
					httpURLConnection.getResponseMessage(), sb.toString());
			logResponse(httpResponseCode, sb.toString());

		}
		return responseMap;
	}

	/**
	 * Build Url for Request
	 * 
	 * @return Url for Request
	 */
	private String getUrl() {
		return new StringBuilder().append(propertyManager.getProperty("xs2a.server.schema")).append("://")
				.append(propertyManager.getProperty("xs2a.server.address")).append(':')
				.append(propertyManager.getProperty("xs2a.server.port"))
				.append(propertyManager.getProperty("xs2a.server.path")).toString();
	}	
	
	/**
	 * Display request on the view
	 * 
	 * @param requestTitle of the request
	 * @param httpURLConnection Request Url
	 * @param requestPropertyMap Map with header param
	 * @param request Body class
	 * @throws JsonProcessingException request can not be convert to JSON 
	 */
	private void logRequest(String requestTitle, HttpURLConnection httpURLConnection, Map<String, String> requestPropertyMap, Object request) throws JsonProcessingException {
		viewModel.getHttpDataFlow().append("--- ").append(requestTitle).append(' ').append("-----------------------------------------------------------------------------------------------".substring(requestTitle.length() > 95 ? 95 : requestTitle.length())).append('\n');
		viewModel.getHttpDataFlow().append(httpURLConnection.getRequestMethod()).append(' ')
				.append(httpURLConnection.getURL()).append('\n');	
		// for important header HTTP param
		String pre = " header: {...\n          ", after = "";		
		StringBuilder sbHeader = new StringBuilder(); 
		for (String headerParam : headerParamToLog) {
			if (requestPropertyMap.containsKey(headerParam)) {
				viewModel.getHttpDataFlow().append(pre).append(headerParam).append(": ").append(
				                    requestPropertyMap.get(headerParam) == null || requestPropertyMap.get(headerParam).length() < 60 ? requestPropertyMap.get(headerParam) : requestPropertyMap.get(headerParam).substring(0, 57) + "...");
				pre = "\n          ";
				after = "\n          ...}\n";
			}
		}
		sbHeader.append(after);
		viewModel.getHttpDataFlow().append(sbHeader);
		if (httpURLConnection.getRequestMethod().equals("POST")
				|| httpURLConnection.getRequestMethod().equals("PUT")) {
			
			ObjectMapper mapper = new ObjectMapper();
			viewModel.getHttpDataFlow().append(" body: ").append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request)).append('\n');			
		}
	}

	/**
	 * Display response on the view
	 * 
	 * @param httpCode
	 * @param message
	 */
	private void logResponse(int httpCode, String message) {
		viewModel.getHttpDataFlow()
				.append(httpCode).append("\n body: ").append(message)
				.append('\n').append('\n');
		viewModel.setLastResponse(message);
	}

}
