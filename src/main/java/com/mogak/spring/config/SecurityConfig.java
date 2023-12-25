package com.mogak.spring.config;

import com.mogak.spring.jwt.JwtTokenFilter;
import com.mogak.spring.jwt.JwtTokenProvider;
import com.mogak.spring.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용하는 경우 사용
                .and()
                //                .authorizeRequests()
//                .antMatchers("/h2-console/*").permitAll()
//                .antMatchers("**").permitAll() // 우선 모든 권한 허용
//                .antMatchers(HttpMethod.POST,"/api/v1/**").authenticated()
                // 모든 post 요청을 인증된 사용자인지 순서 중요. authenticated 🡪 인증된 사용자인지 확인
                // .antMatchers("/api/**").authenticated() // 다른 api는 인증 필요
                //.and()
                .authorizeRequests()
                .antMatchers("/","/swagger-ui/index.html","/swagger-ui.html",
                        "/swagger-ui/**", "/v3/api-docs", "/swagger-resources/**",
                        "/webjars/**","/api-docs/**","/h2-console/*",
                        "/api/auth/**","/api/users/nickname/verify","/api/users/join").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                //UserNamePasswordAuthenticationFilter 적용하기 전에 JWTTokenFilter를 적용 하라는 뜻.
                .build();
    }



}
