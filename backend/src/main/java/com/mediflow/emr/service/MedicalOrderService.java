package com.mediflow.emr.service;

import com.mediflow.emr.dto.DrugDetailInfo;
import com.mediflow.emr.dto.MedicalOrderRequest;
import com.mediflow.emr.dto.MedicalOrderResponse;
import com.mediflow.emr.entity.MedicalOrder;
import com.mediflow.emr.entity.Patient;
import com.mediflow.emr.exception.BusinessException;
import com.mediflow.emr.exception.ErrorCode;
import com.mediflow.emr.repository.MedicalOrderRepository;
import com.mediflow.emr.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalOrderService {

    private final MedicalOrderRepository medicalOrderRepository;
    private final PatientRepository patientRepository;
    private final DrugApiService drugApiService;

    /**
     * 환자별 오더 목록 조회
     */
    @Transactional(readOnly = true)
    public List<MedicalOrderResponse> getOrdersByPatient(Long patientId) {
        List<MedicalOrder> orders = medicalOrderRepository.findByPatientIdOrderByOrderedAtDesc(patientId);
        
        return orders.stream()
                .map(order -> {
                    MedicalOrderResponse response = MedicalOrderResponse.from(order);
                    
                    // 투약 오더이고 약품명이 있으면 식약처 API 호출
                    if ("MEDICATION".equals(order.getOrderType()) && order.getOrderName() != null) {
                        try {
                            DrugDetailInfo drugInfo = drugApiService.getDrugDetail(order.getOrderName());
                            response.setDrugInfo(drugInfo);
                        } catch (Exception e) {
                            log.warn("약품 상세 정보 조회 실패: {}", order.getOrderName(), e);
                        }
                    }
                    
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 오더 등록
     */
    @Transactional
    public MedicalOrderResponse createOrder(MedicalOrderRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PATIENT_NOT_FOUND));

        MedicalOrder order = MedicalOrder.builder()
                .patient(patient)
                .orderType(request.getOrderType())
                .orderName(request.getOrderName())
                .orderCode(request.getOrderCode())
                .dose(request.getDose())
                .route(request.getRoute())
                .frequency(request.getFrequency())
                .instructions(request.getInstructions())
                .status("PENDING")
                .orderedAt(request.getOrderedAt() != null ? request.getOrderedAt() : LocalDateTime.now())
                .orderDoctor(request.getOrderDoctor())
                .build();

        MedicalOrder savedOrder = medicalOrderRepository.save(order);
        return MedicalOrderResponse.from(savedOrder);
    }

    /**
     * 오더 상태 변경
     */
    @Transactional
    public MedicalOrderResponse updateOrderStatus(Long orderId, String status, String completedBy) {
        MedicalOrder order = medicalOrderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.updateStatus(status, completedBy);
        return MedicalOrderResponse.from(order);
    }

    /**
     * 오더 삭제
     */
    @Transactional
    public void deleteOrder(Long orderId) {
        MedicalOrder order = medicalOrderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        
        medicalOrderRepository.delete(order);
    }
}
