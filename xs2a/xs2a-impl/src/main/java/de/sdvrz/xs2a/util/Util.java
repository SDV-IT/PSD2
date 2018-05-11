/*
 * Copyright 2018 SDV-IT, Sparda Datenverarbeitung eG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.sdvrz.xs2a.util;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Utility methods
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class Util {

  /**
   * Check the validation of input parameters
   * 
   * @return allways true
   */
  public boolean checkValidation() {
    
    return true;
  }

  /**
   * Check the authorization
   * 
   * @return allways true
   */
  public boolean checkAuthorization() {
    
    return true;
  }

  /**
   * Check if the Access Token is present
   * 
   * @param accessToken AccessToken: 'Bearer jwnvnwoqhkvn....'
   * @return true if Access Token is present
   */
  public boolean checkAccessToken(String accessToken) {
    
    if (accessToken == null) {
      return false;
    }
    String[] atList = accessToken.split(" ");
    return atList.length == 2 && atList[0].equals("Bearer") && atList[1].length() > 0;
  }

}
