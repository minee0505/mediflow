package com.mediflow.emr.repository;

import com.mediflow.emr.entity.MedicalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, Long> {
    List<MedicalOrder> findByPatientIdOrderByOrderedAtDesc(Long patientId);
    List<MedicalOrder> findByPatientIdAndStatusOrderByOrderedAtDesc(Long patientId, String status);
}
