package com.mediflow.emr.dto;

import com.mediflow.emr.entity.TestResult;
import com.mediflow.emr.entity.enums.TestStatus;
import com.mediflow.emr.entity.enums.TestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {
    private Long id;
    private Long patientId;
    private String nurseName;
    private TestType testType;
    private String testName;
    private String resultValue;
    private String referenceRange;
    private Boolean isAbnormal;
    private LocalDate testDate;
    private LocalDateTime resultDate;
    private TestStatus status;

    public static TestResultResponse from(TestResult testResult) {
        return TestResultResponse.builder()
                .id(testResult.getId())
                .patientId(testResult.getPatient().getId())
                .nurseName(testResult.getNurse() != null ? testResult.getNurse().getName() : null)
                .testType(testResult.getTestType())
                .testName(testResult.getTestName())
                .resultValue(testResult.getResultValue())
                .referenceRange(testResult.getReferenceRange())
                .isAbnormal(testResult.getIsAbnormal())
                .testDate(testResult.getTestDate())
                .resultDate(testResult.getResultDate())
                .status(testResult.getStatus())
                .build();
    }
}
