package com.statemachinedemo.springstatemachinedemo.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "change_logs")
public class ChangeLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "campaign_id", nullable = false)
  private Campaign campaign;

  private String actor;

  // TODO: using Json for params instead
  @Column(columnDefinition = "json")
  private String params;

  private String state;

  @CreationTimestamp private Date createdAt;

  @UpdateTimestamp private Date updatedAt;

  // Getters and Setters
}
