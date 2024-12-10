package com.statemachinedemo.springstatemachinedemo.repository;

import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}