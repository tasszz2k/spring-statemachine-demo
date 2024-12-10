package com.statemachinedemo.springstatemachinedemo.model;

/**
 * @author anhdt9
 * @since 10/12/24
 */
public enum CampaignState {
    DRAFT,
    FA_REVIEW,
    REJECTED,
    APPROVED,
    DISTRIBUTING,
    IN_USE_REVIEW,
    IN_USE_APPROVED,
    END
}