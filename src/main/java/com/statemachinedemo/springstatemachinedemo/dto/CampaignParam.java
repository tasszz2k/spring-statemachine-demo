package com.statemachinedemo.springstatemachinedemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

/**
 * @author anhdt9
 * @since 12/12/24
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignParam {
  String campaignName;
  String mktName;
  Double budget;
}
