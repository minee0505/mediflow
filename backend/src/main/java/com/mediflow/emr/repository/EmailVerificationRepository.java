package com.mediflow.emr.repository;


import com.mediflow.emr.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    /**
     * 이메일로 인증 정보 조회
     */
    Optional<EmailVerification> findByEmail(String email);

    /**
     * 이메일로 인증 정보 존재 여부 확인
     */
    boolean existsByEmail(String email);

    /**
     * 이메일로 인증 정보 삭제
     */
    void deleteByEmail(String email);
}
