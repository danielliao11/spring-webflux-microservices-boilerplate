package com.saintdan.framework.handler;

import com.saintdan.framework.po.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 10/10/2017
 * @since JDK1.8
 */
@Component public class EchoHandler {

  public Mono<ServerResponse> echo(ServerRequest request) {
    return ServerResponse.ok().body(request.bodyToMono(User.class), User.class);
  }
}
