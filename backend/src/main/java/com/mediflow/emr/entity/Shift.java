package com.mediflow.emr.entity;

import com.mediflow.emr.entity.enums.ShiftType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 근무조 엔티티
 * 주간/초번/야간 근무 스케줄 정보
 */
@Getter
@Entity
@Table(name = "shift")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long id;

    /** 근무 날짜 */
    @Column(nullable = false)
    private LocalDate date;

    /** 근무조 유형 (DAY, EVENING, NIGHT) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShiftType type;

    /** 근무 시작 시간 */
    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    /** 근무 종료 시간 */
    @Column(nullable = false, name = "end_time")
    private LocalTime endTime;
}
