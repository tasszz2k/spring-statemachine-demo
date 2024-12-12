package com.statemachinedemo.springstatemachinedemo.controller;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.dto.CampaignParam;
import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import com.statemachinedemo.springstatemachinedemo.service.CampaignServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@RestController
@RequestMapping("/api/campaigns")
@Tag(name = "Campaign API", description = "Manage Campaigns and their states")
@RequiredArgsConstructor
public class CampaignController {

  private final CampaignServiceImpl campaignService;

  @GetMapping("/{id}")
  public Campaign getCampaign(@PathVariable Long id) {
    return campaignService.findById(id);
  }

  @PostMapping("")
  public Campaign createCampaign(@RequestBody Campaign campaign) {
    return campaignService.newCampaign(campaign);
  }

  @PostMapping("/{id}/events/{event}")
  public Campaign transitionCampaign(
      @PathVariable Long id,
      @PathVariable CampaignEvent event,
      @RequestBody CampaignParam params,
      @RequestParam String actor) {
    return campaignService.transitionState(id, event, params, actor);
  }
}
