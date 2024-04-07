package com.mogak.spring.service;

import com.mogak.spring.converter.JogakConverter;
import com.mogak.spring.converter.JogakPeriodConverter;
import com.mogak.spring.domain.common.Weeks;
import com.mogak.spring.domain.jogak.DailyJogak;
import com.mogak.spring.domain.jogak.Jogak;
import com.mogak.spring.domain.jogak.JogakPeriod;
import com.mogak.spring.domain.jogak.Period;
import com.mogak.spring.domain.mogak.Mogak;
import com.mogak.spring.domain.user.User;
import com.mogak.spring.exception.BaseException;
import com.mogak.spring.exception.JogakException;
import com.mogak.spring.exception.MogakException;
import com.mogak.spring.exception.UserException;
import com.mogak.spring.global.ErrorCode;
import com.mogak.spring.repository.*;
import com.mogak.spring.web.dto.jogakdto.JogakRequestDto;
import com.mogak.spring.web.dto.jogakdto.JogakResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class JogakServiceImpl implements JogakService {

    private final UserRepository userRepository;
    private final MogakRepository mogakRepository;
    private final JogakRepository jogakRepository;
    private final JogakPeriodRepository jogakPeriodRepository;
    private final PeriodRepository periodRepository;
    private final DailyJogakRepository dailyJogakRepository;

    /**
     * 자정에 Ongoing인 모든 모각 생성
     */
    @Transactional
    public void createRoutineJogakToday() {
        for (User user: userRepository.findAll()) {
            List<Jogak> jogaks  = jogakRepository.findDailyRoutineJogaks(user, Weeks.getTodayNum());
            for (Jogak jogak : jogaks) {
                dailyJogakRepository.save(JogakConverter.toInitialDailyJogak(jogak));
            }
        }
    }

//    /**
//     * 자정 1분까지 시작하지 않은 조각 실패 처리
//     * +) 자정엔 조각 생성 스케줄이 있어서 1분 이후에 처리
//     */
//    @Transactional
//    public void failRoutineJogakAtMidnight() {
//        List<Jogak> jogaks = jogakRepository.findJogakByState(null);
//        for (Jogak jogak : jogaks) {
//            jogak.updateState(JogakState.FAIL);
//        }
//    }

    /**
     * 새벽 4시까지 종료를 누르지 않은 조각 실패 처리
     */
//    @Transactional
//    public void failJogakAtFour() {
//        List<Jogak> jogaks = jogakRepository.findJogakIsOngoingYesterday(JogakState.ONGOING.name());
//        for (Jogak jogak : jogaks) {
//            jogak.updateState(JogakState.FAIL);
//        }
//    }

    @Transactional
    @Override
    public JogakResponseDto.CreateJogakDto createJogak(JogakRequestDto.CreateJogakDto createJogakDto) {
        Mogak mogak = mogakRepository.findById(createJogakDto.getMogakId())
                .orElseThrow(() -> new MogakException(ErrorCode.NOT_EXIST_MOGAK));
        // 조각 갯수 검증
        if (!validateJogakNum(mogak)) {
            throw new BaseException(ErrorCode.EXCEED_MAX_JOGAK);
        }
        Jogak jogak = jogakRepository.save(JogakConverter.toInitialJogak(mogak, createJogakDto.getTitle(), createJogakDto.getIsRoutine(), createJogakDto.getToday(), createJogakDto.getEndDate()));
        validatePeriod(Optional.ofNullable(createJogakDto.getIsRoutine()), Optional.ofNullable(createJogakDto.getDays()));

        // 루틴이 존재할 경우
        if (createJogakDto.getIsRoutine()) {
            List<Period> periods = new ArrayList<>();
            List<String> requestDays = createJogakDto.getDays();
            if (requestDays == null) {
                throw new BaseException(ErrorCode.NOT_EXIST_ROUTINES);
            }
            List<String> days = new ArrayList<>();
            // 반복주기 추출
            for (String day: requestDays) {
                Period period = periodRepository.findOneByDays(day)
                        .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_DAY));
                periods.add(period);
                // 주기와 오늘이 일치하는 경우
                if (dateToNum(createJogakDto.getToday()) == period.getId()) {
                    dailyJogakRepository.save(JogakConverter.toInitialDailyJogak(jogak));
                }
            }
            // 다대다-조각주기 저장
            for (Period period: periods) {
                jogakPeriodRepository.save(
                        JogakPeriod.builder()
                                .period(period)
                                .jogak(jogak)
                                .build()
                );
                days.add(period.getDays());
            }
            return JogakConverter.toCreateJogakResponseDto(jogak, days);
        }
        // 루틴이 없는 경우
        return JogakConverter.toCreateJogakResponseDto(jogak);
    }

    // 모각의 조각 개수 검증
    private boolean validateJogakNum(Mogak mogak) {
        int nowJogakNum = 0;
        // 현재 유효한 기간 및 종료 날짜가 없는 조각 개수 체크
        for (Jogak jogak: mogak.getJogaks()) {
            if (jogak.getEndAt() == null || jogak.getEndAt().isAfter(LocalDate.now()) ) {
                nowJogakNum++;
            }
        }
        return nowJogakNum < 8;
    }

    @Transactional
    @Override
    public JogakResponseDto.CreateJogakDto updateJogak(Long jogakId, JogakRequestDto.UpdateJogakDto updateJogakDto) {
        Jogak jogak = jogakRepository.findById(jogakId)
                .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_JOGAK));
        validatePeriod(Optional.ofNullable(updateJogakDto.getIsRoutine()), Optional.ofNullable(updateJogakDto.getDays()));
        jogak.update(updateJogakDto.getTitle(), updateJogakDto.getIsRoutine(), updateJogakDto.getEndDate());

        List<DailyJogak> dailyJogaks = dailyJogakRepository.findAllByJogak(jogak);
        if (!dailyJogaks.isEmpty()) {
            dailyJogaks
                    .forEach(dailyJogak ->
                            dailyJogak.getJogak().update(
                                    updateJogakDto.getTitle(),
                                    updateJogakDto.getIsRoutine(),
                                    updateJogakDto.getEndDate()));
        }

        if (updateJogakDto.getDays() != null) {
            updateJogakPeriod(jogak, updateJogakDto.getDays());
        }
        if (updateJogakDto.getIsRoutine() != null && !updateJogakDto.getIsRoutine()) {
            jogakPeriodRepository.deleteAllByJogakId(jogakId);
        }

        return JogakConverter.toCreateJogakResponseDto(jogak);
    }

    private void validatePeriod(Optional<Boolean> isRoutineOptional, Optional<List<String>> daysOptional) {
        isRoutineOptional.ifPresent(isRoutine -> {
            if (isRoutine && daysOptional.isEmpty()) {
                throw new JogakException(ErrorCode.NOT_VALID_PERIOD);
            }
        });
        daysOptional.ifPresent(days -> {
            if (isRoutineOptional.isEmpty() || !isRoutineOptional.get()) {
                throw new JogakException(ErrorCode.NOT_VALID_PERIOD);
            }
        });
    }

    /**
     * 모각주기 업데이트 메소드
     * */
    private void updateJogakPeriod(Jogak jogak, List<String> days) {
        List<Period> periods = new ArrayList<>();
        for (String day : days) {
            Period period = periodRepository.findOneByDays(day)
                    .orElseThrow(() -> new MogakException(ErrorCode.NOT_EXIST_DAY));
            periods.add(period);

            // 주기와 오늘이 일치하는 경우
            if (dateToNum(LocalDate.now()) == period.getId()) {
                dailyJogakRepository.save(JogakConverter.toInitialDailyJogak(jogak));
            }
        }

        List<JogakPeriod> mogakPeriods = jogakPeriodRepository.findAllByJogak_Id(jogak.getId());
        int periodSize = periods.size();
        int mpSize = mogakPeriods.size();

        IntStream.range(0, Math.min(mpSize, periodSize))
                .forEach(i -> mogakPeriods.get(i).updatePeriod(periods.get(i)));
        if (mpSize > periodSize) {
            IntStream.range(periodSize, mpSize)
                    .forEach(i -> jogakPeriodRepository.delete(mogakPeriods.get(i)));
        } else {
            IntStream.range(mpSize, periodSize)
                    .forEach(i -> jogakPeriodRepository.save(JogakPeriodConverter.toJogakPeriod(periods.get(i), jogak)));
        }
    }

    @Override
    public JogakResponseDto.GetOneTimeJogakListDto getDailyJogaks(LocalDate day) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_EXIST_USER));
        List<Jogak> jogakList = mogakRepository.findAllByUser(user).stream()
                .flatMap(mogak -> mogak.getJogaks().stream()
                        .filter(jogak -> !jogak.getIsRoutine()))
                .collect(Collectors.toList());
        List<DailyJogak> dailyJogak = dailyJogakRepository.findDailyJogaks(user, day.atStartOfDay(), day.atStartOfDay().plusDays(1));
        return JogakConverter.toGetOneTimeJogakListResponseDto(jogakList, dailyJogak);
    }

    @Override
    public JogakResponseDto.GetDailyJogakListDto getDayJogaks(LocalDate day) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_EXIST_USER));
        if (day.isAfter(LocalDate.now())) {
            // 미래 루틴 조각 가져오기
            List<Jogak> userRoutineJogaks = jogakRepository.findDailyRoutineJogaks(user, dateToNum(day));
            return JogakConverter.toGetDailyJogakListResponseDto(
                    userRoutineJogaks.stream()
                            // 여기서 npe 발생
                            .filter(jogak -> jogak.getEndAt().isAfter(day))
                            .map(JogakConverter::toDailyJogakResponseDto)
                            .collect(Collectors.toList()));
        }
        return JogakConverter.toGetDailyJogakListResponseDto(dailyJogakRepository.findDailyJogaks(
                user, day.atStartOfDay(), day.atStartOfDay().plusDays(1)));
    }

    /**
     * 주간/월간 루틴 가져오는 API
     * */
    @Override
    public List<JogakResponseDto.GetRoutineJogakDto> getRoutineJogaks(LocalDate startDate, LocalDate endDate) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_EXIST_USER));
        Long userId = user.getId();
        List<LocalDate> pastDates = getPastDates(startDate, endDate);
        List<LocalDate> futureDates = getFutureDates(startDate, endDate);
        List<JogakResponseDto.GetRoutineJogakDto> routineJogaks = new ArrayList<>();

        // 오늘 + 이전 가져오기
        if (!pastDates.isEmpty()) {
            List<DailyJogak> pastJogaks = dailyJogakRepository.findByDateRange(startDate.atStartOfDay(), endDate.atStartOfDay());
            routineJogaks.addAll(pastJogaks.stream()
                    .map(DailyJogak::getRoutineJogakDto)
                    .collect(Collectors.toList()));
        }

        // 미래 가져오기
        if (!futureDates.isEmpty()) {
            Map<Integer, List<Jogak>> dailyRoutineJogaks = new HashMap<>();
            // 월~금 루틴 조각 가져오기
            List<Jogak> userRoutineJogaks = jogakRepository.findAllRoutineJogaksByUser(userId);
            IntStream.rangeClosed(1, 7).forEach(i -> {
                List<Jogak> matchingJogaks = userRoutineJogaks.stream()
                        .filter(jogak -> jogak.getJogakPeriods().stream()
                                .anyMatch(jogakPeriod -> {
                                    Period period = jogakPeriod.getPeriod();
                                    return i == period.getId();
                                }))
                        .collect(Collectors.toList());
                dailyRoutineJogaks.put(i, matchingJogaks);
                log.info("루틴 day: " + i + " " + dailyRoutineJogaks.get(i));
            });
            // 요일 값 대입
            for (LocalDate date: futureDates) {
                dailyRoutineJogaks.get(dateToNum(date))
                        .forEach(i -> {
                            log.info(i.getEndAt() + " , " + date);
                            // 기간에 해당하지 않는 조각은 가져오지 않는 로직
                            if (i.getEndAt().isAfter(date)) {
                                routineJogaks.add(DailyJogak.getFutureRoutineJogakDto(date, i.getTitle()));
                            }
                        });
            }
        }
        return routineJogaks;
    }

    private List<LocalDate> getPastDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        List<LocalDate> pastDates = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.isBefore(today)) {
                pastDates.add(date);
            }
        }
        return pastDates;
    }

    private List<LocalDate> getFutureDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        List<LocalDate> futureDates = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.isAfter(today)) {
                futureDates.add(date);
            }
        }
        return futureDates;
    }

    @Transactional
    @Override
    public JogakResponseDto.JogakDailyJogakDto startJogak(Long jogakId) {
        Jogak jogak = jogakRepository.findById(jogakId)
                .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_JOGAK));
        if (jogak.getIsRoutine() || dailyJogakRepository.findByCreatedAtBetweenAndId(LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay().plusDays(1), jogakId).isPresent()) {
            throw new JogakException(ErrorCode.ALREADY_START_JOGAK);
        }
        DailyJogak dailyJogak = dailyJogakRepository.save(JogakConverter.toInitialDailyJogak(jogak));
        return JogakConverter.toJogakDailyJogakDto(jogak, dailyJogak);
    }

    @Transactional
    @Override
    public JogakResponseDto.JogakDailyJogakDto successJogak(Long dailyJogakId) {
        DailyJogak dailyjogak = dailyJogakRepository.findById(dailyJogakId)
                .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_JOGAK));
        if (dailyjogak.getIsAchievement()) {
            throw new BaseException(ErrorCode.ALREADY_END_JOGAK);
        }

        updateAchievement(true, dailyjogak);

        return JogakConverter.toJogakDailyJogakDto(dailyjogak.getJogak(), dailyjogak);
    }

    @Transactional
    @Override
    public JogakResponseDto.JogakDailyJogakDto failJogak(Long dailyJogakId) {
        DailyJogak dailyJogak = dailyJogakRepository.findById(dailyJogakId)
                .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_JOGAK));
        if (!dailyJogak.getIsAchievement()) {
            throw new BaseException(ErrorCode.NOT_SUCCESS_DAILY_JOGAK);
        }

        updateAchievement(false, dailyJogak);
        
        return JogakConverter.toJogakDailyJogakDto(dailyJogak.getJogak(), dailyJogak);
    }

    private void updateAchievement(boolean achievement, DailyJogak dailyJogak) {
        Jogak jogak = dailyJogak.getJogak();
        dailyJogak.updateAchievement(achievement);

        // 조각 성공
        if (achievement) {
            jogak.increaseAchievements();
            return;
        }

        // 조각 실패
        jogak.decreaseAchievements();
    }

    @Override
    public JogakResponseDto.DetailJogakDto getJogakDetail(Long jogakId) {
        Jogak jogak = jogakRepository.findById(jogakId)
                .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_JOGAK));
        Mogak mogak = mogakRepository.findByJogak(jogak)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MOGAK));

        if (jogak.getIsRoutine()) {
            List<String> periods = periodRepository.findPeriodsByJogak(jogak).stream()
                    .map(Period::getDays)
                    .collect(Collectors.toList());
            return JogakConverter.toGetJogakDetailResponseDto(jogak, mogak.getColor(), periods);
        }
        return JogakConverter.toGetJogakDetailResponseDto(jogak, mogak.getColor());
    }

    @Transactional
    @Override
    public void deleteJogak(Long jogakId) {
        Jogak jogak = jogakRepository.findById(jogakId)
                .orElseThrow(() -> new JogakException(ErrorCode.NOT_EXIST_JOGAK));
        jogakPeriodRepository.deleteAllByJogakId(jogakId);
        dailyJogakRepository.deleteAllByJogakId(jogakId);
        jogakRepository.deleteById(jogakId);
    }

    private int getTodayNum(LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    private int dateToNum(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue();
    }
}
