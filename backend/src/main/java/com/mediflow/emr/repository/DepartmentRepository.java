package com.mediflow.emr.repository;

import com.mediflow.emr.entity.DepartmentEntity;
import com.mediflow.emr.entity.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 부서 Repository
 */
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    /**
     * 부서 코드로 조회
     */
    Optional<DepartmentEntity> findByCode(String code);

    /**
     * 부서 유형으로 목록 조회
     */
    List<DepartmentEntity> findByType(Department type);

    /**
     * 부서 코드 존재 여부 확인
     */
    boolean existsByCode(String code);
}
