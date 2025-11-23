package com.mediflow.emr.service;

import com.mediflow.emr.dto.UserResponseDto;
import com.mediflow.emr.entity.User;
import com.mediflow.emr.exception.BusinessException;
import com.mediflow.emr.exception.ErrorCode;
import com.mediflow.emr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 관련 비즈니스 로직 처리 서비스
 * - OAuth2 사용자 정보 조회
 * - 사용자 프로필 관리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UserRepository userRepository;

    /**
     * providerId로 현재 사용자 정보 조회 (OAuth2 전용)
     *
     * @param providerId OAuth2 제공자의 사용자 ID
     * @return 사용자 정보 DTO
     */
    @Transactional(readOnly = true)
    public UserResponseDto findMeByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId)
                .map(UserResponseDto::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // 향후 추가될 메서드:
    // - updateUserProfile()
    // - changePassword()
    // - deleteUser()
    // 등 사용자 관리 기능
}

