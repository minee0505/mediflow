import apiClient from './apiClient';

/*
 인증 서비스:
 - me: 현재 로그인한 사용자 정보 조회
 - refresh: 액세스 토큰 재발급 요청 (이 호출은 인증 리프레시 로직을 건너뜀)
 - logout: 서버에 로그아웃 요청
*/
export const AuthService = {
    me: () => apiClient.get('/users/me'),
    refresh: () => apiClient.post('/auth/refresh', {}, { skipAuthRefresh: true }),
    logout: () => apiClient.post('/auth/logout'),
};