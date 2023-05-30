package com.classpick.springbootproject.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    //보안 필터 체인
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Disable Cross site request forgery 교차 사이트 요청 비활성화
        http.csrf().disable();

        // Protect endpoints at /api/<type>/secure -> api와 보안에서 엔드포인트 보호
        http.authorizeRequests(configurer ->
                configurer
                          .antMatchers("/api/books/secure/**",
                                  "/api/reviews/secure/**",
                                  "/api/messages/secure/**",
                                  "/api/admin/secure/**")

                          .authenticated())
                .oauth2ResourceServer()
                .jwt();

        //Add CORS filters 원인 필터를 api 엔드포인트에 추가
        http.cors();

        //Add content negotiation strategy 협상 전략에 콘텐츠 추가
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());

        // Force a non-empty response body for 401's to make the response friendly
        // OCTA가 인증되지 않은 상태코드인 401로 반환하도록 강제함
        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }

}
