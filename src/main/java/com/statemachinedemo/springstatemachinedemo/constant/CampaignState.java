package com.statemachinedemo.springstatemachinedemo.constant;

import java.util.Set;

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
  ENDED;

  // transitions needed to store a draft value
  //  private static final Set<CampaignState[]> transitionsNeededStoreDraftValue =
  //      Set.of(
  //          //          new CampaignState[] {DRAFT, FA_REVIEW},
  //          new CampaignState[] {REJECTED, DRAFT}, // Biz edits a campaign
  //          new CampaignState[] {APPROVED, FA_REVIEW}, // except for the first transition
  //          new CampaignState[] {IN_USE_APPROVED, IN_USE_REVIEW});
  //
  //  private static final Set<CampaignState[]> transitionsAppliedNewValue =
  //      Set.of(
  //          new CampaignState[] {REJECTED, DRAFT}, // apply edited value without approval
  //          new CampaignState[] {FA_REVIEW, APPROVED},
  //          new CampaignState[] {IN_USE_REVIEW, IN_USE_APPROVED});

  private static final Set<String> transitionsNeededStoreDraftValue =
      Set.of("REJECTED->DRAFT", "APPROVED->FA_REVIEW", "IN_USE_APPROVED->IN_USE_REVIEW");
  private static final Set<String> transitionsAppliedNewValue =
      Set.of(
          "REJECTED->DRAFT", // apply edited value without approval
          "FA_REVIEW->APPROVED",
          "IN_USE_REVIEW->IN_USE_APPROVED");

  public static boolean isAppliedNewValue(CampaignState from, CampaignState to) {
    String transition = from + "->" + to;
    return transitionsAppliedNewValue.contains(transition);
  }

  public static boolean isSavedDraftValue(CampaignState from, CampaignState to) {
    String transition = from + "->" + to;
    return transitionsNeededStoreDraftValue.contains(transition);
  }
}
