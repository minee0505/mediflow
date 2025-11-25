import { useState } from 'react';
import OrderTab from '../patient/OrderTab';
import NursingNoteTab from '../patient/NursingNoteTab';
import VitalTab from '../patient/VitalTab';
import IntakeOutputTab from '../patient/IntakeOutputTab';
import MedicationTab from '../patient/MedicationTab';
import styles from './PatientDetail.module.scss';

/**
 * 환자 상세 정보 컴포넌트
 */
const PatientDetail = ({ patient }) => {
  const [activeTab, setActiveTab] = useState('orders');

  if (!patient) {
    return (
      <div className={styles.patientDetail}>
        <div className={styles.emptyState}>
          <div className={styles.message}>환자를 선택해주세요</div>
        </div>
      </div>
    );
  }

  const tabs = [
    { id: 'orders', label: '오더조회' },
    { id: 'notes', label: '간호기록' },
    { id: 'vitals', label: '바이탈' },
    { id: 'io', label: '섭취배설' },
    { id: 'medications', label: '투약' },
    { id: 'images', label: '검사영상' },
  ];

  return (
    <div className={styles.patientDetail}>
      <div className={styles.header}>
        <div className={styles.patientName}>
          {patient.name} ({patient.age}세, {patient.gender === 'M' ? '남' : '여'})
        </div>
        <div className={styles.patientMeta}>
          {patient.chartNumber} | {patient.diagnosis}
        </div>
      </div>

      <div className={styles.tabs}>
        {tabs.map((tab) => (
          <button
            key={tab.id}
            className={`${styles.tab} ${activeTab === tab.id ? styles.active : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </div>

      <div className={styles.content}>
        {activeTab === 'orders' && <OrderTab patientId={patient.patientId} />}
        {activeTab === 'notes' && <NursingNoteTab patientId={patient.patientId} />}
        {activeTab === 'vitals' && <VitalTab patientId={patient.patientId} />}
        {activeTab === 'io' && <IntakeOutputTab patientId={patient.patientId} />}
        {activeTab === 'medications' && <MedicationTab patientId={patient.patientId} />}
        {activeTab === 'images' && (
          <div className={styles.comingSoon}>
            {tabs.find((t) => t.id === activeTab)?.label} 기능은 곧 추가됩니다.
          </div>
        )}
      </div>
    </div>
  );
};

export default PatientDetail;
