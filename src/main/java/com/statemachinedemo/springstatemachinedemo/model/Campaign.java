package com.statemachinedemo.springstatemachinedemo.model;

import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "campaigns")
public class Campaign {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long campaignId;

  private String campaignName;
  private String mktCode;
  private String mktName;
  private Double budget;

  @Enumerated(EnumType.STRING)
  private CampaignState currentState;

  @CreationTimestamp private Date createdAt;

  @UpdateTimestamp private Date updatedAt;
}
