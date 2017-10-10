package com.saintdan.framework.domain;

import com.saintdan.framework.po.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 09/10/2017
 * @since JDK1.8
 */
@Component public class UserDomain {

  private static final List<User> DATA = new ArrayList<>();

  static {
    DATA.add(User.builder().id(1).name("tom").build());
    DATA.add(User.builder().id(2).name("jerry").build());
  }

  public Mono<User> create(User user) {
    long id = DATA.size() + 1;
    User saved = User.builder().id(id).name(user.getName()).build();
    DATA.add(saved);
    return Mono.justOrEmpty(saved);
  }

  public Flux<User> findAll() {
    return Flux.fromIterable(DATA);
  }

  public Mono<User> findById(long id) {
    return findAll().filter(u -> Objects.equals(u.getId(), id)).singleOrEmpty();
  }

}
