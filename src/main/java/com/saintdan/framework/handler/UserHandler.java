package com.saintdan.framework.handler;

import static com.saintdan.framework.constant.CommonsConstant.ID;

import com.saintdan.framework.param.UserParam;
import com.saintdan.framework.po.User;
import com.saintdan.framework.repo.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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
public class UserHandler {

  public Mono<ServerResponse> create(ServerRequest request) {
    return ServerResponse.status(HttpStatus.CREATED).body(request.bodyToMono(UserParam.class)
        .flatMap(this::param2Po)
        .flatMap(userRepository::save), User.class);
  }

  public Mono<ServerResponse> all(ServerRequest request) {
    return ServerResponse.ok().body(userRepository.findAll(), User.class);
  }

  public Mono<ServerResponse> get(ServerRequest request) {
    return ServerResponse.ok().body(userRepository.findById(request.pathVariable(ID)), User.class);
  }

  public Mono<ServerResponse> update(ServerRequest request) {
    return ServerResponse.ok().body(userRepository.findById(request.pathVariable(ID))
        .flatMap(user -> request.bodyToMono(UserParam.class)
            .flatMap(param -> param2Po(param, user)
                .flatMap(userRepository::save))), User.class);
  }

  public Mono<ServerResponse> delete(ServerRequest request) {
    return userRepository.deleteById(request.pathVariable(ID))
        .flatMap(r -> ServerResponse.noContent().build());
  }

  private final UserRepository userRepository;

  @Autowired public UserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private Mono<User> param2Po(UserParam param) {
    return Mono.just(User.builder()
        .name(param.getName())
        .usr(param.getUsr())
        .pwd(param.getPwd())
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
