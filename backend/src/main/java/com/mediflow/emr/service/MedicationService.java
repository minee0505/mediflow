package com.mediflow.emr.service;

import com.mediflow.emr.dto.MedicationRequest;
import com.mediflow.emr.dto.MedicationResponse;
import com.mediflow.emr.entity.Medication;
import com.mediflow.emr.entity.Patient;
import com.mediflow.emr.entity.User;
import com.mediflow.emr.exception.BusinessException;
import com.mediflow.emr.exception.ErrorCode;
import com.mediflow.emr.repository.MedicationRepository;
import com.mediflow.emr.repository.PatientRepository;
import com.mediflow.emr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 투약 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    /**
     * 투약 기록 등록
     */
    @Transactional
    public MedicationResponse createMedication(Long nurseId, MedicationRequest request) {
        log.info("투약 기록 등록 - nurseId: {}, patientId: {}", nurseId, request.patientId());

        // 간호사 조회
        User nurse = userRepository.findById(nurseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 환자 조회
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PATIENT_NOT_FOUND));

        // 투약 시간 (요청에 없으면 현재 시간)
        LocalDateTime administeredAt = request.administeredAt() != null 
                ? request.administeredAt() 
                : LocalDateTime.now();

        // 미래 시간 검증
        if (administeredAt.isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "투약 시간은 현재 시간보다 미래일 수 없습니다");
        }

        // 투약 기록 생성
        Medication medication = Medication.builder()
                .patient(patient)
                .nurse(nurse)
                .drugName(request.drugName())
                .drugCode(request.drugCode())
                .dose(request.dose())
                .route(request.route())
                .frequency(request.frequency())
                .administeredAt(administeredAt)
                .orderDoctor(request.orderDoctor())
                .build();

        Medication saved = medicationRepository.save(medication);
        log.info("투약 기록 등록 완료 - id: {}", saved.getId());

        return toResponse(saved, nurseId);
    }

    /**
     * 환자의 투약 기록 목록 조회 (최신순)
     */
    public List<MedicationResponse> getPatientMedications(Long patientId, Long currentUserId) {
        log.info("환자 투약 목록 조회 - patientId: {}, currentUserId: {}", patientId, currentUserId);

        // 환자 존재 확인
        if (!patientRepository.existsById(patientId)) {
            throw new BusinessException(ErrorCode.PATIENT_NOT_FOUND);
        }

        List<Medication> medications = medicationRepository.findByPatientIdOrderByAdministeredAtDesc(patientId);
        log.info("조회된 투약 기록 수: {}", medications.size());

        return medications.stream()
                .map(med -> toResponse(med, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * 투약 기록 수정
     */
    @Transactional
    public MedicationResponse updateMedication(Long medicationId, Long nurseId, MedicationRequest request) {
        log.info("투약 기록 수정 - medicationId: {}, nurseId: {}", medicationId, nurseId);

        // 기록 조회
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEDICATION_NOT_FOUND));

        // 권한 확인 (본인이 등록한 기록만 수정 가능)
        if (!medication.getNurse().getId().equals(nurseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인이 등록한 기록만 수정할 수 있습니다");
        }

        // 투약 시간 (요청에 없으면 기존 시간 유지)
        LocalDateTime administeredAt = request.administeredAt() != null 
                ? request.administeredAt() 
                : medication.getAdministeredAt();

        // 미래 시간 검증
        if (administeredAt.isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "투약 시간은 현재 시간보다 미래일 수 없습니다");
        }

        // 기록 수정
        medication.update(
                request.drugName(),
                request.drugCode(),
                request.dose(),
                request.route(),
                request.frequency(),
                administeredAt,
                request.orderDoctor()
        );

        log.info("투약 기록 수정 완료 - id: {}", medication.getId());
        return toResponse(medication, nurseId);
    }

    /**
     * 투약 기록 삭제
     */
    @Transactional
    public void deleteMedication(Long medicationId, Long nurseId) {
        log.info("투약 기록 삭제 - medicationId: {}, nurseId: {}", medicationId, nurseId);

        // 기록 조회
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEDICATION_NOT_FOUND));

        // 권한 확인 (본인이 등록한 기록만 삭제 가능)
        if (!medication.getNurse().getId().equals(nurseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인이 등록한 기록만 삭제할 수 있습니다");
        }

        medicationRepository.delete(medication);
        log.info("투약 기록 삭제 완료 - id: {}", medicationId);
    }

    /**
     * Entity -> Response DTO 변환
     */
    private MedicationResponse toResponse(Medication medication, Long currentUserId) {
        boolean canEdit = currentUserId != null && 
                         medication.getNurse().getId().equals(currentUserId);
        
        return MedicationResponse.builder()
                .id(medication.getId())
                .patientId(medication.getPatient().getId())
                .patientName(medication.getPatient().getName())
                .nurseId(medication.getNurse().getId())
                .nurseName(medication.getNurse().getName())
                .drugName(medication.getDrugName())
                .drugCode(medication.getDrugCode())
                .dose(medication.getDose())
                .route(medication.getRoute())
                .frequency(medication.getFrequency())
                .administeredAt(medication.getAdministeredAt())
                .orderDoctor(medication.getOrderDoctor())
                .createdAt(medication.getCreatedAt())
                .canEdit(canEdit)
                .build();
    }
}
