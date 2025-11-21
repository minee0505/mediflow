package com.mediflow.emr.config;


import com.mediflow.emr.util.AppProperties;
import com.mediflow.emr.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 로그인 실패 시 실행되는 핸들러.
 *
 * 동작 순서
 * 1) 실패 사유를 로깅한다
 * 2) 기존에 설정된 Access/Refresh 토큰 쿠키를 삭제한다
 * 3) 실패 메시지를 쿼리 파라미터로 담아 프론트엔드 애플리케이션 URL로 리다이렉트한다
 *
 * 보안 포인트
 * - 토큰 쿠키를 삭제하여, 실패 후에도 이전 토큰이 남아있지 않도록 한다
 * - 실패 메시지는 URL 쿼리로 전달하나, 민감 정보는 포함하지 않는다
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final CookieUtil cookieUtil;
    private final AppProperties appProperties;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String rawMessage = exception.getMessage() == null ? "oauth2_login_failed" : exception.getMessage();
        log.warn("OAuth2 authentication failed: {}", rawMessage);

        // 기존 토큰 쿠키 삭제
        cookieUtil.deleteAccessTokenCookie(response);
        cookieUtil.deleteRefreshTokenCookie(response);

        // 실패 메시지를 쿼리 파라미터로 인코딩하여 리다이렉트
        String encodedMsg = URLEncoder.encode(rawMessage, StandardCharsets.UTF_8);
        // 리다이렉트 URL 구성
        String baseUrl = appProperties.getOauth2FailureRedirectUrl();
        // 쿼리 파라미터가 이미 있는지 확인 후 적절히 추가
        String redirect = baseUrl.contains("?") ? baseUrl + "&error=" + encodedMsg : baseUrl + "?error=" + encodedMsg;
        response.sendRedirect(redirect);
    }
}

