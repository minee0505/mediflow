package com.mediflow.emr.repository;

import com.mediflow.emr.entity.Assignment;
import com.mediflow.emr.entity.Patient;
import com.mediflow.emr.entity.Shift;
import com.mediflow.emr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * 환자 배정 Repository
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /**
     * 간호사 ID와 근무조 ID로 배정 목록 조회
     */
    List<Assignment> findByNurseIdAndShiftId(Long nurseId, Long shiftId);

    /**
     * 간호사 ID로 배정 목록 조회
     */
    List<Assignment> findByNurseId(Long nurseId);

    /**
     * 환자 ID로 배정 목록 조회
     */
    List<Assignment> findByPatientId(Long patientId);

    /**
     * 근무조 ID로 배정 목록 조회
     */
    List<Assignment> findByShiftId(Long shiftId);

    /**
     * 배정 날짜로 목록 조회
     */
    List<Assignment> findByAssignedDate(LocalDate assignedDate);

    /**
     * 간호사와 배정 날짜로 목록 조회
     */
    List<Assignment> findByNurseIdAndAssignedDate(Long nurseId, LocalDate assignedDate);

    /**
     * 주담당 배정 목록 조회
     */
    List<Assignment> findByPatientIdAndIsPrimary(Long patientId, Boolean isPrimary);

    /**
     * 중복 배정 확인
     */
    boolean existsByNurseAndPatientAndShift(User nurse, Patient patient, Shift shift);
}
