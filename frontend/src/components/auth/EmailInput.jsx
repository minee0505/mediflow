import { useRef, useState, useEffect } from 'react';
import styles from './EmailInput.module.scss';

const EmailInput = () => {
    const emailRef = useRef();

    // 에러 상태메시지를 관리
    const [error, setError] = useState('');
    const [email, setEmail] = useState('');

    // 화면이 렌더링되자마자 입력창에 포커싱
    useEffect(() => {
        emailRef.current.focus();
    }, []);

    // 이메일 입력 이벤트
    const handleEmail = (e) => {
        const inputValue = e.target.value;
        setEmail(inputValue);

        // 입력값이 없으면 에러 메시지 초기화
        if (!inputValue) {
            setError('');
            return;
        }

        // 이메일 패턴 검증
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 간단한 이메일 패턴 검사
        if (!emailPattern.test(inputValue)) {
            setError('이메일이 올바르지 않습니다.');
            return;
        }
        setError('');
    };

    // 인증 코드 요청 핸들러
    const handleSubmit = () => {
        if (!email) {
            setError('이메일을 입력해주세요.');
            return;
        }

        if (error) {
            return;
        }

        // TODO: 인증 코드 요청 API 호출
        console.log('인증 코드 요청:', email);
    };

    return (
        <div className={styles.container}>
            <div className={styles.infoBox}>
                <span className={styles.label}>Step 1:</span> 유효한 이메일을 입력해주세요.
            </div>
            
            <div className={styles.formGroup}>
                <label htmlFor='email'>이메일 주소</label>
                <input
                    ref={emailRef}
                    id='email'
                    className={error ? styles.invalidInput : ''}
                    type='email'
                    name='email'
                    placeholder='example@email.com'
                    value={email}
                    onChange={handleEmail}
                />
                {error && <p className={styles.errorMessage}>{error}</p>}
            </div>
            
            <button
                type='button'
                className={styles.submitButton}
                onClick={handleSubmit}
                disabled={!email || !!error}
            >
                인증 코드 받기
            </button>
        </div>
    );
};

export default EmailInput;