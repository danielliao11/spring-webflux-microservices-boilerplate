package com.saintdan.framework.controller;

import com.saintdan.framework.po.User;
import com.saintdan.framework.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    return userRepository.save(user)
        .map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED));
  }

  @GetMapping
  public Flux all() {
    return userRepository.findAll();
  }

  @GetMapping(value = "/{id}")
  public Mono<User> get(@PathVariable String id) {
    return userRepository.findById(id);
  }

  @PutMapping(value = "/{id}")
  public Mono<ResponseEntity<User>> update(@PathVariable String id, @RequestBody User user) {
   return userRepository.findById(id)
       .flatMap(exist-> {
         exist.setName(user.getName());
         return userRepository.save(exist);
       })
       .map(saved -> new ResponseEntity<>(saved, HttpStatus.OK))
       .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping(value = "/{id}")
  public Mono<ResponseEntity> delete(@PathVariable String id) {
    return userRepository.deleteById(id).map(r -> ResponseEntity.noContent().build());
  }

  private final UserRepository userRepository;

  @Autowired public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
