package com.organization.taskManagement.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class AnalyticsResponse {

    private Long totalTasks;
    private Long completedTasks;
    private Long pendingTasks;
    private Map<String, Long> teamDistribution;
}
