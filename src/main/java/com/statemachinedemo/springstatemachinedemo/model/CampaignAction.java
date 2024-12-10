package com.statemachinedemo.springstatemachinedemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author anhdt9
 * @since 10/12/24
 */

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class CampaignAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionId;

    @Enumerated(EnumType.STRING)
    private CampaignState currentState;

    @Enumerated(EnumType.STRING)
    private CampaignEvent event;

    @Enumerated(EnumType.STRING)
    private CampaignState nextState;

    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Getters and Setters
}
