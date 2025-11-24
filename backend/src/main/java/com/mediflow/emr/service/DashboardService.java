package com.mediflow.emr.service;

import com.mediflow.emr.dto.DepartmentSummaryResponse;
import com.mediflow.emr.dto.MyPatientResponse;
import com.mediflow.emr.entity.*;
import com.mediflow.emr.entity.enums.ShiftType;
import com.mediflow.emr.exception.BusinessException;
import com.mediflow.emr.exception.ErrorCode;
import com.mediflow.emr.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 대시보드 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final VitalSignRepository vitalSignRepository;
    private final PatientRepository patientRepository;
    private final ShiftRepository shiftRepository;

    /**
     * 내 담당 환자 목록 조회
     */
    public List<MyPatientResponse> getMyPatients(Long userId) {
        // 사용자 조회
        User nurse = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 현재 시간 기준 근무조 확인
        ShiftType currentShiftType = getCurrentShiftType();

        // 오늘 날짜의 현재 근무조 조회
        Shift currentShift = shiftRepository.findByDateAndType(today, currentShiftType)
                .orElse(null);

        if (currentShift == null) {
            log.warn("오늘 날짜의 근무조를 찾을 수 없습니다. userId: {}, date: {}, shiftType: {}", 
                    userId, today, currentShiftType);
            return List.of();
        }

        // 배정된 환자 목록 조회
        List<Assignment> assignments = assignmentRepository.findByNurseIdAndShiftId(userId, currentShift.getId());

        // 환자 정보 + 최근 바이탈 조합
        List<MyPatientResponse> responses = assignments.stream()
                .map(assignment -> {
                    Patient patient = assignment.getPatient();
                    
                    // 최근 바이탈 조회
                    List<VitalSign> vitals = vitalSignRepository.findByPatientIdOrderByMeasuredAtDesc(patient.getId());
                    VitalSign latestVital = vitals.isEmpty() ? null : vitals.get(0);

                    return MyPatientResponse.builder()
                            .patientId(patient.getId())
                            .chartNumber(patient.getChartNumber())
                            .name(patient.getName())
                            .age(patient.getAge())
                            .gender(patient.getGender())
                            .diagnosis(patient.getDiagnosis())
                            .allergies(patient.getAllergies())
                            .triageLevel(patient.getTriageLevel())
                            .isPrimary(assignment.getIsPrimary())
                            .systolicBp(latestVital != null ? latestVital.getSystolicBp() : null)
                            .diastolicBp(latestVital != null ? latestVital.getDiastolicBp() : null)
                            .heartRate(latestVital != null ? latestVital.getHeartRate() : null)
                            .bodyTemp(latestVital != null ? latestVital.getBodyTemp() : null)
                            .spo2(latestVital != null ? latestVital.getSpo2() : null)
                            .lastVitalTime(latestVital != null ? latestVital.getMeasuredAt() : null)
                            .departmentName(patient.getDepartment() != null ? patient.getDepartment().getName() : null)
                            .departmentCode(patient.getDepartment() != null ? patient.getDepartment().getCode() : null)
                            .build();
                })
                .collect(Collectors.toList());

        // 응급실 환자는 트리아지 순서로 정렬 (낮은 숫자가 우선)
        responses.sort((a, b) -> {
            // 트리아지가 있는 환자 우선
            if (a.getTriageLevel() != null && b.getTriageLevel() == null) return -1;
            if (a.getTriageLevel() == null && b.getTriageLevel() != null) return 1;
            if (a.getTriageLevel() != null && b.getTriageLevel() != null) {
                return a.getTriageLevel().compareTo(b.getTriageLevel());
            }
            // 트리아지가 없으면 주담당 우선
            return Boolean.compare(b.getIsPrimary(), a.getIsPrimary());
        });

        return responses;
    }

    /**
     * 부서 요약 정보 조회
     */
    public DepartmentSummaryResponse getDepartmentSummary(Long userId) {
        // 사용자 조회
        User nurse = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (nurse.getDepartment() == null) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NOT_FOUND);
        }

        // 부서 전체 환자 수
        Long totalPatients = (long) patientRepository.findByDepartmentIdAndIsAdmitted(
                nurse.getDepartment().getId(), true).size();

        // 내 담당 환자 수
        Long myPatients = (long) getMyPatients(userId).size();

        // 현재 근무조
        ShiftType currentShiftType = getCurrentShiftType();

        return DepartmentSummaryResponse.builder()
                .departmentName(nurse.getDepartment().getName())
                .departmentCode(nurse.getDepartment().getCode())
                .totalPatients(totalPatients)
                .myPatients(myPatients)
                .nurseName(nurse.getName())
                .shiftType(getShiftTypeKorean(currentShiftType))
                .build();
    }

    /**
     * 현재 시간 기준 근무조 타입 반환
     */
    private ShiftType getCurrentShiftType() {
        LocalTime now = LocalTime.now();
        
        if (now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(16, 0))) {
            return ShiftType.DAY;
        } else if (now.isAfter(LocalTime.of(16, 0)) || now.equals(LocalTime.MIDNIGHT)) {
            return ShiftType.EVENING;
        } else {
            return ShiftType.NIGHT;
        }
    }

    /**
     * 근무조 타입 한글 변환
     */
    private String getShiftTypeKorean(ShiftType shiftType) {
        return switch (shiftType) {
            case DAY -> "주간";
            case EVENING -> "초번";
            case NIGHT -> "야간";
        };
    }
}
