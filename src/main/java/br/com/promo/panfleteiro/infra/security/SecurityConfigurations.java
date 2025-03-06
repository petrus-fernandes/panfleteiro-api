package br.com.promo.panfleteiro.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {
        return https.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "/v1/auth/register").hasRole("ADMIN"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.PUT, "/v1/auth/usuarios").hasRole("ADMIN"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/v1/auth/usuarios").hasRole("ADMIN"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.DELETE, "/v1/auth/usuarios/**").hasRole("ADMIN"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/v1/anuncios/**").permitAll())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/v1/**").hasRole("USER"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.DELETE, "/v1/**").hasRole("USER"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "/v1/**").hasRole("USER"))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.PUT, "/v1/**").hasRole("USER"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }


}
