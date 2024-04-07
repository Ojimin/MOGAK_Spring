package com.mogak.spring.domain.jogak;

import com.mogak.spring.domain.mogak.Mogak;
import com.mogak.spring.domain.mogak.MogakCategory;
import com.mogak.spring.global.BaseEntity;
import com.mogak.spring.web.dto.jogakdto.JogakResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Getter
@Table(name = "daily_jogak")
@Entity
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyJogak extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_jogak_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mogak_id")
    private Mogak mogak;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogak_id")
    private Jogak jogak;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mogak_category")
    private MogakCategory category;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Boolean isAchievement;
    @Column(nullable = false)
    private Boolean isRoutine;

    public void updateAchievement(boolean state) {
        this.isAchievement = state;
    }

    public static JogakResponseDto.GetRoutineJogakDto getRoutineJogakDto(DailyJogak dailyJogak) {
        return JogakResponseDto.GetRoutineJogakDto.builder()
                .dailyJogakId(dailyJogak.getId())
                .date(dailyJogak.getCreatedAt().toLocalDate())
                .isAchievement(dailyJogak.getIsAchievement())
                .title(dailyJogak.getTitle())
                .build();
    }

    public static JogakResponseDto.GetRoutineJogakDto getFutureRoutineJogakDto(LocalDate date, String title) {
        return JogakResponseDto.GetRoutineJogakDto.builder()
                .dailyJogakId(-1L)
                .date(date)
                .isAchievement(false)
                .title(title)
                .build();
    }
}