package com.mogak.spring.repository;

import com.mogak.spring.domain.jogak.Jogak;
import com.mogak.spring.domain.jogak.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PeriodRepository extends JpaRepository<Period, Integer> {

    //요일 불러오는 메소드
    Optional<Period> findOneByDays(String day);

    @Query("SELECT p " +
            "FROM Period p " +
            "JOIN FETCH p.jogakPeriods jp " +
            "WHERE jp.jogak = :jogak ")
    List<Period> findPeriodsByJogak(@Param(value = "jogak") Jogak jogak);
}
