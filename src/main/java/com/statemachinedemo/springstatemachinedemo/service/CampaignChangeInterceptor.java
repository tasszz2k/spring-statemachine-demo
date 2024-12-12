package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import com.statemachinedemo.springstatemachinedemo.dto.CampaignParam;
import com.statemachinedemo.springstatemachinedemo.model.ActionLog;
import com.statemachinedemo.springstatemachinedemo.model.ChangeLog;
import com.statemachinedemo.springstatemachinedemo.repository.ActionLogRepository;
import com.statemachinedemo.springstatemachinedemo.repository.CampaignRepository;
import com.statemachinedemo.springstatemachinedemo.repository.ChangeLogRepository;
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
  private final ActionLogRepository actionLogRepository;
  private final ChangeLogRepository changeLogRepository;

  @Override
  public void preStateChange(
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
              var stateId = state.getId();
              campaignRepository
                  .findById(campaignId)
                  .ifPresent(
                      campaign -> {
                        var previousState = campaign.getCurrentState();

                        // save change log
                        if (!CampaignState.isSavedDraftValue(previousState, stateId)) {
                          return;
                        }
                        // save change log
                        var actor =
                            String.valueOf(
                                message
                                    .getHeaders()
                                    .getOrDefault(CampaignServiceImpl.ACTOR, "system"));
                        var param =
                            (CampaignParam)
                                message
                                    .getHeaders()
                                    .getOrDefault(CampaignServiceImpl.CAMPAIGN_PARAM, null);
                        var changeLog = new ChangeLog();
                        changeLog.setCampaign(campaign);
                        changeLog.setActor(actor);
                        changeLog.setState(stateId.toString());
                        changeLog.setParams(param.toJson());
                        changeLogRepository.save(changeLog);
                      });
            });
  }

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
              var stateId = state.getId();
              campaignRepository
                  .findById(campaignId)
                  .ifPresent(
                      campaign -> {
                        var previousState = campaign.getCurrentState();

                        // update to the next state
                        campaign.setCurrentState(stateId);
                        campaignRepository.save(campaign);

                        var actor =
                            String.valueOf(
                                message
                                    .getHeaders()
                                    .getOrDefault(CampaignServiceImpl.ACTOR, "system"));
                        var event = message.getPayload();

                        // update action log
                        ActionLog log = new ActionLog();
                        log.setCampaign(campaign);
                        log.setPreviousState(previousState);
                        log.setNextState(stateId);
                        log.setEventTriggered(event);
                        log.setActor(actor);
                        actionLogRepository.save(log);

                        // update the changes into the campaign
                        if (!CampaignState.isAppliedNewValue(previousState, stateId)) {
                          return;
                        }

                        // TODO: refactor this part
                        // handle a special case: IN_USE_REVIEW -> IN_USE_APPROVED: REJECT
                        // => do not apply new value
                        if (CampaignState.IN_USE_REVIEW.equals(previousState)
                            && CampaignState.IN_USE_APPROVED.equals(stateId)
                            && CampaignEvent.REJECT.equals(event)) {
                          return;
                        }

                        // get the change log from db
                        // TODO: selecting specific state instead of get last record
                        var changeLog =
                            changeLogRepository.findFirstByCampaign_CampaignIdOrderByIdDesc(
                                campaignId);
                        if (changeLog == null) {
                          return;
                        }

                        var param = CampaignParam.fromJson(changeLog.getParams());
                        // apply non-null fields
                        if (param != null) {
                          if (param.getCampaignName() != null) {
                            campaign.setCampaignName(param.getCampaignName());
                          }
                          if (param.getMktName() != null) {
                            campaign.setMktName(param.getMktName());
                          }
                          if (param.getBudget() != null) {
                            campaign.setBudget(param.getBudget());
                          }
                        }
                        campaignRepository.save(campaign);
                      });
            });
  }
}
