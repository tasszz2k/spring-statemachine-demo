package com.statemachinedemo.springstatemachinedemo.constant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author anhdt9
 * @since 13/12/24
 */class CampaignStateTest {

    @Test
    void isAppliedNewValue() {
      }

    @Test
    void isSavedDraftValue() {
        // test case: from REJECTED -> DRAFT
        assertTrue(CampaignState.isSavedDraftValue(CampaignState.REJECTED, CampaignState.DRAFT));
      }
}