package com.mediflow.emr.repository;

import com.mediflow.emr.entity.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 바이탈 사인 Repository
 */
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {

    /**
     * 환자 ID로 바이탈 목록 조회 (측정 시간 내림차순)
     */
    List<VitalSign> findByPatientIdOrderByMeasuredAtDesc(Long patientId);

    /**
     * 환자 ID로 바이탈 목록 조회 (측정 시간 오름차순)
     */
    List<VitalSign> findByPatientIdOrderByMeasuredAtAsc(Long patientId);

    /**
     * 간호사 ID로 바이탈 목록 조회
     */
    List<VitalSign> findByNurseId(Long nurseId);

    /**
     * 환자 ID와 측정 시간 범위로 조회
     */
    List<VitalSign> findByPatientIdAndMeasuredAtBetween(Long patientId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 측정 시간 범위로 조회
     */
    List<VitalSign> findByMeasuredAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}
