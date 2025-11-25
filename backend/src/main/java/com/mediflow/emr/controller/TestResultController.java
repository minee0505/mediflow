package com.mediflow.emr.controller;

import com.mediflow.emr.dto.ApiResponse;
import com.mediflow.emr.dto.TestResultResponse;
import com.mediflow.emr.service.TestResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 검사 결과 컨트롤러 (조회 전용)
 */
@Slf4j
@RestController
@RequestMapping("/api/test-results")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

    /**
     * 환자별 검사 결과 목록 조회
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<TestResultResponse>>> getTestResultsByPatient(
            @PathVariable Long patientId) {
        log.info("환자 검사 결과 조회 요청: patientId={}", patientId);
        List<TestResultResponse> testResults = testResultService.getTestResultsByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.ok(testResults));
    }
}
