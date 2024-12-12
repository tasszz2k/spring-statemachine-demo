package com.statemachinedemo.springstatemachinedemo.config;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import java.util.EnumSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Slf4j
@Configuration
// @EnableStateMachine
@EnableStateMachineFactory
public class CampaignStateMachineConfig
    extends StateMachineConfigurerAdapter<CampaignState, CampaignEvent> {

  @Override
  public void configure(StateMachineStateConfigurer<CampaignState, CampaignEvent> states)
      throws Exception {
    states
        .withStates()
        .initial(CampaignState.DRAFT)
        .states(EnumSet.allOf(CampaignState.class))
        .end(CampaignState.END);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<CampaignState, CampaignEvent> transitions)
      throws Exception {
    transitions
        .withExternal()
        .source(CampaignState.DRAFT)
        .target(CampaignState.FA_REVIEW)
        .event(CampaignEvent.SUBMIT_FOR_FA_REVIEW)
        .and()
        .withExternal()
        .source(CampaignState.FA_REVIEW)
        .target(CampaignState.REJECTED)
        .event(CampaignEvent.FA_REJECT)
        .and()
        .withExternal()
        .source(CampaignState.FA_REVIEW)
        .target(CampaignState.APPROVED)
        .event(CampaignEvent.FA_APPROVE)
        .and()
        .withExternal()
        .source(CampaignState.REJECTED)
        .target(CampaignState.DRAFT)
        .event(CampaignEvent.MKT_EDIT)
        .and()
        .withExternal()
        .source(CampaignState.APPROVED)
        .target(CampaignState.FA_REVIEW)
        .event(CampaignEvent.MKT_EDIT)
        .and()
        .withExternal()
        .source(CampaignState.APPROVED)
        .target(CampaignState.END)
        .event(CampaignEvent.PAUSE)
        .and()
        .withExternal()
        .source(CampaignState.APPROVED)
        .target(CampaignState.DISTRIBUTING)
        .event(CampaignEvent.DISTRIBUTE)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_REVIEW)
        .target(CampaignState.IN_USE_APPROVED)
        .event(CampaignEvent.APPROVE_BUDGET_CHANGE)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_REVIEW)
        .target(CampaignState.IN_USE_APPROVED)
        .event(CampaignEvent.REJECT_BUDGET_CHANGE)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_APPROVED)
        .target(CampaignState.END)
        .event(CampaignEvent.FINISH);
  }

  @Override
  public void configure(StateMachineConfigurationConfigurer<CampaignState, CampaignEvent> config)
      throws Exception {
    StateMachineListenerAdapter<CampaignState, CampaignEvent> adapter =
        new StateMachineListenerAdapter<>() {
          @Override
          public void stateChanged(
              State<CampaignState, CampaignEvent> from, State<CampaignState, CampaignEvent> to) {
            log.info("State change from {} to {}", from.getId(), to.getId());
          }
        };
    config.withConfiguration().listener(adapter);
  }
}
