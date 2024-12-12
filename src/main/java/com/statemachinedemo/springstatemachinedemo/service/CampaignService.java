package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import com.statemachinedemo.springstatemachinedemo.dto.CampaignParam;
import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import org.springframework.statemachine.StateMachine;

/**
 * @author anhdt9
 * @since 12/12/24
 */
public interface CampaignService {
  Campaign findById(Long id);

  Campaign transitionState(Long id, CampaignEvent event, CampaignParam param, String actor);

  /**
   * Initialize a new campaign: * -> DRAFT
   *
   * @param campaign
   * @param actor
   * @return
   */
  Campaign newCampaign(Campaign campaign, String actor);
}
