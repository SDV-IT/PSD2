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
package de.sdvrz.xs2a.api.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 */
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "Links")
public class Links {

  @ApiModelProperty(value = "Link to self", required = false)
  private String self;
  @ApiModelProperty(value = "In case of an SCA Redirect Approach - the redirect link", required = false)
  private String redirect;
  @ApiModelProperty(value = "Link to Update PSU Identification", required = false)
  private String update_psu_identification;
  @ApiModelProperty(value = "Link to Update PSU Authetication (mandatory if SCA approach is 'Embedded')", required = false)
  private String update_psu_authentication;
  @ApiModelProperty(value = "Link to resource where TPP can select authentication method", required = false)
  private String select_authentication_method;
  @ApiModelProperty(value = "Link to resource where TPP can send 'Transaction Authorisation Request'", required = false)
  private String authorise_transaction;
  @ApiModelProperty(value = "Link to an accounts/.../transactions", required = false)
  private String account_link;
  @ApiModelProperty(value = "Link to balances of account is to return", required = false,
      example = "/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/balances")
  private String balances;
  @ApiModelProperty(value = "Link to transactions of account is to return", required = false,
      example = "/v1/accounts/3dc3d5b3-7023-4848-9853-f5400a64e80f/transactions")
  private String transaction;
  @ApiModelProperty(value = "Link to Status", required = false)
  private String status;
  @ApiModelProperty(value = "Navigation link for accounts reports", required = false)
  private String first_page_link;
  @ApiModelProperty(value = "Navigation link for accounts reports", required = false)
  private String second_page_link;
  @ApiModelProperty(value = "Navigation link for accounts reports", required = false)
  private String current_page_link;
  @ApiModelProperty(value = "Navigation link for accounts reports", required = false)
  private String last_page_link;

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public String getRedirect() {
    return redirect;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  public String getUpdate_psu_identification() {
    return update_psu_identification;
  }

  public void setUpdate_psu_identification(String update_psu_identification) {
    this.update_psu_identification = update_psu_identification;
  }

  public String getUpdate_psu_authentication() {
    return update_psu_authentication;
  }

  public void setUpdate_psu_authentication(String update_psu_authentication) {
    this.update_psu_authentication = update_psu_authentication;
  }

  public String getSelect_authentication_method() {
    return select_authentication_method;
  }

  public void setSelect_authentication_method(String select_authentication_method) {
    this.select_authentication_method = select_authentication_method;
  }

  public String getAuthorise_transaction() {
    return authorise_transaction;
  }

  public void setAuthorise_transaction(String authorise_transaction) {
    this.authorise_transaction = authorise_transaction;
  }

  public String getAccount_link() {
    return account_link;
  }

  public void setAccount_link(String account_link) {
    this.account_link = account_link;
  }

  public String getBalances() {
    return balances;
  }

  public void setBalances(String balances) {
    this.balances = balances;
  }

  public String getTransaction() {
    return transaction;
  }

  public void setTransaction(String transaction) {
    this.transaction = transaction;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFirst_page_link() {
    return first_page_link;
  }

  public void setFirst_page_link(String first_page_link) {
    this.first_page_link = first_page_link;
  }

  public String getSecond_page_link() {
    return second_page_link;
  }

  public void setSecond_page_link(String second_page_link) {
    this.second_page_link = second_page_link;
  }

  public String getCurrent_page_link() {
    return current_page_link;
  }

  public void setCurrent_page_link(String current_page_link) {
    this.current_page_link = current_page_link;
  }

  public String getLast_page_link() {
    return last_page_link;
  }

  public void setLast_page_link(String last_page_link) {
    this.last_page_link = last_page_link;
  }

  @Override
  public String toString() {
    
    Map<String, String> map = new HashMap<>();
    Method[] methodArray = this.getClass().getDeclaredMethods();
    for (java.lang.reflect.Method method : methodArray) {  
      if (!method.getName().startsWith("get")) {
        continue;
      }
      String feldName = method.getName().replaceAll("get", "");
      feldName = feldName.substring(0, 1).toLowerCase() + feldName.substring(1);
      try {
        map.put(feldName, (String)method.invoke(this));
      } catch (IllegalAccessException|InvocationTargetException e) {        
      }
    }

    return map.toString();
  }



}
