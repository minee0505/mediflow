import { useState } from 'react';
import styles from './SignUpForm.module.scss';
import EmailInput from './EmailInput.jsx';
import VerificationInput from './VerificationInput.jsx';

const SignUpForm = () => {
    // 현재 어떤 스텝인지 확인
    const [step, setStep] = useState(1);

    // 이메일 중복확인이 끝날때 호출될 함수
    const emailSuccessHandler = () => setStep(2);

    return (
        <div className={styles.signupForm}>
            <div className={styles.formStepActive}>
                {step === 1 && <EmailInput onSuccess={emailSuccessHandler} />}
                {step === 2 && <VerificationInput />}
            </div>
        </div>
    );
};

export default SignUpForm;