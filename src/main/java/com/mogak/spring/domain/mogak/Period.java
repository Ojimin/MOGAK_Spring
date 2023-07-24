package com.mogak.spring.domain.mogak;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "period")
@Entity
public class Period {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_id")
    private Long id;
    @Column(nullable = false)
    private String day;
}
