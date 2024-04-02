package com.mogak.spring.repository;

import com.mogak.spring.domain.jogak.Jogak;
import com.mogak.spring.domain.mogak.Mogak;
import com.mogak.spring.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MogakRepository extends JpaRepository<Mogak, Long> {
    @Query
    List<Mogak> findAllByModaratId(Long modaratId);
//    @Query(value = "select m from Mogak m " +
//            "join fetch m.bigCategory join fetch m.mogakPeriods mp join fetch mp.period " +
//            "where m.state = :state and mp.period.id = :today - 1 ")
//    List<Mogak> findAllOngoingToday(@Param("state") String state, @Param("today") int today);
    List<Mogak> findAllByUser(User user);

    void deleteByUserId(Long userId);

    @Query("SELECT m FROM Mogak m JOIN FETCH m.jogaks j WHERE j = :jogak")
    Optional<Mogak> findByJogak(@Param("jogak") Jogak jogak);
}
