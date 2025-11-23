import { useState } from 'react';
import { Link } from 'react-router-dom';
import styles from './SignUpForm.module.scss';
import EmailInput from './EmailInput.jsx';
import VerificationInput from './VerificationInput.jsx';
import PasswordInput from './PasswordInput.jsx';

const SignUpForm = () => {
    // 현재 어떤 스텝인지 확인
    const [step, setStep] = useState(1);
    // 이메일 상태 관리
    const [email, setEmail] = useState('');
    // 비밀번호 상태 관리
    const [password, setPassword] = useState('');
    const [isPasswordValid, setIsPasswordValid] = useState(false);

    // 이메일 중복확인이 끝날때 호출될 함수
    const emailSuccessHandler = (verifiedEmail) => {
        setEmail(verifiedEmail);
        setStep(2);
    };

    // 인증 코드 검증이 끝날때 호출될 함수
    const verificationSuccessHandler = () => {
        setStep(3);
    };

    // 비밀번호 변경 핸들러
    const passwordChangeHandler = (newPassword, isValid) => {
        setPassword(newPassword);
        setIsPasswordValid(isValid);
    };

    return (
        <div className={styles.signupForm}>
            <div className={styles.formStepActive}>
                {step === 1 && <EmailInput onSuccess={emailSuccessHandler} />}
                {step === 2 && <VerificationInput email={email} onSuccess={verificationSuccessHandler} />}
                {step === 3 && <PasswordInput onPasswordChange={passwordChangeHandler} />}
            </div>

            <div className="text-center mt-6">
                <Link
                    to='/'
                    className="text-gray-500 hover:text-gray-700 text-sm"
                >
                    ← 뒤로 가기
                </Link>
            </div>
        </div>
    );
};

export default SignUpForm;