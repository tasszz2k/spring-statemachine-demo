package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import com.statemachinedemo.springstatemachinedemo.model.ActionLog;
import com.statemachinedemo.springstatemachinedemo.repository.CampaignRepository;
import com.statemachinedemo.springstatemachinedemo.repository.ActionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Service
@RequiredArgsConstructor
public class CampaignService {

  private final CampaignRepository campaignRepository;
  private final ActionLogRepository actionLogRepository;
  private final StateMachineFactory<CampaignState, CampaignEvent> stateMachineFactory;

  public Campaign getCampaign(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Campaign not found"));
  }

  public Campaign transitionState(Long campaignId, CampaignEvent event, String changedBy) {
    Campaign campaign = getCampaign(campaignId);

    // Initialize and configure the state machine with the current state
    StateMachine<CampaignState, CampaignEvent> stateMachine =
        stateMachineFactory.getStateMachine(campaignId.toString());
    stateMachine
        .getStateMachineAccessor()
        .doWithAllRegions(
            accessor ->
                accessor.resetStateMachine(
                    new DefaultStateMachineContext<>(
                        campaign.getCurrentState(), null, null, null)));

    // Start the state machine reactively
    stateMachine.startReactively().block();

    // Send the event reactively
    stateMachine
        .sendEvent(Mono.just(MessageBuilder.withPayload(event).build()))
        .doOnNext(
            result -> {
              if (!result.getResultType().equals(StateMachineEventResult.ResultType.ACCEPTED)) {
                throw new IllegalStateException("Invalid state transition");
              }
            })
        .blockLast();

    // Update campaign state and log the transition
    CampaignState previousState = campaign.getCurrentState();
    CampaignState nextState = stateMachine.getState().getId();

    campaign.setCurrentState(nextState);
    campaignRepository.save(campaign);

    ActionLog log = new ActionLog();
    log.setCampaign(campaign);
    log.setPreviousState(previousState);
    log.setNextState(nextState);
    log.setEventTriggered(event);
    log.setChangedBy(changedBy);
    actionLogRepository.save(log);

    return campaign;
  }
}
