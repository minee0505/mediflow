import { useEffect } from 'react';
import styles from './SuccessModal.module.scss';

const SuccessModal = ({ message, onClose }) => {
    useEffect(() => {
        // 1초 후 자동으로 닫기
        const timer = setTimeout(() => {
            onClose();
        }, 1000);

        return () => clearTimeout(timer);
    }, [onClose]);

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <div className={styles.iconWrapper}>
                    <svg
                        className={styles.checkIcon}
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={2}
                            d="M5 13l4 4L19 7"
                        />
                    </svg>
                </div>
                <p className={styles.message}>{message}</p>
            </div>
        </div>
    );
};

export default SuccessModal;

