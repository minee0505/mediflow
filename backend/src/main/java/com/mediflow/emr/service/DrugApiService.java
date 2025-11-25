package com.mediflow.emr.service;

import com.mediflow.emr.dto.DrugApiResponse;
import com.mediflow.emr.dto.DrugSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 식약처 의약품 API 서비스
 */
@Slf4j
@Service
public class DrugApiService {

    @Value("${drug-api.base-url}")
    private String baseUrl;

    @Value("${drug-api.service-key}")
    private String serviceKey;

    private final RestClient restClient;

    public DrugApiService(@Value("${drug-api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 약품명으로 검색
     */
    public List<DrugSearchResult> searchDrugs(String keyword) {
        log.info("식약처 API 약품 검색 시작 - keyword: {}", keyword);

        try {
            // API 호출
            DrugApiResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getDrbEasyDrugList")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 20)
                            .queryParam("type", "json")
                            .queryParam("itemName", keyword)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), (request, responseStatus) -> {
                        log.error("식약처 API HTTP 오류 - status: {}", responseStatus.getStatusCode());
                    })
                    .body(DrugApiResponse.class);

            log.debug("API 응답: {}", response);

            // 응답 헤더 확인
            if (response != null && response.getHeader() != null) {
                String resultCode = response.getHeader().getResultCode();
                String resultMsg = response.getHeader().getResultMsg();
                log.debug("API 응답 코드: {}, 메시지: {}", resultCode, resultMsg);

                if (!"00".equals(resultCode)) {
                    log.error("식약처 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                    return new ArrayList<>();
                }
            }

            if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                log.warn("식약처 API 응답이 비어있습니다. response: {}", response);
                return new ArrayList<>();
            }

            // 결과 변환
            List<DrugSearchResult> results = response.getBody().getItems().stream()
                    .map(item -> DrugSearchResult.builder()
                            .itemSeq(item.getItemSeq())
                            .itemName(item.getItemName())
                            .entpName(item.getEntpName())
                            .build())
                    .collect(Collectors.toList());

            log.info("식약처 API 검색 성공: {} 건", results.size());
            return results;

        } catch (Exception e) {
            log.error("식약처 API 호출 실패 - keyword: {}, error: {}", keyword, e.getMessage(), e);
            // API 실패 시 빈 리스트 반환 (서비스 중단 방지)
            return new ArrayList<>();
        }
    }

    /**
     * 약품 상세 정보 조회 (약품명으로)
     */
    public com.mediflow.emr.dto.DrugDetailInfo getDrugDetail(String drugName) {
        log.info("식약처 API 약품 상세 조회 시작 - drugName: {}", drugName);

        try {
            // 약품명으로 검색
            DrugApiResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getDrbEasyDrugList")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 1)
                            .queryParam("type", "json")
                            .queryParam("itemName", drugName)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), (request, responseStatus) -> {
                        log.error("식약처 API HTTP 오류 - status: {}", responseStatus.getStatusCode());
                    })
                    .body(DrugApiResponse.class);

            log.debug("약품 상세 정보 API 응답: {}", response);

            // 응답 헤더 확인
            if (response != null && response.getHeader() != null) {
                String resultCode = response.getHeader().getResultCode();
                String resultMsg = response.getHeader().getResultMsg();
                log.debug("API 응답 코드: {}, 메시지: {}", resultCode, resultMsg);

                if (!"00".equals(resultCode)) {
                    log.error("식약처 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                    return null;
                }
            }

            if (response == null || response.getBody() == null || response.getBody().getItems() == null || response.getBody().getItems().isEmpty()) {
                log.warn("약품 상세 정보를 찾을 수 없습니다: {}", drugName);
                return null;
            }

            DrugApiResponse.DrugItem item = response.getBody().getItems().getFirst();
            log.info("약품 상세 정보 조회 성공 - drugName: {}, itemName: {}", drugName, item.getItemName());

            return com.mediflow.emr.dto.DrugDetailInfo.builder()
                    .itemSeq(item.getItemSeq())
                    .itemName(item.getItemName())
                    .entpName(item.getEntpName())
                    .efcyQesitm(item.getEfcyQesitm())
                    .useMethodQesitm(item.getUseMethodQesitm())
                    .atpnWarnQesitm(item.getAtpnWarnQesitm())
                    .atpnQesitm(item.getAtpnQesitm())
                    .intrcQesitm(item.getIntrcQesitm())
                    .seQesitm(item.getSeQesitm())
                    .depositMethodQesitm(item.getDepositMethodQesitm())
                    .build();

        } catch (Exception e) {
            log.error("약품 상세 정보 조회 실패 - drugName: {}, error: {}", drugName, e.getMessage(), e);
            return null;
        }
    }
}
