package com.mediflow.emr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 식약처 API 응답 DTO
 * 실제 응답 구조: { "header": {...}, "body": {...} }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugApiResponse {
    private Header header;
    private Body body;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Integer pageNo;
        private Integer totalCount;
        private Integer numOfRows;
        @JsonProperty("items")
        private List<DrugItem> items;

        // API가 단일 아이템을 반환할 때를 대비
        @JsonProperty("item")
        private List<DrugItem> item;

        public List<DrugItem> getItems() {
            // items가 null이면 item을 반환
            return items != null ? items : item;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DrugItem {
        private String itemSeq;        // 품목기준코드
        private String itemName;       // 약품명
        private String entpName;       // 제조사명
        private String efcyQesitm;     // 효능·효과
        private String useMethodQesitm; // 사용법
        private String atpnWarnQesitm; // 주의사항 경고
        private String atpnQesitm;     // 주의사항
        private String intrcQesitm;    // 상호작용
        private String seQesitm;       // 부작용
        private String depositMethodQesitm; // 보관방법
    }
}
