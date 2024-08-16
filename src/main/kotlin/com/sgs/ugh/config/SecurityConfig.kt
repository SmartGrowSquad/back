package com.sgs.ugh.config

import com.sgs.ugh.security.CustomUserDetailsService
import com.sgs.ugh.security.JwtFilter
import com.sgs.ugh.utils.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy

import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
// 어노테이션으로 메서드 단위 접근 제한
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    @Value("\${cors.allowed-origins.\${spring.profiles.active}}")
    private val allowOriginList: List<String>,
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 지정 리소스에 대해서만 cors 허용
            .cors { it.configurationSource(corsConfigurationSource()) }
            // csrf 비활성화
            .csrf { it.disable() }
            // 세션 관리 비활성화
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            // 폼 로그인 비활성화
            .formLogin { it.disable() }
            // HTTP 기본 인증 비활성화
            .httpBasic { it.disable() }
            // UsernamePasswordAuthenticationFilter 앞에 JwtFilter 추가
            .addFilterBefore(JwtFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
            // 권한 규칙 작성. 메서드 단위 보안 수준 사용
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers(
                        "/v1/auth/**",
                        "/v1/user/create-user",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs"
                    ).permitAll() // 인증 없이 허용
                    .anyRequest().authenticated()  // 모든 요청에 대해 인증 요구
            }

        return http.build()
    }
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = allowOriginList
            allowedMethods = listOf("POST", "GET", "DELETE", "PUT")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}