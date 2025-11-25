package com.mediflow.emr.entity;

import com.mediflow.emr.entity.enums.TestStatus;
import com.mediflow.emr.entity.enums.TestType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 검사 결과 엔티티
 */
@Entity
@Table(name = "test_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_result_id")
    private Long id;

    /** 환자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** 확인한 간호사 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private User nurse;

    /** 검사 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TestType testType;

    /** 검사명 */
    @Column(nullable = false, length = 200)
    private String testName;

    /** 결과값/판독소견 */
    @Column(columnDefinition = "TEXT")
    private String resultValue;

    /** 참고치 */
    @Column(length = 100)
    private String referenceRange;

    /** 이상 여부 */
    @Column(nullable = false)
    private Boolean isAbnormal;

    /** 검사 날짜 */
    @Column(nullable = false)
    private LocalDate testDate;

    /** 결과 나온 시간 */
    @Column(nullable = false)
    private LocalDateTime resultDate;

    /** 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TestStatus status;
}
