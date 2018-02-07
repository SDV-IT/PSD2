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
package de.sdvrz.tpp.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for utility services
 *
 */
public class Helper {

	private static final Logger LOG = LoggerFactory.getLogger(Helper.class);

	/**
	 * makes all certificates trusted
	 */
	public static void trustAllCertificatesCheck() {
		LOG.warn("trustAllCertificatesCheck() It will be trusted to all certificates");
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[0];
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = (hostname, session) -> true;

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("NoSuchAlgorithmException: {}", e);
		} catch (KeyManagementException e) {
			LOG.error("KeyManagementException: {}", e);
		} catch (Exception e) {
			LOG.error("Error", e);
		}
	}

	/**
	 * An actual date/time in HTTP Header format: EEE, dd MMM yyyy HH:mm:ss z
	 * 
	 * @return Date/Time
	 */
	public static String GetHttpHeaderDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * Get a date in the future: now + days
	 * 
	 * @param days Amount of days
	 * @return date in format yyyy-MM-dd
	 */
	public static String getValidUntil(int days) {

		Calendar calendar = Calendar.getInstance();
		calendar.add(java.util.Calendar.DAY_OF_MONTH, days);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(calendar.getTime());
	}

}
