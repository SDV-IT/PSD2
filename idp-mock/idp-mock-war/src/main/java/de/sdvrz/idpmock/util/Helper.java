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
package de.sdvrz.idpmock.util;

/**
 * Class for utility services
 *
 */
public class Helper {

	private static String hexaZeichen = "abcdef1234567890";
	
	private static int[] codeMask = new int[] {8, 4, 4, 4, 12};
	
	/**
	 * @return authorization code or refresh code
	 */
	public static String generateCode() {		
		
		java.util.Random random = new java.util.Random();
		StringBuilder result = new StringBuilder();
		String trenn = "";
		for (int i : codeMask) {
			result.append(trenn);
			for(int j=i; j>0; j--) {
				result.append(hexaZeichen.charAt(random.nextInt() & (hexaZeichen.length()-1) ));
			}
			trenn = "-";
		}
		return result.toString();
	}	
	
	private static String[] expiresInTab = new String[]{"3600", "3600", "3600", "2100", "1800", "960"};
	
	/**
	 * @return Expires In
	 */
	public static String generateExpiresIn() {
		
		java.util.Random random = new java.util.Random();
		return expiresInTab[random.nextInt() & (expiresInTab.length-1)];
	}
	
	private static String zeichen = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890._";
	
	
	/**
	 * @return Access Token
	 */
	public static String generateAccessToken() {
		
		java.util.Random random = new java.util.Random();
		StringBuilder result = new StringBuilder();		
		for(int i=0; i<500; i++) {
			result.append(zeichen.charAt(random.nextInt() & (zeichen.length()-1) ));
		}			
		return result.toString();
	}

}
