package com.statemachinedemo.springstatemachinedemo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    private String campaignName;
    private String mktCode;
    private String mktName;
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    private CampaignState currentState;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Getters and Setters
}