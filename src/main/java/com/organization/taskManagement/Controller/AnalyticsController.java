package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.AnalyticsResponseDTO;
import com.organization.taskManagement.DTO.ApiResponseDTO;
import com.organization.taskManagement.Services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

	private final AnalyticsService analyticsService;

	@GetMapping("/overview")
	public ResponseEntity<ApiResponseDTO<AnalyticsResponseDTO>> overview() {
		return ResponseEntity.ok(ApiResponseDTO.success("", analyticsService.getOverview()));
	}
}
