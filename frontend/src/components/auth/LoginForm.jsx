import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { EmailAuthService } from '../../services/authService';
import { useAuthStore } from '../../stores/authStore';
import styles from './LoginForm.module.scss';

const LoginForm = () => {
    const navigate = useNavigate();
    const { setAuth } = useAuthStore();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');

        try {
            console.log('로그인 시도:', { email });
            const response = await EmailAuthService.login(email, password);
            console.log('로그인 성공:', response.data);

            /**
             * 쿠키 기반 인증 처리
             *
             * [변경 전] 로컬스토리지 방식:
             * setAuth(response.data.accessToken, response.data.refreshToken, user);
             * - 백엔드에서 받은 JWT 토큰을 직접 로컬스토리지에 저장
             * - XSS 공격에 취약 (JavaScript로 토큰 접근 가능)
             *
             * [변경 후] 쿠키 기반 방식 (현재):
             * setAuth(null, null, user);
             * - JWT 토큰은 백엔드에서 HTTP-Only 쿠키로 자동 설정됨
             * - 프론트엔드는 사용자 정보만 저장 (토큰 관리 불필요)
             * - accessToken, refreshToken을 null로 전달하는 이유:
             *   1. authStore의 setAuth 함수 시그니처 호환성 유지
             *   2. 실제로는 토큰을 사용하지 않고 무시됨
             *   3. 사용자 정보(user)만 스토어에 저장됨
             *
             * 쿠키 처리:
             * - 백엔드 로그인 API 응답 시 Set-Cookie 헤더로 토큰 설정
             * - 이후 모든 API 요청에 쿠키가 자동으로 포함됨
             * - withCredentials: true 설정으로 쿠키 전송 활성화
             */
            setAuth(null, null, {
                email: response.data.email,
                role: response.data.role,
            });

            // 로그인 성공 시 앱으로 이동
            navigate('/app');
        } catch (error) {
            console.error('로그인 실패:', error);
            console.error('에러 응답:', error.response);

            let errorMessage = '로그인에 실패했습니다.';

            if (error.response?.status === 401) {
                errorMessage = '이메일 또는 비밀번호가 올바르지 않습니다.';
            } else if (error.response?.status === 400) {
                errorMessage = '이메일 인증을 완료해주세요.';
            } else if (error.response?.data?.message) {
                errorMessage = error.response.data.message;
            }

            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>
            <form onSubmit={handleSubmit} className={styles.loginForm}>
                <h2 className={styles.title}>
                    이메일로 로그인
                </h2>

                {error && (
                    <div className={styles.errorMessage}>
                        {error}
                    </div>
                )}

                <div className={styles.formGroup}>
                    <label
                        htmlFor='email'
                        className={styles.label}
                    >
                        이메일
                    </label>
                    <input
                        id='email'
                        type='email'
                        name='email'
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={isLoading}
                        className={styles.input}
                        placeholder="example@email.com"
                    />
                </div>

                <div className={styles.formGroup}>
                    <label
                        htmlFor='password'
                        className={styles.label}
                    >
                        비밀번호
                    </label>
                    <input
                        id='password'
                        type='password'
                        name='password'
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        disabled={isLoading}
                        className={styles.input}
                        placeholder="비밀번호를 입력하세요"
                    />
                </div>

                <button
                    type='submit'
                    disabled={isLoading}
                    className={styles.submitButton}
                >
                    {isLoading ? '로그인 중...' : '로그인'}
                </button>

                <div className={styles.divider}>
                    <Link
                        to='/sign-up'
                        className={styles.signupLink}
                    >
                        회원이 아니십니까? 회원가입을 해보세요
                    </Link>
                </div>

                <Link
                    to='/'
                    className={styles.backLink}
                >
                    ← 뒤로 가기
                </Link>
            </form>
        </>
    );
};

export default LoginForm;