package com.mediflow.emr.repository;

import com.mediflow.emr.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 투약 Repository
 */
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    /**
     * 환자 ID로 투약 목록 조회 (투약 시간 내림차순)
     */
    List<Medication> findByPatientIdOrderByAdministeredAtDesc(Long patientId);

    /**
     * 환자 ID로 투약 목록 조회 (투약 시간 오름차순)
     */
    List<Medication> findByPatientIdOrderByAdministeredAtAsc(Long patientId);

    /**
     * 간호사 ID로 투약 목록 조회
     */
    List<Medication> findByNurseId(Long nurseId);

    /**
     * 환자 ID와 투약 시간 범위로 조회
     */
    List<Medication> findByPatientIdAndAdministeredAtBetween(Long patientId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 약물명으로 검색
     */
    List<Medication> findByDrugNameContaining(String drugName);
}
