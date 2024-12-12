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

  Campaign transitionState(Long id, CampaignEvent event, String actor);

  Campaign transitionState(Long id, CampaignEvent event, CampaignParam param, String actor);

  /**
   * Initialize a new campaign: * -> DRAFT
   *
   * @param campaign
   * @return
   */
  Campaign newCampaign(Campaign campaign);

  /**
   * Submit a campaign for FA review: DRAFT -> FA_REVIEW
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> submitForFAReview(Long id, String actor);

  /**
   * FA rejects a campaign: FA_REVIEW -> REJECTED
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> reject(Long id, String actor);

  /**
   * Biz edits a campaign: REJECTED -> DRAFT
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> approve(Long id, String actor);

  /**
   * Biz edits a campaign: REJECTED -> DRAFT
   *
   * @param id
   * @param param
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> edit(
      Long id, CampaignParam param, String actor);

  /**
   * Biz edits a campaign after it has been approved: APPROVED -> FA_REVIEW
   *
   * @param id
   * @param param
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> editAfterApproved(
      Long id, CampaignParam param, String actor);

  /**
   * Terminate a campaign: *ANY -> ENDED
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> terminate(Long id, String actor);

  /**
   * Distribute a campaign: APPROVED -> DISTRIBUTING
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> distribute(Long id, String actor);

  /**
   * Approve a campaign: DISTRIBUTING -> IN_USE_APPROVED
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> approveForUse(Long id, String actor);

  /**
   * Reject a campaign: IN_USE_APPROVED -> IN_USE_REVIEW
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> editBudget(
      Long id, CampaignParam param, String actor);

  /**
   * FA rejects a campaign and returns the previous budget approved: IN_USE_REVIEW -> IN_USE_APPROVED
   *
   * @param id
   * @param actor
   * @return
   */
  StateMachine<CampaignState, CampaignEvent> rejectBudget(Long id, String actor);

    /**
     * FA approves a campaign budget: IN_USE_REVIEW -> IN_USE_APPROVED
     *
     * @param id
     * @param actor
     * @return
     */
  StateMachine<CampaignState, CampaignEvent> approveBudget(Long id, String actor);

}
