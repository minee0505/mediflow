package com.mediflow.emr.repository;

import com.mediflow.emr.entity.IntakeOutput;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 섭취배설량 Repository
 */
public interface IntakeOutputRepository extends JpaRepository<IntakeOutput, Long> {

    /**
     * 환자 ID로 섭취배설량 목록 조회 (기록 시간 내림차순)
     */
    List<IntakeOutput> findByPatientIdOrderByRecordedAtDesc(Long patientId);

    /**
     * 환자 ID로 섭취배설량 목록 조회 (기록 시간 오름차순)
     */
    List<IntakeOutput> findByPatientIdOrderByRecordedAtAsc(Long patientId);

    /**
     * 간호사 ID로 섭취배설량 목록 조회
     */
    List<IntakeOutput> findByNurseId(Long nurseId);

    /**
     * 환자 ID와 기록 시간 범위로 조회
     */
    List<IntakeOutput> findByPatientIdAndRecordedAtBetween(Long patientId, LocalDateTime startTime, LocalDateTime endTime);
}
