import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useDashboardStore from '../../stores/useDashboardStore';
import { useAuthStore } from '../../stores/authStore';
import { AuthService } from '../../services/authService';
import DatePicker from '../common/DatePicker';
import styles from './AppHeader.module.scss';

const AppHeader = () => {
    const navigate = useNavigate();
    const { departmentSummary } = useDashboardStore();
    const { clear } = useAuthStore();
    const [selectedDate, setSelectedDate] = useState(new Date());

    const handleLogout = async () => {
        try {
            await AuthService.logout();
        } catch (error) {
            console.error('로그아웃 실패:', error);
        } finally {
            clear();
            navigate('/');
        }
    };

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    return (
        <header className={styles.header}>
            <div className={styles.left}>
                <div className={styles.logo}>MediFlow</div>
                <div className={styles.hospitalInfo}>
                    <div className={styles.hospitalName}>서울대학교병원</div>
                    <div className={styles.departmentName}>
                        {departmentSummary?.departmentName || '부서 정보 로딩중...'}
                    </div>
                </div>
            </div>

            <div className={styles.right}>
                <div className={styles.userInfo}>
                    <div className={styles.nurseName}>
                        {departmentSummary?.nurseName || '사용자'}
                    </div>
                    <div className={styles.shiftInfo}>
                        {departmentSummary?.shiftType || '근무조'} 근무
                    </div>
                </div>
                <DatePicker selectedDate={selectedDate} onDateChange={handleDateChange} />
                <button className={styles.logoutBtn} onClick={handleLogout}>
                    로그아웃
                </button>
            </div>
        </header>
    );
};

export default AppHeader;
