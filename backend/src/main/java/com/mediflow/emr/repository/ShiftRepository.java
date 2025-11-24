package com.mediflow.emr.repository;

import com.mediflow.emr.entity.Shift;
import com.mediflow.emr.entity.enums.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 근무조 Repository
 */
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    /**
     * 날짜로 근무조 목록 조회
     */
    List<Shift> findByDate(LocalDate date);

    /**
     * 날짜와 근무조 유형으로 조회
     */
    Optional<Shift> findByDateAndType(LocalDate date, ShiftType type);

    /**
     * 날짜 범위로 근무조 목록 조회
     */
    List<Shift> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
