package com.saintdan.framework;

import static com.saintdan.framework.constant.PathConstant.PROFILE;
import static com.saintdan.framework.constant.PathConstant.USERS;
import static com.saintdan.framework.constant.PathConstant.USER_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.saintdan.framework.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Entrance of <p>spring-micro-services-boilerplate2</p> <p> "Engine start" </p>
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 09/10/2017
 * @since JDK1.8
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableWebFlux
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @SuppressWarnings("unchecked")
  public RouterFunction<ServerResponse> userRoute() {
    return route(GET(USERS).and(accept(APPLICATION_JSON_UTF8)), userHandler::all)
        .andRoute(GET(USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::get)
        .andRoute(POST(USERS).and(accept(APPLICATION_JSON_UTF8)), userHandler::create)
        .andRoute(PUT(USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::update)
        .andRoute(DELETE(USER_ID).and(accept(APPLICATION_JSON_UTF8)), userHandler::delete)
        .andRoute(GET(PROFILE), userHandler::profile);
  }

  private final UserHandler userHandler;

  @Autowired public Application(UserHandler userHandler) {
    this.userHandler = userHandler;
  }

}
