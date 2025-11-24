package com.mediflow.emr.repository;

import com.mediflow.emr.entity.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 온보딩 Repository
 */
public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {

    /**
     * 간호사 ID로 온보딩 목록 조회
     */
    List<Onboarding> findByNurseId(Long nurseId);

    /**
     * 간호사 ID와 완료 여부로 조회
     */
    List<Onboarding> findByNurseIdAndIsCompleted(Long nurseId, Boolean isCompleted);

    /**
     * 간호사 ID로 최신 온보딩 조회
     */
    Optional<Onboarding> findTopByNurseIdOrderByCreatedAtDesc(Long nurseId);

    /**
     * 완료되지 않은 온보딩 목록 조회
     */
    List<Onboarding> findByIsCompleted(Boolean isCompleted);
}
