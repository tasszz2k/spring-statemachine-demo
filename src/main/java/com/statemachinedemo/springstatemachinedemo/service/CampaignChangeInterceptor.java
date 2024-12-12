package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import com.statemachinedemo.springstatemachinedemo.repository.CampaignRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

/**
 * @author anhdt9
 * @since 12/12/24
 */
@RequiredArgsConstructor
@Component
public class CampaignChangeInterceptor
    extends StateMachineInterceptorAdapter<CampaignState, CampaignEvent> {

  private final CampaignRepository campaignRepository;

  @Override
  public void postStateChange(
      State<CampaignState, CampaignEvent> state,
      Message<CampaignEvent> message,
      Transition<CampaignState, CampaignEvent> transition,
      StateMachine<CampaignState, CampaignEvent> stateMachine,
      StateMachine<CampaignState, CampaignEvent> rootStateMachine) {
    Optional.ofNullable(message)
        .flatMap(
            msg ->
                Optional.ofNullable(
                    (Long) msg.getHeaders().getOrDefault(CampaignServiceImpl.CAMPAIGN_ID, -1L)))
        .ifPresent(
            campaignId -> {
              CampaignState stateId = state.getId();
              campaignRepository
                  .findById(campaignId)
                  .ifPresent(
                      campaign -> {
                        // update to the next state
                        campaign.setCurrentState(stateId);
                        campaignRepository.save(campaign);
                      });
            });
  }
}
