package com.statemachinedemo.springstatemachinedemo.service;

import com.statemachinedemo.springstatemachinedemo.model.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.model.CampaignState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Configuration
//@EnableStateMachine
@EnableStateMachineFactory
public class CampaignStateMachineConfig extends StateMachineConfigurerAdapter<CampaignState, CampaignEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<CampaignState, CampaignEvent> states) throws Exception {
        states
                .withStates()
                .initial(CampaignState.DRAFT)
                .states(EnumSet.allOf(CampaignState.class))
                .end(CampaignState.END);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<CampaignState, CampaignEvent> transitions) throws Exception {
        transitions
                .withExternal().source(CampaignState.DRAFT).target(CampaignState.FA_REVIEW).event(CampaignEvent.SUBMIT_FOR_FA_REVIEW)
                .and().withExternal().source(CampaignState.FA_REVIEW).target(CampaignState.REJECTED).event(CampaignEvent.FA_REJECT)
                .and().withExternal().source(CampaignState.FA_REVIEW).target(CampaignState.APPROVED).event(CampaignEvent.FA_APPROVE)
                .and().withExternal().source(CampaignState.REJECTED).target(CampaignState.DRAFT).event(CampaignEvent.MKT_EDIT)
                .and().withExternal().source(CampaignState.APPROVED).target(CampaignState.FA_REVIEW).event(CampaignEvent.MKT_EDIT)
                .and().withExternal().source(CampaignState.APPROVED).target(CampaignState.END).event(CampaignEvent.PAUSE)
                .and().withExternal().source(CampaignState.APPROVED).target(CampaignState.DISTRIBUTING).event(CampaignEvent.DISTRIBUTE)
                .and().withExternal().source(CampaignState.IN_USE_REVIEW).target(CampaignState.IN_USE_APPROVED).event(CampaignEvent.APPROVE_BUDGET_CHANGE)
                .and().withExternal().source(CampaignState.IN_USE_REVIEW).target(CampaignState.IN_USE_APPROVED).event(CampaignEvent.REJECT_BUDGET_CHANGE)
                .and().withExternal().source(CampaignState.IN_USE_APPROVED).target(CampaignState.END).event(CampaignEvent.FINISH);
    }
}
