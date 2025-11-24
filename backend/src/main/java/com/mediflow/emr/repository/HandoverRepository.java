package com.mediflow.emr.repository;

import com.mediflow.emr.entity.Handover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 인수인계 Repository
 */
public interface HandoverRepository extends JpaRepository<Handover, Long> {

    /**
     * 부서 ID와 인수인계 날짜로 조회
     */
    List<Handover> findByDepartmentIdAndHandoverDate(Long departmentId, LocalDate handoverDate);

    /**
     * 부서 ID로 인수인계 목록 조회 (날짜 내림차순)
     */
    List<Handover> findByDepartmentIdOrderByHandoverDateDesc(Long departmentId);

    /**
     * 인수인계 날짜로 조회
     */
    List<Handover> findByHandoverDate(LocalDate handoverDate);

    /**
     * 인계 근무조 ID로 조회
     */
    List<Handover> findByFromShiftId(Long fromShiftId);

    /**
     * 인수 근무조 ID로 조회
     */
    List<Handover> findByToShiftId(Long toShiftId);

    /**
     * 작성자 ID로 조회
     */
    List<Handover> findByCreatedById(Long createdById);

    /**
     * 부서, 인계 근무조, 인수 근무조로 조회
     */
    Optional<Handover> findByDepartmentIdAndFromShiftIdAndToShiftId(Long departmentId, Long fromShiftId, Long toShiftId);
}
