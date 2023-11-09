package com.martin.socialnet.security;

import com.martin.socialnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

	@Autowired
	private UserRepository userRepository;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/**").permitAll()
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.loginPage("/login"))
//				.formLogin(Customizer.withDefaults())
				.build();
	}
	/*

	toto je 2. moznost - neodskusal som ju
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http
				.authorizeHttpRequests(authorizeConfig -> {
					authorizeConfig.requestMatchers("/private").authenticated();
					authorizeConfig.requestMatchers("").permitAll();
				})
				.formLogin(Customizer.withDefaults())
				.build();
	}*/


}
