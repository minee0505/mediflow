package com.mediflow.emr.entity;

import com.mediflow.emr.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 간호사 사용자 엔티티
 * EMR 시스템의 간호사 계정 정보
 */
@Getter
@Entity
@Table(name = "nurse_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NurseUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /** 이메일 (로그인 ID) */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /** 비밀번호 (BCrypt 암호화) */
    @Column(nullable = false, length = 500)
    private String password;

    /** 이름 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 역할 (ADMIN, NURSE, DOCTOR) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /** 소속 부서 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    /** 연락처 */
    @Column(length = 20)
    private String phone;

    /** 입사일 */
    @Column(name = "hire_date")
    private LocalDate hireDate;

    /** 활성화 여부 */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
