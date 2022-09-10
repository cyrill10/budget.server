package ch.bader.budget.server;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    public static final String BUDGET_PATH = "/budget/**";
    public static final String USER = "USER";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, BUDGET_PATH).hasRole(USER)
            .antMatchers(HttpMethod.POST, BUDGET_PATH)
            .hasRole(USER)
            .antMatchers(HttpMethod.PUT, BUDGET_PATH)
            .hasRole(USER)
            .and()
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .formLogin().disable().cors()
            .configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowedOriginPatterns(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            });

        return http.build();
    }
}
