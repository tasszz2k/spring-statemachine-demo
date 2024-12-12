package com.statemachinedemo.springstatemachinedemo.repository;

import com.statemachinedemo.springstatemachinedemo.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}