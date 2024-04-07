package com.mogak.spring.domain.jogak;

import com.mogak.spring.domain.mogak.Mogak;
import com.mogak.spring.domain.mogak.MogakCategory;
import com.mogak.spring.domain.user.User;
import com.mogak.spring.global.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@Getter
@Table(name = "jogak")
@Entity
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jogak extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jogak_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mogak_id")
    private Mogak mogak;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mogak_category")
    private MogakCategory category;
    @Column(nullable = false)
    private String title;
    @OneToMany(mappedBy = "jogak")
    private List<JogakPeriod> jogakPeriods = new ArrayList<>();
    @OneToMany(mappedBy = "jogak")
    private List<DailyJogak> dailyJogaks = new ArrayList<>();
    @Column(nullable = false)
    private Boolean isRoutine;
    @Column(name = "achievement")
    private Integer achievements;
    @Column(name = "start_at")
    private LocalDate startAt;
    @Column(name = "end_at")
    private LocalDate endAt;
    private String state;

    public List<String> getPeriods() {
        return this.getJogakPeriods()
                .stream()
                .map(JogakPeriod::getPeriod)
                .map(Period::getDays)
                .collect(Collectors.toList());
    }

    public void update(String title, Boolean isRoutine, LocalDate endAt) {
        Optional.ofNullable(title).ifPresent(updateTitle -> this.title = updateTitle);
        Optional.ofNullable(isRoutine).ifPresent(routine -> this.isRoutine = routine);
        Optional.ofNullable(endAt).ifPresent(endDate -> {
            this.endAt = endDate;
            if (endAt.getYear() == 9999) {
                this.endAt = null;
            }
        });
    }

    public void updateCategory(MogakCategory category) {
        this.category = category;
    }

    public void updateState(JogakState state) {
        this.state = state.toString();
    }

    public void increaseAchievements() {
        this.achievements += 1;
    }

    public void decreaseAchievements() {
        this.achievements -= 1;
    }
}
