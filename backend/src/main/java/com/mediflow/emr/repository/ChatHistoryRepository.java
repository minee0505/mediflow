package com.mediflow.emr.repository;

import com.mediflow.emr.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * AI 챗봇 대화 기록 Repository
 */
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    /**
     * 간호사 ID로 대화 기록 조회 (생성 시간 내림차순)
     */
    List<ChatHistory> findByNurseIdOrderByCreatedAtDesc(Long nurseId);

    /**
     * 간호사 ID로 대화 기록 조회 (생성 시간 오름차순)
     */
    List<ChatHistory> findByNurseIdOrderByCreatedAtAsc(Long nurseId);

    /**
     * 액션 유형으로 조회
     */
    List<ChatHistory> findByActionType(String actionType);

    /**
     * 간호사 ID와 액션 유형으로 조회
     */
    List<ChatHistory> findByNurseIdAndActionType(Long nurseId, String actionType);
}
