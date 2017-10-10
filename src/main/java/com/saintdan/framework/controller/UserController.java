package com.saintdan.framework.controller;

import com.saintdan.framework.domain.UserDomain;
import com.saintdan.framework.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 09/10/2017
 * @since JDK1.8
 */
@RestController @RequestMapping(value = "/users")
public class UserController {

  @PostMapping
  public Mono<ResponseEntity<User>> create(@RequestBody User user) {
    return userDomain.create(user)
        .map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED));
  }

  @GetMapping
  public Flux all() {
    return userDomain.findAll();
  }

  @GetMapping(value = "/{id}")
  public Mono<User> get(@PathVariable Long id) {
    return userDomain.findById(id);
  }

  private final UserDomain userDomain;

  @Autowired public UserController(UserDomain userDomain) {
    this.userDomain = userDomain;
  }
}
