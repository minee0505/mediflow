package com.mediflow.emr.entity;

import com.mediflow.emr.entity.enums.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 부서 엔티티
 * 응급실, 중환자실, 병동, 외래 등 병원 부서 정보
 */
@Getter
@Entity
@Table(name = "department")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    /** 부서명 (예: 응급실, 중환자실, 내과병동 3층) */
    @Column(nullable = false, length = 100)
    private String name;

    /** 부서 코드 (예: ER, ICU, MW3, SW5) */
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    /** 병상 수 (병동/중환자실만 해당) */
    @Column(name = "bed_count")
    private Integer bedCount;

    /** 부서 구분 (진료과/특수부서) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Department type;
}
