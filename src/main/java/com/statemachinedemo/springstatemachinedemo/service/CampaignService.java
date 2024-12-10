package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import com.statemachinedemo.springstatemachinedemo.model.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.model.CampaignState;
import com.statemachinedemo.springstatemachinedemo.model.CampaignStateLog;
import com.statemachinedemo.springstatemachinedemo.repository.CampaignRepository;
import com.statemachinedemo.springstatemachinedemo.repository.CampaignStateLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Service
@RequiredArgsConstructor
public class CampaignService {

  private final CampaignRepository campaignRepository;
  private final CampaignStateLogRepository campaignStateLogRepository;
  private final StateMachineFactory<CampaignState, CampaignEvent> stateMachineFactory;

  public Campaign getCampaign(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Campaign not found"));
  }

  public Campaign transitionState(Long campaignId, CampaignEvent event, String changedBy) {
    Campaign campaign = getCampaign(campaignId);

    StateMachine<CampaignState, CampaignEvent> stateMachine =
        stateMachineFactory.getStateMachine(campaignId.toString());
    stateMachine
        .getStateMachineAccessor()
        .doWithAllRegions(
            accessor ->
                accessor.resetStateMachine(
                    new DefaultStateMachineContext<>(
                        campaign.getCurrentState(), null, null, null)));

    if (!stateMachine.sendEvent(event)) {
      throw new IllegalStateException("Invalid state transition");
    }

    CampaignState previousState = campaign.getCurrentState();
    CampaignState nextState = stateMachine.getState().getId();

    campaign.setCurrentState(nextState);
    campaignRepository.save(campaign);

    CampaignStateLog log = new CampaignStateLog();
    log.setCampaign(campaign);
    log.setPreviousState(previousState);
    log.setNextState(nextState);
    log.setEventTriggered(event);
    log.setChangedBy(changedBy);
    campaignStateLogRepository.save(log);

    return campaign;
  }
}
