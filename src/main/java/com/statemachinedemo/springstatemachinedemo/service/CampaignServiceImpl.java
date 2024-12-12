package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import com.statemachinedemo.springstatemachinedemo.dto.CampaignParam;
import com.statemachinedemo.springstatemachinedemo.model.ActionLog;
import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import com.statemachinedemo.springstatemachinedemo.repository.ActionLogRepository;
import com.statemachinedemo.springstatemachinedemo.repository.CampaignRepository;
import com.statemachinedemo.springstatemachinedemo.repository.ChangeLogRepository;
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
public class CampaignServiceImpl implements CampaignService {

  public static final String CAMPAIGN_ID = "campaign_id";
  public static final String ACTOR = "actor";
  public static final String CAMPAIGN_PARAM = "campaign_param";

  private final CampaignRepository campaignRepository;
  private final ActionLogRepository actionLogRepository;
  private final ChangeLogRepository changeLogRepository;
  private final StateMachineFactory<CampaignState, CampaignEvent> stateMachineFactory;
  private final CampaignChangeInterceptor campaignChangeInterceptor;

  public Campaign findById(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
  }

  @Deprecated
  public Campaign transitionState(Long campaignId, CampaignEvent event, String changedBy) {
    Campaign campaign = findById(campaignId);

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

  @Override
  public Campaign transitionState(Long id, CampaignEvent event, CampaignParam param, String actor) {
    sendEvent(id, event, actor, param);
    // return the updated campaign
    return findById(id);
  }

  private StateMachine<CampaignState, CampaignEvent> build(Long campaignId) {
    var campaign = findById(campaignId);
    StateMachine<CampaignState, CampaignEvent> sm =
        stateMachineFactory.getStateMachine(campaignId.toString());

    // sm.stop() is deprecated, use sm.stopReactively() instead
    sm.stop();

    sm.getStateMachineAccessor()
        .doWithAllRegions(
            accessor -> {
              accessor.addStateMachineInterceptor(campaignChangeInterceptor);
              accessor.resetStateMachine(
                  new DefaultStateMachineContext<>(campaign.getCurrentState(), null, null, null));
            });
    // sm.startReactively()
    sm.start();
    return sm;
  }

  private void sendEvent(Long campaignId, CampaignEvent event, String actor) {
    sendEvent(campaignId, event, actor, null);
  }

  private void sendEvent(Long campaignId, CampaignEvent event, String actor, CampaignParam param) {
    var sm = build(campaignId);

    var msg =
        MessageBuilder.withPayload(event)
            .setHeader(CAMPAIGN_ID, campaignId)
            .setHeader(ACTOR, actor)
            .setHeader(CAMPAIGN_PARAM, param)
            .build();

    sm.sendEvent(Mono.just(msg))
        .doOnNext(
            result -> {
              if (!result.getResultType().equals(StateMachineEventResult.ResultType.ACCEPTED)) {
                throw new IllegalStateException("Invalid state transition");
              }
            })
        .blockLast();
  }

  @Override
  public Campaign newCampaign(Campaign campaign) {
    campaign.setCurrentState(CampaignState.DRAFT);
    // save the campaign with the initial state = DRAFT
    var savedCampaign = campaignRepository.save(campaign);

    // log action

    return savedCampaign;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> submitForFAReview(Long id, String actor) {
    sendEvent(id, CampaignEvent.SUBMIT, actor);
    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> reject(Long id, String actor) {
    sendEvent(id, CampaignEvent.REJECT, actor);
    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> approve(Long id, String actor) {
    sendEvent(id, CampaignEvent.APPROVE, actor);
    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> edit(
      Long id, CampaignParam param, String actor) {
    sendEvent(id, CampaignEvent.EDIT, actor);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> editAfterApproved(
      Long id, CampaignParam param, String actor) {
    sendEvent(id, CampaignEvent.EDIT, actor, param);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> terminate(Long id, String actor) {
    sendEvent(id, CampaignEvent.TERMINATE, actor);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> distribute(Long id, String actor) {
    sendEvent(id, CampaignEvent.DISTRIBUTE, actor);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> approveForUse(Long id, String actor) {
    sendEvent(id, CampaignEvent.APPROVE, actor);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> editBudget(
      Long id, CampaignParam param, String actor) {
    sendEvent(id, CampaignEvent.EDIT, actor);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> rejectBudget(Long id, String actor) {
    sendEvent(id, CampaignEvent.REJECT, actor);

    return null;
  }

  @Override
  public StateMachine<CampaignState, CampaignEvent> approveBudget(Long id, String actor) {
    sendEvent(id, CampaignEvent.APPROVE, actor);

    return null;
  }
}
