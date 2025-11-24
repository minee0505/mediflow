package com.mediflow.emr.repository;

import com.mediflow.emr.entity.NursingNote;
import com.mediflow.emr.entity.enums.NoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 간호기록 Repository
 */
public interface NursingNoteRepository extends JpaRepository<NursingNote, Long> {

    /**
     * 환자 ID로 간호기록 목록 조회 (생성 시간 내림차순)
     */
    List<NursingNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    /**
     * 환자 ID로 간호기록 목록 조회 (생성 시간 오름차순)
     */
    List<NursingNote> findByPatientIdOrderByCreatedAtAsc(Long patientId);

    /**
     * 간호사 ID로 간호기록 목록 조회
     */
    List<NursingNote> findByNurseId(Long nurseId);

    /**
     * 환자 ID와 카테고리로 조회
     */
    List<NursingNote> findByPatientIdAndCategory(Long patientId, NoteCategory category);

    /**
     * 중요 표시된 기록 조회
     */
    List<NursingNote> findByPatientIdAndIsImportantOrderByCreatedAtDesc(Long patientId, Boolean isImportant);

    /**
     * AI 제안 기록 조회
     */
    List<NursingNote> findByPatientIdAndAiSuggestedOrderByCreatedAtDesc(Long patientId, Boolean aiSuggested);

    /**
     * 기록 내용으로 검색
     */
    List<NursingNote> findByPlainTextContaining(String keyword);
}
