import { useState, useEffect } from 'react';
import apiClient from '../../services/apiClient';
import styles from './MedicationTab.module.scss';

/**
 * 투약 기록 탭 컴포넌트
 */
const MedicationTab = ({ patientId }) => {
  const [medications, setMedications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingMedicationId, setEditingMedicationId] = useState(null);

  // 약품 검색 관련
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [showSearchResults, setShowSearchResults] = useState(false);

  // 투약 경로 옵션
  const routes = [
    { value: 'PO', label: '경구 (PO)' },
    { value: 'IV', label: '정맥 (IV)' },
    { value: 'IM', label: '근육 (IM)' },
    { value: 'SC', label: '피하 (SC)' },
    { value: 'TOPICAL', label: '국소' },
    { value: 'INHALATION', label: '흡입' },
  ];

  // 현재 시간
  const getCurrentDateTime = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  };

  // 투약 입력 폼 상태
  const [formData, setFormData] = useState({
    drugName: '',
    drugCode: '',
    dose: '',
    route: 'PO',
    frequency: '',
    administeredAt: '',
    orderDoctor: '',
  });

  // 투약 목록 조회
  const fetchMedications = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get(`/medications/patient/${patientId}`);
      setMedications(response.data.data || []);
    } catch (err) {
      console.error('투약 목록 조회 실패:', err);
      setError('투약 목록을 불러올 수 없습니다');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (patientId) {
      fetchMedications();
    }
  }, [patientId]);

  // 약품 검색
  const handleSearchDrug = async (keyword) => {
    setSearchKeyword(keyword);
    
    if (keyword.length < 2) {
      setSearchResults([]);
      setShowSearchResults(false);
      return;
    }

    try {
      const response = await apiClient.get(`/medications/drugs/search?keyword=${keyword}`);
      setSearchResults(response.data.data || []);
      setShowSearchResults(true);
    } catch (err) {
      console.error('약품 검색 실패:', err);
      setSearchResults([]);
    }
  };

  // 약품 선택
  const handleSelectDrug = (drug) => {
    const drugName = typeof drug === 'string' ? drug : drug.itemName;
    const drugCode = typeof drug === 'string' ? '' : drug.itemSeq;
    setFormData(prev => ({ 
      ...prev, 
      drugName,
      drugCode 
    }));
    setSearchKeyword(drugName);
    setShowSearchResults(false);
  };

  // 입력값 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name === 'drugName') {
      handleSearchDrug(value);
    }
    
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  // 투약 등록/수정
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!formData.drugName.trim()) {
      setError('약물명을 입력해주세요');
      return;
    }

    try {
      const payload = {
        patientId,
        drugName: formData.drugName,
        drugCode: formData.drugCode || null,
        dose: formData.dose || null,
        route: formData.route,
        frequency: formData.frequency || null,
        administeredAt: formData.administeredAt || null,
        orderDoctor: formData.orderDoctor || null,
      };

      if (editingMedicationId) {
        await apiClient.put(`/medications/${editingMedicationId}`, payload);
      } else {
        await apiClient.post('/medications', payload);
      }
      
      resetForm();
      fetchMedications();
    } catch (err) {
      console.error('투약 저장 실패:', err);
      setError(err.response?.data?.message || '투약 저장에 실패했습니다');
    }
  };

  // 폼 초기화
  const resetForm = () => {
    setFormData({
      drugName: '',
      drugCode: '',
      dose: '',
      route: 'PO',
      frequency: '',
      administeredAt: '',
      orderDoctor: '',
    });
    setSearchKeyword('');
    setSearchResults([]);
    setShowSearchResults(false);
    setEditingMedicationId(null);
    setShowForm(false);
  };

  // 수정 모드로 전환
  const handleEdit = (medication) => {
    setFormData({
      drugName: medication.drugName || '',
      drugCode: medication.drugCode || '',
      dose: medication.dose || '',
      route: medication.route || 'PO',
      frequency: medication.frequency || '',
      administeredAt: medication.administeredAt ? medication.administeredAt.slice(0, 16) : '',
      orderDoctor: medication.orderDoctor || '',
    });
    setSearchKeyword(medication.drugName || '');
    setEditingMedicationId(medication.id);
    setShowForm(true);
  };

  // 시간 포맷
  const formatTime = (dateTime) => {
    const date = new Date(dateTime);
    return date.toLocaleString('ko-KR', {
      timeZone: 'Asia/Seoul',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
  };

  if (loading && medications.length === 0) {
    return <div className={styles.loading}>투약 정보를 불러오는 중...</div>;
  }

  return (
    <div className={styles.medicationTab}>
      <div className={styles.header}>
        <h3>투약 기록</h3>
        <button 
          className={styles.addButton}
          onClick={() => {
            if (showForm) {
              resetForm();
            } else {
              setShowForm(true);
            }
          }}
        >
          {showForm ? '취소' : '+ 투약 등록'}
        </button>
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {/* 투약 입력 폼 */}
      {showForm && (
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label>약물명 *</label>
              <div className={styles.searchContainer}>
                <input
                  type="text"
                  name="drugName"
                  value={formData.drugName}
                  onChange={handleChange}
                  placeholder="약물명을 입력하세요"
                  autoComplete="off"
                  required
                />
                {showSearchResults && searchResults.length > 0 && (
                  <div className={styles.searchResults}>
                    {searchResults.map((drug, index) => (
                      <div
                        key={index}
                        className={styles.searchResultItem}
                        onClick={() => handleSelectDrug(drug)}
                      >
                        <div className={styles.drugName}>
                          {typeof drug === 'string' ? drug : drug.itemName}
                        </div>
                        {typeof drug === 'object' && drug.entpName && (
                          <div className={styles.drugCompany}>
                            {drug.entpName}
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>

            <div className={styles.formGroup}>
              <label>용량</label>
              <input
                type="text"
                name="dose"
                value={formData.dose}
                onChange={handleChange}
                placeholder="예: 500mg, 1정"
              />
            </div>
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label>투약 경로 *</label>
              <select
                name="route"
                value={formData.route}
                onChange={handleChange}
                required
              >
                {routes.map(route => (
                  <option key={route.value} value={route.value}>
                    {route.label}
                  </option>
                ))}
              </select>
            </div>

            <div className={styles.formGroup}>
              <label>투약 빈도</label>
              <input
                type="text"
                name="frequency"
                value={formData.frequency}
                onChange={handleChange}
                placeholder="예: 1일 3회, BID, TID"
              />
            </div>
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label>투약 시간</label>
              <input
                type="datetime-local"
                name="administeredAt"
                value={formData.administeredAt}
                onChange={handleChange}
                max={getCurrentDateTime()}
              />
              <small>미입력 시 현재 시간</small>
            </div>

            <div className={styles.formGroup}>
              <label>처방 의사</label>
              <input
                type="text"
                name="orderDoctor"
                value={formData.orderDoctor}
                onChange={handleChange}
                placeholder="의사명"
              />
            </div>
          </div>

          <div className={styles.formActions}>
            <button type="submit" className={styles.submitButton}>
              {editingMedicationId ? '수정' : '등록'}
            </button>
            {editingMedicationId && (
              <button 
                type="button" 
                className={styles.cancelButton}
                onClick={resetForm}
              >
                취소
              </button>
            )}
          </div>
        </form>
      )}

      {/* 투약 이력 목록 */}
      <div className={styles.medicationList}>
        {medications.length === 0 ? (
          <div className={styles.empty}>등록된 투약 기록이 없습니다</div>
        ) : (
          medications.map((medication) => (
            <div key={medication.id} className={styles.medicationItem}>
              <div className={styles.medicationHeader}>
                <div className={styles.medicationHeaderLeft}>
                  <span className={styles.drugName}>{medication.drugName}</span>
                  <span className={styles.route}>{routes.find(r => r.value === medication.route)?.label || medication.route}</span>
                </div>
                <div className={styles.medicationHeaderRight}>
                  <span className={styles.time}>{formatTime(medication.administeredAt)}</span>
                  <span className={styles.nurse}>{medication.nurseName}</span>
                  {medication.canEdit && (
                    <button 
                      className={styles.editButton}
                      onClick={() => handleEdit(medication)}
                      title="투약 수정"
                    >
                      수정
                    </button>
                  )}
                </div>
              </div>

              <div className={styles.medicationDetails}>
                {medication.dose && (
                  <div className={styles.detailItem}>
                    <span className={styles.detailLabel}>용량</span>
                    <span className={styles.detailValue}>{medication.dose}</span>
                  </div>
                )}
                {medication.frequency && (
                  <div className={styles.detailItem}>
                    <span className={styles.detailLabel}>빈도</span>
                    <span className={styles.detailValue}>{medication.frequency}</span>
                  </div>
                )}
                {medication.orderDoctor && (
                  <div className={styles.detailItem}>
                    <span className={styles.detailLabel}>처방의</span>
                    <span className={styles.detailValue}>{medication.orderDoctor}</span>
                  </div>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default MedicationTab;
