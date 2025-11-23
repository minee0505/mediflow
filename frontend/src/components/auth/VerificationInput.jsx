import { useRef, useEffect, useState } from 'react';
import styles from './SignUpForm.module.scss';
import { EmailAuthService } from '../../services/authService';

const VerificationInput = ({ email }) => {
    // 완성된 인증코드를 상태관리
    const [codes, setCodes] = useState([]);
    // 에러 메시지 상태 관리
    const [error, setError] = useState('');
    // 남은 시간 상태 관리 (초 단위)
    const [remainingSeconds, setRemainingSeconds] = useState(0);

    // ref를 배열로 관리하는 법
    const inputRefs = useRef([]);

    // 수동으로 ref배열에 input태그들 저장하기
    const bindRef = ($input) => {
        if ($input && !inputRefs.current.includes($input)) {
            inputRefs.current.push($input);
        }
    };

    useEffect(() => {
        // 맨 첫번째 칸에 포커싱
        if (inputRefs.current[0]) {
            inputRefs.current[0].focus();
        }

        // 초기 남은 시간 조회
        if (email) {
            fetchRemainingTime();
        }
    }, [email]);

    // 남은 시간 조회 함수
    const fetchRemainingTime = async () => {
        try {
            const response = await EmailAuthService.getRemainingTime(email);
            const seconds = response.data.remainingSeconds;
            setRemainingSeconds(seconds);
        } catch (error) {
            console.error('남은 시간 조회 실패:', error);
        }
    };

    // 타이머 설정
    useEffect(() => {
        if (remainingSeconds <= 0) return;

        const timer = setInterval(() => {
            setRemainingSeconds((prev) => {
                if (prev <= 1) {
                    clearInterval(timer);
                    return 0;
                }
                return prev - 1;
            });
        }, 1000);

        return () => clearInterval(timer);
    }, [remainingSeconds]);

    // 초를 분:초 형식으로 변환
    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${minutes}:${secs.toString().padStart(2, '0')}`;
    };

    // 다음 입력 칸으로 포커스 이동
    const focusNextInput = (index) => {
        // 인덱스 검증 - 마지막 칸에서는 포커스 이동대신 블러처리
        if (index < inputRefs.current.length) {
            // 한글자가 입력되면 포커스를 다음 칸으로 이동
            inputRefs.current[index].focus();
        } else {
            // 포커스 아웃
            inputRefs.current[index - 1].blur();
        }
    };

    // 숫자 입력 이벤트
    const handleNumber = (index, e) => {
        const inputValue = e.target.value;

        // 숫자가 아닌 경우 검증
        if (inputValue && !/^\d$/.test(inputValue)) {
            setError('숫자만 입력해주세요.');
            e.target.value = ''; // 입력값 초기화
            return;
        }

        // 에러 메시지 초기화
        setError('');

        // 입력한 숫자를 하나로 연결하기
        const copyCodes = [...codes];
        copyCodes[index] = inputValue;

        setCodes(copyCodes);

        // 숫자가 입력된 경우에만 다음 칸으로 이동
        if (inputValue) {
            focusNextInput(index + 1);
        }
    };

    return (
        <>
            <p className={styles.infoText}>Step 2: 이메일로 전송된 인증번호 4자리를 입력해주세요.</p>
            {remainingSeconds > 0 && (
                <p className={styles.timerText}>
                    남은 시간: <span className={styles.timer}>{formatTime(remainingSeconds)}</span>
                </p>
            )}
            {remainingSeconds === 0 && email && (
                <p className={styles.expiredText}>인증 시간이 만료되었습니다. 다시 시도해주세요.</p>
            )}
            {error && <p className={styles.errorMessage}>{error}</p>}
            <div className={styles.codeInputContainer}>
                {Array.from(new Array(4)).map((_, index) => (
                    <input
                        ref={bindRef}
                        key={index}
                        type='text'
                        inputMode='numeric'
                        pattern='[0-9]'
                        className={styles.codeInput}
                        maxLength={1}
                        onChange={(e) => handleNumber(index, e)}
                        disabled={remainingSeconds === 0}
                    />
                ))}
            </div>
        </>
    );
};

export default VerificationInput;