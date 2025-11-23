import styles from './SignUpForm.module.scss';
import EmailInput from './EmailInput.jsx';

const SignUpForm = () => {
    return (
        <div className={styles.signupForm}>
            <div className={styles.formStepActive}>
                <EmailInput />
            </div>
        </div>
    );
};

export default SignUpForm;