package com.statemachinedemo.springstatemachinedemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statemachinedemo.springstatemachinedemo.model.Campaign;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anhdt9
 * @since 13/12/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignDto {
    private Campaign campaign;
    private CampaignParam paramChange;
}
