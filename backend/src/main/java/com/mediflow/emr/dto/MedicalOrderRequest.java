package com.mediflow.emr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalOrderRequest {
    private Long patientId;
    private String orderType;
    private String orderName;
    private String orderCode;
    private String dose;
    private String route;
    private String frequency;
    private String instructions;
    private LocalDateTime orderedAt;
    private String orderDoctor;
}
