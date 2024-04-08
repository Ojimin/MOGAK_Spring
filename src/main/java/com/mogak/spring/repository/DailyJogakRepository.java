package com.mogak.spring.repository;

import com.mogak.spring.domain.jogak.DailyJogak;
import com.mogak.spring.domain.jogak.Jogak;
import com.mogak.spring.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyJogakRepository extends JpaRepository<DailyJogak, Long> {
    @Query("SELECT j FROM DailyJogak j WHERE j.createdAt BETWEEN :startDate AND :endDate")
    List<DailyJogak> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT j FROM DailyJogak j WHERE j.createdAt BETWEEN :startDateTime AND :endDateTime AND j.jogak = :jogak")
    Optional<DailyJogak> findByCreatedAtBetweenAndId(@Param("startDateTime") LocalDateTime startDateTime,
                                                     @Param("endDateTime") LocalDateTime endDateTime,
                                                     @Param("jogak") Jogak jogak
    );

    @Query("SELECT j from DailyJogak j " +
            "JOIN FETCH j.mogak jm JOIN FETCH jm.user " +
            "WHERE jm.user = :user and j.createdAt BETWEEN :today AND :tomorrow")
    List<DailyJogak> findDailyJogaks(@Param(value = "user") User user,
                                     @Param(value = "today") LocalDateTime today,
                                     @Param(value = "tomorrow") LocalDateTime tomorrow);

    void deleteAllByJogak(Jogak jogak);

    List<DailyJogak> findAllByJogak(Jogak jogak);
}
