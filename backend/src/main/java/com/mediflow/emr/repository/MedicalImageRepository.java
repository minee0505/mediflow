package com.mediflow.emr.repository;

import com.mediflow.emr.entity.MedicalImage;
import com.mediflow.emr.entity.enums.ImageType;
import com.mediflow.emr.entity.enums.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * 의료 영상 Repository
 */
public interface MedicalImageRepository extends JpaRepository<MedicalImage, Long> {

    /**
     * 환자 ID로 영상 목록 조회 (검사 날짜 내림차순)
     */
    List<MedicalImage> findByPatientIdOrderByExamDateDesc(Long patientId);

    /**
     * 환자 ID와 영상 유형으로 조회
     */
    List<MedicalImage> findByPatientIdAndImageType(Long patientId, ImageType imageType);

    /**
     * 환자 ID와 판독 상태로 조회
     */
    List<MedicalImage> findByPatientIdAndReadingStatus(Long patientId, ReadingStatus readingStatus);

    /**
     * 판독 상태로 조회
     */
    List<MedicalImage> findByReadingStatus(ReadingStatus readingStatus);

    /**
     * 검사 날짜 범위로 조회
     */
    List<MedicalImage> findByExamDateBetween(LocalDate startDate, LocalDate endDate);
}
