package com.mediflow.emr.repository;

import com.mediflow.emr.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 환자 Repository
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * 차트번호로 환자 조회
     */
    Optional<Patient> findByChartNumber(String chartNumber);

    /**
     * 부서 ID로 환자 목록 조회
     */
    List<Patient> findByDepartmentId(Long departmentId);

    /**
     * 입원 여부로 환자 목록 조회
     */
    List<Patient> findByIsAdmitted(Boolean isAdmitted);

    /**
     * 부서와 입원 여부로 환자 목록 조회
     */
    List<Patient> findByDepartmentIdAndIsAdmitted(Long departmentId, Boolean isAdmitted);

    /**
     * 차트번호 존재 여부 확인
     */
    boolean existsByChartNumber(String chartNumber);
}
