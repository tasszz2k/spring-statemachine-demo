package com.statemachinedemo.springstatemachinedemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

/**
 * @author anhdt9
 * @since 12/12/24
 */
// @Value
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignParam {
  private String campaignName;
  private String mktName;
  private Double budget;

  public String toJson() {
    // Serialize to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  public static CampaignParam fromJson(String json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, CampaignParam.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
