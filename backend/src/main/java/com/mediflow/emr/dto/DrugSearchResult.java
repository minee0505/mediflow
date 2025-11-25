package com.mediflow.emr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 약품 검색 결과 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugSearchResult {
    private String itemSeq;    // 품목기준코드
    private String itemName;   // 약품명
    private String entpName;   // 제조사명
}
