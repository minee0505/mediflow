package com.mediflow.emr.controller;

import com.mediflow.emr.dto.ApiResponse;
import com.mediflow.emr.dto.MedicalOrderRequest;
import com.mediflow.emr.dto.MedicalOrderResponse;
import com.mediflow.emr.service.MedicalOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class MedicalOrderController {

    private final MedicalOrderService medicalOrderService;

    /**
     * 환자별 오더 목록 조회
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<MedicalOrderResponse>>> getOrdersByPatient(
            @PathVariable Long patientId) {
        log.info("환자 오더 목록 조회 요청: patientId={}", patientId);
        List<MedicalOrderResponse> orders = medicalOrderService.getOrdersByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.ok(orders));
    }

    /**
     * 오더 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MedicalOrderResponse>> createOrder(
            @RequestBody MedicalOrderRequest request) {
        log.info("오더 등록 요청: {}", request);
        MedicalOrderResponse order = medicalOrderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.ok(order));
    }

    /**
     * 오더 상태 변경
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<MedicalOrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) String completedBy) {
        log.info("오더 상태 변경 요청: orderId={}, status={}", orderId, status);
        MedicalOrderResponse order = medicalOrderService.updateOrderStatus(orderId, status, completedBy);
        return ResponseEntity.ok(ApiResponse.ok(order));
    }

    /**
     * 오더 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        log.info("오더 삭제 요청: orderId={}", orderId);
        medicalOrderService.deleteOrder(orderId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * 오더의 약품 상세 정보 조회
     */
    @GetMapping("/{orderId}/drug-detail")
    public ResponseEntity<ApiResponse<com.mediflow.emr.dto.DrugDetailInfo>> getOrderDrugDetail(
            @PathVariable Long orderId) {
        log.info("오더 약품 상세 정보 조회 요청: orderId={}", orderId);
        com.mediflow.emr.dto.DrugDetailInfo drugDetail = medicalOrderService.getOrderDrugDetail(orderId);
        return ResponseEntity.ok(ApiResponse.ok(drugDetail));
    }
}
