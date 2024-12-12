package com.statemachinedemo.springstatemachinedemo.controller;

import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.service.CampaignService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@RestController
@RequestMapping("/api/campaigns")
@Tag(name = "Campaign API", description = "Manage Campaigns and their states")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping("/{id}")
    public Campaign getCampaign(@PathVariable Long id) {
        return campaignService.getCampaign(id);
    }

    @PostMapping("/{id}/events/{event}")
    public Campaign transitionCampaign(@PathVariable Long id, @PathVariable CampaignEvent event, @RequestParam String changedBy) {
        return campaignService.transitionState(id, event, changedBy);
    }
}
