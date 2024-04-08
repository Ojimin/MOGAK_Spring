package com.mogak.spring.repository;

import com.mogak.spring.domain.jogak.DailyJogak;
import com.mogak.spring.domain.jogak.Jogak;
import com.mogak.spring.domain.mogak.Mogak;
import com.mogak.spring.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JogakRepository extends JpaRepository<Jogak, Long> {

    @Query("SELECT j from Jogak j " +
            "JOIN FETCH j.mogak jm JOIN FETCH jm.user JOIN FETCH j.jogakPeriods jp JOIN FETCH jp.period " +
            "WHERE jm.user = :user and jp.period.id = :today")
    List<Jogak> findDailyRoutineJogaks(@Param(value = "user") User user, @Param(value = "today") int todayNum);

    @Query(value = "SELECT j FROM Jogak j WHERE :state Is NULL or j.state = :state")
    List<Jogak> findJogakByState(@Param(value = "state") String state);

//    @Query(value = "SELECT j FROM Jogak j WHERE j.state = :state " +
//            "AND j.startTime < CURRENT_DATE AND j.createdAt >= CURRENT_DATE - 1")
//    List<Jogak> findJogakIsOngoingYesterday(@Param(value = "state") String state);

    List<Jogak> findAllByMogak(Mogak mogak);

    @Query("SELECT DISTINCT j FROM Jogak j JOIN FETCH j.jogakPeriods jp JOIN FETCH jp.period p " +
            "WHERE j.user.id = :userId AND j.isRoutine = true")
    List<Jogak> findAllRoutineJogaksByUser(@Param("userId") Long userId);

    Optional<List<Jogak>> findAllByUserId(Long userId);

    void deleteByUserId(Long userId);

    @Query("SELECT j FROM Jogak j " +
            "JOIN FETCH j.dailyJogaks dj " +
            "WHERE dj = :dailyJogak")
    Optional<Jogak> findByDailyJogak(@Param("dailyJogak") DailyJogak dailyJogak);
}
