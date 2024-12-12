package com.statemachinedemo.springstatemachinedemo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author anhdt9
 * @since 13/12/24
 */
class CampaignParamTest {

  @Test
  void fromJson() {
    var json = "{\"budget\": 200000.0, \"campaignName\": \"[code 100k] xxx\"}";
    var campaignParam = CampaignParam.fromJson(json);
    assertNotNull(campaignParam);
    assertEquals("200000.0", campaignParam.getBudget().toString());
    assertEquals("[code 100k] xxx", campaignParam.getCampaignName());
  }

  @Test
  void toJson() {
    var campaignParam = CampaignParam.builder().budget(200000.0).campaignName("[code 100k] xxx").build();
    var json = campaignParam.toJson();
    assertNotNull(json);
    assertTrue(json.contains("\"budget\":200000.0"));
    assertTrue(json.contains("\"campaignName\":\"[code 100k] xxx\""));
  }
}
