package com.statemachinedemo.springstatemachinedemo.repository;

import com.statemachinedemo.springstatemachinedemo.model.ChangeLog;
import com.statemachinedemo.springstatemachinedemo.constant.CampaignState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
}