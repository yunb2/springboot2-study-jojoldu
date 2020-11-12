package com.jojoldu.book.springboot.config;

import com.jojoldu.book.springboot.config.auth.CustomOAuth2UserService;
import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정들을 활성화 시킴
public class SecurityConfig extends WebSecurityConfigurerAdapter { // servlet filter 에서 사용할 Security 정보를 구성하기 위한 어댑터

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable() // h2-console 화면을 사용하기 위함
                .and()
                    .authorizeRequests() // URL 별 권한 관리 시작 점, authorizeRequests 가 선언 되어야지 antMatchers 옵션을 사용할 수 있다.
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // index 페이지, 스태틱한 파일들은 permit all
                    .antMatchers(HttpMethod.POST, "/api/v1/**").hasRole(Role.USER.name()) // POST 메소드로 /api/v1/** 를 가진 api 는 USER 권한 가진 사람만
                    .anyRequest().authenticated() // 나머지 URL 들은 모두 인증된 (로그인된) 사용자에만 허용
                .and()
                    .logout() // 로그아웃 처리 진입점
                    .logoutSuccessUrl("/") // 로그아웃 성공시 "/" 로 리다이렉트
                .and()
                    .oauth2Login() // oauth2 로그인 기능에 대한 설정들의 진입점
                    .userInfoEndpoint() // oauth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                    .userService(customOAuth2UserService); // oauth2 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록,
                                                            // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
    }
}

