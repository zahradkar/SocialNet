package com.martin.socialnet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@WebSe
public class config {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/", "index.html").permitAll() // allows index.html without providing it to browser's url bar
						.requestMatchers("/**").permitAll()
//						.requestMatchers("/style.css").permitAll()
//						.requestMatchers("/auth/**").permitAll()
//						.requestMatchers("/admin/**").hasAuthority("ADMIN")
//						.requestMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginPage("/index")
				)
				.build();
	}
}
