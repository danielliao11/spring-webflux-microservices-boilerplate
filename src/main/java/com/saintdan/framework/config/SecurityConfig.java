package com.saintdan.framework.config;

import com.saintdan.framework.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 16/10/2017
 * @since JDK1.8
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Bean
  public Flux<MapUserDetailsRepository> userDetailsRepository() {
    return userRepository.findAll().flatMap(u -> Mono.just(new MapUserDetailsRepository(User
        .withUsername(u.getUsr())
        .password(u.getPwd())
        .roles(ADMIN)
        .disabled(u.isEnabledAlias())
        .credentialsExpired(u.isCredentialsNonExpiredAlias())
        .accountLocked(u.isAccountNonLockedAlias())
        .accountExpired(u.isAccountNonExpiredAlias())
        .authorities(u.getAuthorities())
        .build())));
  }

  private static final String ADMIN = "ADMIN";
  private final UserRepository userRepository;

  @Autowired public SecurityConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
