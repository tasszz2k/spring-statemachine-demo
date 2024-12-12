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
        .end(CampaignState.ENDED);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<CampaignState, CampaignEvent> transitions)
      throws Exception {
    transitions
        .withExternal()
        .source(CampaignState.DRAFT)
        .target(CampaignState.FA_REVIEW)
        .event(CampaignEvent.SUBMIT)
        .and()
        .withExternal()
        .source(CampaignState.FA_REVIEW)
        .target(CampaignState.REJECTED)
        .event(CampaignEvent.REJECT)
        .and()
        .withExternal()
        .source(CampaignState.FA_REVIEW)
        .target(CampaignState.APPROVED)
        .event(CampaignEvent.APPROVE)
        .and()
        .withExternal()
        .source(CampaignState.REJECTED)
        .target(CampaignState.DRAFT)
        .event(CampaignEvent.EDIT)
        .and()
        .withExternal()
        .source(CampaignState.APPROVED)
        .target(CampaignState.FA_REVIEW)
        .event(CampaignEvent.EDIT)
        .and()
        .withExternal()
        .source(CampaignState.APPROVED)
        .target(CampaignState.ENDED)
        .event(CampaignEvent.TERMINATE)
        .and()
        .withExternal()
        .source(CampaignState.APPROVED)
        .target(CampaignState.DISTRIBUTION)
        .event(CampaignEvent.DISTRIBUTE)
        .and()
        .withExternal()
        .source(CampaignState.DISTRIBUTION)
        .target(CampaignState.IN_USE_REVIEW)
        .event(CampaignEvent.EDIT_BUDGET)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_REVIEW)
        .target(CampaignState.IN_USE_APPROVED)
        .event(CampaignEvent.APPROVE_BUDGET)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_REVIEW)
        .target(CampaignState.IN_USE_APPROVED)
        .event(CampaignEvent.REJECT_BUDGET)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_REVIEW)
        .target(CampaignState.IN_USE_APPROVED)
        .event(CampaignEvent.APPROVE_BUDGET)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_REVIEW)
        .target(CampaignState.IN_USE_APPROVED)
        .event(CampaignEvent.REJECT_BUDGET)
        .and()
        .withExternal()
        .source(CampaignState.IN_USE_APPROVED)
        .target(CampaignState.ENDED)
        .event(CampaignEvent.END);
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
