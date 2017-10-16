package com.saintdan.framework.config;

import com.saintdan.framework.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 16/10/2017
 * @since JDK1.8
 */
@Component
public class CustomUserDetailsRepository implements UserDetailsRepository {

  @Override public Mono<UserDetails> findByUsername(String username) {
    return userRepository.findByUsr(username)
        .flatMap(user -> Mono.just(User
            .withUsername(user.getUsr())
            .password(user.getPwd())
            .roles(user.getRole())
            .authorities(user.getAuthorities())
            .accountExpired(user.isAccountNonExpiredAlias())
            .accountLocked(user.isAccountNonLockedAlias())
            .credentialsExpired(user.isCredentialsNonExpiredAlias())
            .disabled(user.isEnabledAlias()).build()));
  }

  private final UserRepository userRepository;

  @Autowired public CustomUserDetailsRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
