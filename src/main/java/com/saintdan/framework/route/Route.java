package com.saintdan.framework.route;

import com.saintdan.framework.constant.PathConstant;
import com.saintdan.framework.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 12/10/2017
 * @since JDK1.8
 */
@Configuration public class Route {

  @Bean
  public RouterFunction<ServerResponse> userRoute() {
    return route(GET(PathConstant.USER).and(accept(APPLICATION_JSON_UTF8)), userHandler::all)
        .andRoute(GET(PathConstant.USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::get)
        .andRoute(POST(PathConstant.USER).and(accept(APPLICATION_JSON_UTF8)), userHandler::create)
        .andRoute(PUT(PathConstant.USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::update)
        .andRoute(DELETE(PathConstant.USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::delete);
  }

  private final UserHandler userHandler;

  @Autowired public Route(UserHandler userHandler) {
    this.userHandler = userHandler;
  }
}
