import { useRef, useEffect } from 'react';
import styles from './SignUpForm.module.scss';

const VerificationInput = () => {
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
    }, []);

    // 숫자 입력 이벤트
    const handleNumber = (index) => {
        // 인덱스 검증 - 마지막 칸에서는 포커스 이동대신 블러처리
        if (index < inputRefs.current.length) {
            // 한글자가 입력되면 포커스를 다음 칸으로 이동
            inputRefs.current[index].focus();
        } else {
            // 포커스 아웃
            inputRefs.current[index - 1].blur();
        }
    };

    return (
        <>
            <p className={styles.infoText}>Step 2: 이메일로 전송된 인증번호 4자리를 입력해주세요.</p>
            <div className={styles.codeInputContainer}>
                {Array.from(new Array(4)).map((_, index) => (
                    <input
                        ref={bindRef}
                        key={index}
                        type='text'
                        className={styles.codeInput}
                        maxLength={1}
                        onChange={() => handleNumber(index + 1)}
                    />
                ))}
            </div>
        </>
    );
};

export default VerificationInput;