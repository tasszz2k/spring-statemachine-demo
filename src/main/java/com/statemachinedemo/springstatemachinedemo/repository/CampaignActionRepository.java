package com.statemachinedemo.springstatemachinedemo.repository;

import com.statemachinedemo.springstatemachinedemo.model.CampaignAction;
import com.statemachinedemo.springstatemachinedemo.model.CampaignState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Repository
public interface CampaignActionRepository extends JpaRepository<CampaignAction, Long> {
    List<CampaignAction> findByCurrentState(CampaignState state);
}