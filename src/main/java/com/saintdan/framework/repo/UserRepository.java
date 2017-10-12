package com.saintdan.framework.repo;

import com.saintdan.framework.po.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
public interface UserRepository extends ReactiveCrudRepository<User, String> {

  Mono<User> findByName(String name);

}
