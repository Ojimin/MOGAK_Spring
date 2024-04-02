package com.mogak.spring.converter;

import com.mogak.spring.domain.jogak.DailyJogak;
import com.mogak.spring.domain.jogak.Jogak;
import com.mogak.spring.domain.mogak.Mogak;
import com.mogak.spring.web.dto.jogakdto.JogakResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class JogakConverter {
    public static Jogak toInitialJogak(Mogak mogak, String title, Boolean isRoutine, LocalDate today, LocalDate endAt) {
        return Jogak.builder()
                .user(mogak.getUser())
                .mogak(mogak)
                .category(mogak.getBigCategory())
                .title(title)
                .isRoutine(isRoutine)
                .achievements(0)
                .startAt(today)
                .endAt(endAt)
                .build();
    }

    public static Jogak toJogak(DailyJogak dailyJogak) {
        return Jogak.builder()
                .mogak(dailyJogak.getMogak())
                .category(dailyJogak.getCategory())
                .title(dailyJogak.getTitle())
                .isRoutine(dailyJogak.getIsRoutine())
                .build();
    }

    public static DailyJogak toDailyJogakResponseDto(Jogak jogak) {
        return DailyJogak.builder()
                .id(-1L)
                .mogak(jogak.getMogak())
                .jogakId(jogak.getId())
                .title(jogak.getTitle())
                .category(jogak.getCategory())
                .isRoutine(jogak.getIsRoutine())
                .isAchievement(false)
                .build();
    }

    public static DailyJogak toInitialDailyJogak(Jogak jogak) {
        return DailyJogak.builder()
                .mogak(jogak.getMogak())
                .category(jogak.getCategory())
                .title(jogak.getTitle())
                .isAchievement(false)
                .jogakId(jogak.getId())
                .isRoutine(jogak.getIsRoutine())
                .build();
    }

    public static JogakResponseDto.CreateJogakDto toCreateJogakResponseDto(Jogak jogak) {
        return JogakResponseDto.CreateJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .achievements(jogak.getAchievements())
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.CreateJogakDto toCreateJogakResponseDto(Jogak jogak, List<String> days) {
        return JogakResponseDto.CreateJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .days(days)
                .achievements(jogak.getAchievements())
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.DetailJogakDto toGetJogakDetailResponseDto(Jogak jogak, String color) {
        return JogakResponseDto.DetailJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .achievements(jogak.getAchievements())
                .color(color)
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.DetailJogakDto toGetJogakDetailResponseDto(Jogak jogak, String color, List<String> days) {
        return JogakResponseDto.DetailJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .days(days)
                .achievements(jogak.getAchievements())
                .color(color)
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.GetOneTimeJogakDto toGetOneTimeJogakResponseDto(Jogak jogak, Boolean bool) {
        return JogakResponseDto.GetOneTimeJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .isAlreadyAdded(bool)
                .achievements(jogak.getAchievements())
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.GetJogakDto toGetJogakResponseDto(Jogak jogak) {
        return JogakResponseDto.GetJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .achievements(jogak.getAchievements())
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.GetJogakDto toGetJogakResponseDto(Jogak jogak, Boolean isAlreadyAdded) {
        return JogakResponseDto.GetJogakDto.builder()
                .jogakId(jogak.getId())
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .days(jogak.getPeriods())
                .isAlreadyAdded(isAlreadyAdded)
                .achievements(jogak.getAchievements())
                .startDate(jogak.getStartAt())
                .endDate(jogak.getEndAt())
                .build();
    }

    public static JogakResponseDto.GetDailyJogakDto toGetDailyJogakResponseDto(DailyJogak dailyJogak) {
        return JogakResponseDto.GetDailyJogakDto.builder()
                .jogakId(dailyJogak.getJogakId())
                .dailyJogakId(dailyJogak.getId())
                .mogakTitle(dailyJogak.getMogak().getTitle())
                .category(dailyJogak.getCategory().getName())
                .title(dailyJogak.getTitle())
                .isRoutine(dailyJogak.getIsRoutine())
                .isAchievement(dailyJogak.getIsAchievement())
                .build();
    }

    public static JogakResponseDto.GetDailyJogakDto toGetFutureDailyJogakResponseDto(Jogak jogak) {
        return JogakResponseDto.GetDailyJogakDto.builder()
                .dailyJogakId(-1L)
                .mogakTitle(jogak.getMogak().getTitle())
                .category(jogak.getCategory().getName())
                .title(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .isAchievement(false)
                .build();
    }

    public static JogakResponseDto.GetOneTimeJogakListDto toGetOneTimeJogakListResponseDto(List<Jogak> jogaks, List<DailyJogak> dailyJogaks) {
        return JogakResponseDto.GetOneTimeJogakListDto.builder()
                .jogaks(jogaks.stream()
                        .map(jogak -> toGetOneTimeJogakResponseDto(jogak, findCorrespondingDailyJogak(jogak, dailyJogaks)))
                        .collect(Collectors.toList()))
                .size(jogaks.size())
                .build();
    }

    private static Boolean findCorrespondingDailyJogak(Jogak jogak, List<DailyJogak> dailyJogaks) {
        return dailyJogaks.stream()
                .anyMatch(dailyJogak -> dailyJogak.getJogakId() == jogak.getId());
    }

    public static JogakResponseDto.GetDailyJogakListDto toGetDailyJogakListResponseDto(List<DailyJogak> dailyJogaks) {
        return JogakResponseDto.GetDailyJogakListDto.builder()
                .dailyJogaks(dailyJogaks.stream()
                        .map(JogakConverter::toGetDailyJogakResponseDto)
                        .collect(Collectors.toList()))
                .size(dailyJogaks.size())
                .build();
    }

    public static JogakResponseDto.JogakSuccessDto toSuccessJogak(Jogak jogak) {
        return JogakResponseDto.JogakSuccessDto.builder()
                .title(jogak.getMogak().getTitle())
                .mogakTitle(jogak.getMogak().getTitle())
                .title(jogak.getCategory().getName())
                .build();
    }

    public static JogakResponseDto.JogakDailyJogakDto toJogakDailyJogakDto(Jogak jogak, DailyJogak dailyJogak) {
        return JogakResponseDto.JogakDailyJogakDto.builder()
                .jogakId(jogak.getId())
                .dailyJogakId(dailyJogak.getId())
                .title(dailyJogak.getTitle())
                .mogakTitle(jogak.getTitle())
                .category(jogak.getTitle())
                .isRoutine(jogak.getIsRoutine())
                .isAchievement(dailyJogak.getIsAchievement())
                .achievements(jogak.getAchievements())
                .build();
    }
}
