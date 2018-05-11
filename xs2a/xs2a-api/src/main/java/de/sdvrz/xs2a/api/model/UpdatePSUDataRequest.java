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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 */
@ApiModel(value = "UpdatePSUDataRequest", description = "Update PSU Data Request", subTypes = {PSUData.class})
@JsonInclude(Include.NON_NULL)
public class UpdatePSUDataRequest {

  @ApiModelProperty(value = "authentication method id for Update PSU Data (Authentication Method)", required = false)
  String authentication_method_id;
  @ApiModelProperty(value = "PSU Data for Update PSU Data (Authentication)", required = false)
  PSUData psu_data;
  @ApiModelProperty(value = "sca_authentication_data - for Authorise Request", required = false)
  String sca_authentication_data;

  public String getAuthentication_method_id() {
    return authentication_method_id;
  }
  public void setAuthentication_method_id(String authentication_method_id) {
    this.authentication_method_id = authentication_method_id;
  }
  public PSUData getPsu_data() {
    return psu_data;
  }
  public void setPsu_data(PSUData psu_data) {
    this.psu_data = psu_data;
  }
  public String getSca_authentication_data() {
    return sca_authentication_data;
  }
  public void setSca_authentication_data(String sca_authentication_data) {
    this.sca_authentication_data = sca_authentication_data;
  }

}
