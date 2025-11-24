package com.mediflow.emr.controller;

import com.mediflow.emr.dto.ApiResponse;
import com.mediflow.emr.dto.DepartmentSummaryResponse;
import com.mediflow.emr.dto.MyPatientResponse;
import com.mediflow.emr.entity.User;
import com.mediflow.emr.exception.BusinessException;
import com.mediflow.emr.exception.ErrorCode;
import com.mediflow.emr.repository.UserRepository;
import com.mediflow.emr.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 대시보드 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    /**
     * 내 담당 환자 목록 조회
     */
    @GetMapping("/my-patients")
    public ResponseEntity<ApiResponse<List<MyPatientResponse>>> getMyPatients(Authentication authentication) {
        
        String providerId = authentication.getName(); // JWT subject (providerId)
        log.info("내 담당 환자 목록 조회 요청: providerId={}", providerId);
        
        // providerId로 사용자 조회
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        List<MyPatientResponse> patients = dashboardService.getMyPatients(user.getId());
        
        return ResponseEntity.ok(ApiResponse.ok(patients));
    }

    /**
     * 부서 요약 정보 조회
     */
    @GetMapping("/department-summary")
    public ResponseEntity<ApiResponse<DepartmentSummaryResponse>> getDepartmentSummary(Authentication authentication) {
        
        String providerId = authentication.getName(); // JWT subject (providerId)
        log.info("부서 요약 정보 조회 요청: providerId={}", providerId);
        
        // providerId로 사용자 조회
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        DepartmentSummaryResponse summary = dashboardService.getDepartmentSummary(user.getId());
        
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }
}
