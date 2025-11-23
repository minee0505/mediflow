import { useRef, useEffect, useState } from 'react';
import { debounce } from 'lodash';
import styles from './SignUpForm.module.scss';
import { EmailAuthService } from '../../services/authService';

const VerificationInput = ({ email }) => {
    // 완성된 인증코드를 상태관리
    const [codes, setCodes] = useState(['', '', '', '']);
    // 에러 메시지 상태 관리
    const [error, setError] = useState('');
    // 남은 시간 상태 관리 (초 단위)
    const [remainingSeconds, setRemainingSeconds] = useState(0);

    // ref를 배열로 관리하는 법
    const inputRefs = useRef([]);

    // 수동으로 ref배열에 input태그들 저장하기
    const bindRef = ($input, index) => {
        inputRefs.current[index] = $input;
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
            // 서버 에러 시에도 기본값을 5분(300초)으로 설정
            setRemainingSeconds(300);
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

    // 서버에 인증코드 전송
    const fetchVerifying = debounce(async (verifyCode) => {
        try {
            const response = await EmailAuthService.verifyCode(email, verifyCode);
            const { isMatch } = response.data;

            // 검증에 실패했을 경우
            if (!isMatch) {
                // 에러메시지를 세팅
                setError('유효하지 않거나 만료된 인증코드입니다. 인증코드를 재발송합니다.');
                // 인증코드를 모두 빈칸으로 되돌림
                setCodes(['', '', '', '']);
                // input 값들도 초기화
                inputRefs.current.forEach((input) => {
                    if (input) input.value = '';
                });
                // 첫번째 칸으로 재 포커싱
                inputRefs.current[0].focus();
                return;
            }

            // 검증 성공시
            setError('');
            console.log('인증 성공!');
            // TODO: 다음 스텝으로 이동하는 로직 추가

        } catch (error) {
            console.error('인증 코드 검증 실패:', error);
            setError('인증 코드 검증 중 오류가 발생했습니다.');
        }
    }, 1000);

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

        // 모든 인증코드를 입력했을 때 서버에 인증코드를 전송
        if (copyCodes.every((code) => code !== '')) {
            console.log('모든 칸이 입력됨! ', copyCodes);
            const verifyCode = copyCodes.join('');
            console.log('전송할 인증코드: ', verifyCode);

            // 서버에 전송
            fetchVerifying(verifyCode);
        }
    };

    // 붙여넣기 이벤트 처리
    const handlePaste = (index, e) => {
        e.preventDefault();

        // 붙여넣은 텍스트 가져오기
        const pastedText = e.clipboardData.getData('text');

        // 숫자만 추출
        const numbers = pastedText.replace(/\D/g, '');

        if (numbers.length === 0) {
            setError('숫자만 입력해주세요.');
            return;
        }

        // 에러 메시지 초기화
        setError('');

        // 각 칸에 숫자 입력
        const copyCodes = [...codes];
        const availableSlots = 4 - index; // 현재 칸부터 사용 가능한 칸 수
        const numbersToFill = numbers.slice(0, availableSlots);

        for (let i = 0; i < numbersToFill.length; i++) {
            copyCodes[index + i] = numbersToFill[i];
            if (inputRefs.current[index + i]) {
                inputRefs.current[index + i].value = numbersToFill[i];
            }
        }

        setCodes(copyCodes);

        // 마지막으로 입력된 칸의 다음 칸으로 포커스 이동
        const lastFilledIndex = index + numbersToFill.length - 1;
        if (lastFilledIndex < 3) {
            focusNextInput(lastFilledIndex + 1);
        } else {
            inputRefs.current[lastFilledIndex].blur();
        }

        // 모든 인증코드를 입력했을 때 서버에 인증코드를 전송
        if (copyCodes.every((code) => code !== '')) {
            console.log('모든 칸이 입력됨! ', copyCodes);
            const verifyCode = copyCodes.join('');
            console.log('전송할 인증코드: ', verifyCode);

            // 서버에 전송
            fetchVerifying(verifyCode);
        }
    };

    // 키보드 이벤트 처리 (화살표, 백스페이스)
    const handleKeyDown = (index, e) => {
        // 왼쪽 화살표: 이전 칸으로 이동
        if (e.key === 'ArrowLeft' && index > 0) {
            e.preventDefault();
            inputRefs.current[index - 1].focus();
        }
        // 오른쪽 화살표: 다음 칸으로 이동
        else if (e.key === 'ArrowRight' && index < 3) {
            e.preventDefault();
            inputRefs.current[index + 1].focus();
        }
        // 백스페이스: 현재 칸이 비어있으면 이전 칸으로 이동하고 삭제
        else if (e.key === 'Backspace' && !e.target.value && index > 0) {
            e.preventDefault();
            const copyCodes = [...codes];
            copyCodes[index - 1] = '';
            setCodes(copyCodes);
            inputRefs.current[index - 1].value = '';
            inputRefs.current[index - 1].focus();
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
                        ref={($input) => bindRef($input, index)}
                        key={index}
                        type='text'
                        inputMode='numeric'
                        pattern='[0-9]'
                        className={styles.codeInput}
                        maxLength={1}
                        onChange={(e) => handleNumber(index, e)}
                        onPaste={(e) => handlePaste(index, e)}
                        onKeyDown={(e) => handleKeyDown(index, e)}
                        disabled={remainingSeconds === 0}
                    />
                ))}
            </div>
        </>
    );
};

export default VerificationInput;