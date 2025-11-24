import React, { useEffect } from 'react';
import useDashboardStore from '../stores/useDashboardStore';
import PatientCard from '../components/dashboard/PatientCard';
import PatientDetail from '../components/dashboard/PatientDetail';
import styles from './DashboardPage.module.scss';

/**
 * 메인 대시보드 페이지
 */
const DashboardPage = () => {
  const {
    myPatients,
    selectedPatient,
    departmentSummary,
    loading,
    error,
    fetchMyPatients,
    fetchDepartmentSummary,
    selectPatient,
  } = useDashboardStore();

  useEffect(() => {
    // 데이터 로드
    fetchMyPatients();
    fetchDepartmentSummary();
  }, [fetchMyPatients, fetchDepartmentSummary]);

  return (
    <div className={styles.dashboardPage}>
      {/* 좌측 패널 - 환자 목록 */}
      <div className={styles.leftPanel}>
          {/* 부서 요약 */}
          {departmentSummary && (
            <div className={styles.summary}>
              <div className={styles.summaryTitle}>부서 현황</div>
              <div className={styles.summaryStats}>
                <div className={styles.stat}>
                  <div className={styles.statLabel}>전체 환자</div>
                  <div className={styles.statValue}>
                    {departmentSummary.totalPatients}
                  </div>
                </div>
                <div className={styles.stat}>
                  <div className={styles.statLabel}>내 담당</div>
                  <div className={styles.statValue}>
                    {departmentSummary.myPatients}
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* 환자 목록 */}
          <div className={styles.patientList}>
            <div className={styles.listHeader}>
              <span>담당 환자</span>
              <span className={styles.count}>{myPatients.length}명</span>
            </div>

            {loading && <div className={styles.loading}>로딩중...</div>}

            {error && <div className={styles.error}>{error}</div>}

            {!loading && !error && myPatients.length === 0 && (
              <div className={styles.empty}>배정된 환자가 없습니다.</div>
            )}

            {!loading &&
              !error &&
              myPatients.map((patient) => (
                <PatientCard
                  key={patient.patientId}
                  patient={patient}
                  isSelected={selectedPatient?.patientId === patient.patientId}
                  onClick={() => selectPatient(patient)}
                />
              ))}
          </div>
        </div>

      {/* 우측 패널 - 환자 상세 */}
      <div className={styles.rightPanel}>
        <PatientDetail patient={selectedPatient} />
      </div>
    </div>
  );
};

export default DashboardPage;
