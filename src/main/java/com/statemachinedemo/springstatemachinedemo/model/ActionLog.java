package com.statemachinedemo.springstatemachinedemo.model;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignEvent;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "action_logs")
public class ActionLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long logId;

  @ManyToOne
  @JoinColumn(name = "campaign_id", nullable = false)
  private Campaign campaign;

  @Enumerated(EnumType.STRING)
  private CampaignState previousState;

  @Enumerated(EnumType.STRING)
  private CampaignState nextState;

  @Enumerated(EnumType.STRING)
  private CampaignEvent eventTriggered;

  private String actor;

  private String note;

  @CreationTimestamp private Date changedAt;
}
