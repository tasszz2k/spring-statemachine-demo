package com.statemachinedemo.springstatemachinedemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class CampaignStateLog {
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

    private String changedBy;

    @CreationTimestamp
    private LocalDateTime changedAt;

    // Getters and Setters
}