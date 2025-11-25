package com.mediflow.emr.dto;

import com.mediflow.emr.entity.MedicalOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalOrderResponse {
    private Long id;
    private Long patientId;
    private String orderType;
    private String orderName;
    private String orderCode;
    private String dose;
    private String route;
    private String frequency;
    private String instructions;
    private String status;
    private LocalDateTime orderedAt;
    private String orderDoctor;
    private LocalDateTime completedAt;
    private String completedBy;
    private String notes;
    private LocalDateTime createdAt;

    // 식약처 API 정보 (투약 오더인 경우)
    private DrugDetailInfo drugInfo;

    public static MedicalOrderResponse from(MedicalOrder order) {
        return MedicalOrderResponse.builder()
                .id(order.getId())
                .patientId(order.getPatient().getId())
                .orderType(order.getOrderType())
                .orderName(order.getOrderName())
                .orderCode(order.getOrderCode())
                .dose(order.getDose())
                .route(order.getRoute())
                .frequency(order.getFrequency())
                .instructions(order.getInstructions())
                .status(order.getStatus())
                .orderedAt(order.getOrderedAt())
                .orderDoctor(order.getOrderDoctor())
                .completedAt(order.getCompletedAt())
                .completedBy(order.getCompletedBy())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
