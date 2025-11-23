package com.mediflow.emr.service;

import com.mediflow.emr.dto.UserResponseDto;
import com.mediflow.emr.entity.EmailVerification;
import com.mediflow.emr.exception.BusinessException;
import com.mediflow.emr.exception.ErrorCode;
import com.mediflow.emr.repository.EmailVerificationRepository;
import com.mediflow.emr.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 사용자 관련 비즈니스 로직 처리 서비스
 * - 현재 인증된 사용자 정보 조회 기능 제공
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    // 조회된 providerId로 현재 사용자 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto findMeByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId)
                .map(UserResponseDto::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // 메일 발송인의 정보
    @Value("${spring.mail.username}")
    private String mailHost;

    // 이메일 발송을 위한 의존객체
    private final JavaMailSender mailSender;

    // 이메일 중복확인 처리
    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {

        // 중복확인
        boolean flag = userRepository.existsByEmail(email);

        // 사용가능한 이메일인 경우 인증메일 발송
        if (!flag) sendVerificationEmail(email);

        return flag;
    }

    // 이메일 인증코드 발송 로직
    private void sendVerificationEmail(String email) {

        // 인증코드 생성
        String code = generateCode();

        // 만료시간 설정 (5분)
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5);

        // 기존 인증 정보가 있다면 삭제
        if (emailVerificationRepository.existsByEmail(email)) {
            emailVerificationRepository.deleteByEmail(email);
        }

        // 인증 정보 저장
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .verificationCode(code)
                .expiryDate(expiryDate)
                .build();
        emailVerificationRepository.save(verification);

        // 메일 전송 로직
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper
                    = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 누구에게 이메일을 보낼지
            messageHelper.setTo(email);

            // 누가 보내는 건지
            messageHelper.setFrom(mailHost);

            // 이메일 제목 설정
            messageHelper.setSubject("[인증메일] MediFlow 가입 인증 메일입니다.");
            // 이메일 내용 설정
            messageHelper.setText(
                    "인증 코드: <b style=\"font-weight: 700; letter-spacing: 5px; font-size: 30px;\">" + code + "</b>"
                    , true
            );

            // 메일 보내기
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("메일 발송에 실패했습니다.");
        }

    }

    // 무작위로 1000~9999 사이의 랜덤 숫자를 생성
    private String generateCode() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    /**
     * 이메일 인증 코드 검증
     * @param email 이메일
     * @param code 인증 코드
     * @return 인증 성공 여부
     */
    public boolean verifyEmailCode(String email, String code) {
        EmailVerification verification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_NOT_FOUND));

        if (verification.isExpired()) {
            throw new BusinessException(ErrorCode.VERIFICATION_EXPIRED);
        }

        if (!verification.isValid(code)) {
            throw new BusinessException(ErrorCode.VERIFICATION_INVALID);
        }

        verification.verify();
        return true;
    }
}