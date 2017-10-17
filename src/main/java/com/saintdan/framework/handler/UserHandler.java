package com.saintdan.framework.handler;

import static com.saintdan.framework.constant.CommonsConstant.ID;

import com.saintdan.framework.filter.ValidateFilter;
import com.saintdan.framework.param.UserParam;
import com.saintdan.framework.po.User;
import com.saintdan.framework.repo.UserRepository;
import java.security.Principal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Domain for {@link User}
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
@Component
@Transactional(readOnly = true)
public class UserHandler {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------

  public Mono<ServerResponse> profile(ServerRequest request) {
    return request.principal()
        .map(Principal::getName) // Get username.
        .flatMap(usr -> ServerResponse.ok()
            .body(userRepository.findByUsr(usr), User.class)); // Return user's profile.
  }

  @Transactional public Mono<ServerResponse> create(ServerRequest request) {
    return request.bodyToMono(UserParam.class)
        .flatMap(param -> validateFilter.validate(param, request.method()) // Validate param.
            .flatMap(err -> ServerResponse
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // If validate failed, return err info.
                .body(BodyInserters.fromObject(err)))
            .switchIfEmpty(ServerResponse
                .status(HttpStatus.CREATED) // If validate success, return save result.
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(param2Po(param)
                    .flatMap(userRepository::save), User.class)));
  }

  public Mono<ServerResponse> all(ServerRequest request) {
    return ServerResponse.ok().body(userRepository.findAll(), User.class);
  }

  public Mono<ServerResponse> get(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(userRepository.findById(request.pathVariable(ID)), User.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  @Transactional public Mono<ServerResponse> update(ServerRequest request) {
    return request.bodyToMono(UserParam.class)
        .flatMap(param -> validateFilter.validate(param, request.method()) // Validate param.
            .flatMap(err -> ServerResponse
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // If validate failed, return err info.
                .body(BodyInserters.fromObject(err)))
            .switchIfEmpty(userRepository.findById(request.pathVariable(ID)) // Find user by id.
                .flatMap(user -> param2Po(param, user)
                    .flatMap(u -> ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(userRepository.save(u), User.class)))
                .switchIfEmpty(
                    ServerResponse.notFound().build()))); // If user find failed, return NOT_FOUND
  }

  @Transactional public Mono<ServerResponse> delete(ServerRequest request) {
    return userRepository.deleteById(request.pathVariable(ID))
        .flatMap(r -> ServerResponse.noContent().build())
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  // --------------------------
  // PRIVATE METHODS AND FIELDS
  // --------------------------

  private final UserRepository userRepository;
  private final ValidateFilter validateFilter;

  @Autowired public UserHandler(UserRepository userRepository, ValidateFilter validateFilter) {
    this.userRepository = userRepository;
    this.validateFilter = validateFilter;
  }

  private Mono<User> param2Po(UserParam param) {
    return Mono.just(User.builder()
        .name(param.getName())
        .usr(param.getUsr())
        .pwd(param.getPwd())
        .role(param.getRole())
        .description(param.getDescription())
        .build());
  }

  private Mono<User> param2Po(UserParam param, User user) {
    user.setName(StringUtils.defaultIfBlank(param.getName(), user.getName()));
    user.setPwd(StringUtils.defaultIfBlank(param.getPwd(), user.getPwd()));
    user.setDescription(StringUtils.defaultIfBlank(param.getDescription(), user.getDescription()));
    return Mono.just(user);
  }
}
