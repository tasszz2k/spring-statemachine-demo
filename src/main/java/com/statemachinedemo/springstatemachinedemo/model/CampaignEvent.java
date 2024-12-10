package com.statemachinedemo.springstatemachinedemo.model;

/**
 * @author anhdt9
 * @since 10/12/24
 */
public enum CampaignEvent {
    CREATE,
    SUBMIT_FOR_FA_REVIEW,
    FA_APPROVE,
    FA_REJECT,
    MKT_EDIT,
    PAUSE,
    EDIT_BUDGET,
    APPROVE_BUDGET_CHANGE,
    REJECT_BUDGET_CHANGE,
    END
}