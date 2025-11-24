package com.mediflow.emr.entity;

import com.mediflow.emr.entity.enums.ImageType;
import com.mediflow.emr.entity.enums.ReadingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 의료 영상 엔티티
 * 환자의 CT, MRI, X-Ray 등 검사 영상 정보
 */
@Getter
@Entity
@Table(name = "medical_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_image_id")
    private Long id;

    /** 환자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** 영상 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "image_type", length = 20)
    private ImageType imageType;

    /** 영상 URL (더미 이미지 경로) */
    @Column(nullable = false, name = "image_url", length = 500)
    private String imageUrl;

    /** 검사 날짜 */
    @Column(nullable = false, name = "exam_date")
    private LocalDate examDate;

    /** 판독 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "reading_status", length = 20)
    @Builder.Default
    private ReadingStatus readingStatus = ReadingStatus.PENDING;
}
